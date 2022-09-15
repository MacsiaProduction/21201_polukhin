#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <assert.h>
#include <stdbool.h>
#include <limits.h>

#define MAXLENCODE (64)
#define BUFLEN (1<<20)

typedef unsigned char uchar;
typedef unsigned int uint;
//decode
typedef struct node {
	struct node *left;
	struct node *right;
	int key;
} node;

typedef struct read_info {
    FILE *in;
    int buflen;
    int bufcur;
    uchar* buffer;
    uchar active_bits;
} read_info;

void free_tree(node* k) {
    if (k) {
        free_tree(k->left);
        free_tree(k->right);
        free(k);
    }
}

node* create_node() {
    node* NEW = (node*)malloc(sizeof(node));
    NEW->key = -1;
    NEW->left = 0;
    NEW->right = 0;
    return NEW;
}

bool get_bytes(read_info* info) { //returns true if succeded
    int tmp = fread(info->buffer, sizeof(uchar), BUFLEN, info->in);
    if (!tmp) return 0;
    int c = fgetc(info->in);
    if (c == EOF) {
        info->buflen = (tmp-1)*8 + info->active_bits;
    } else info->buflen = (tmp) * 8;
    ungetc(c, info->in);
    return 1;
}

int get_bit(read_info* info) { //returns -1, if failed to get new bit
    if (info->buflen == info->bufcur) {
        info->bufcur = 0;
        if (!get_bytes(info)) return -1;
    }
    int ans = !!(info->buffer[info->bufcur/8] & (1<<(7-(info->bufcur%8))));
    info->bufcur++;
    return ans;
}   

uchar get_uchar(read_info* info) {
    uchar tmp = 0;
    for (int i = 0; i < 8; i++) {
        int bit = get_bit(info);
        if (bit) tmp |= (1<<(7-i));
        assert (bit != -1);
    }
    return tmp;
}

void read_tree(node* k, read_info* info) {
    int bit = get_bit(info);
    assert(bit != -1);
    if (bit) {
        if (!k->left)  k->left = create_node();
        if (!k->right) k->right = create_node();
        read_tree(k->left, info);
        read_tree(k->right, info);
    } else {
        k->key = get_uchar(info);
    }
}

void decode_text(FILE* out, node* k, read_info* info) {
    node* tmp = k;
    while (1) {
        int kek = get_bit(info);
        if (kek == -1) break;
        tmp = kek ? tmp->right : tmp->left;
        if (tmp->key != -1) {
            fprintf(out, "%c", (uchar)tmp->key);
            tmp = k;
        } 
    }
    assert(tmp == k); //checkpoint
}

void decode(FILE *in, FILE *out) {
    read_info* info = (read_info*)calloc(1, sizeof(read_info));
    info->buffer = (uchar*)malloc((BUFLEN) * sizeof(uchar));
    info->in = in;
    if (1 != fscanf(in, "%c", &info->active_bits)) exit(0);
    assert(info->active_bits <= 8); //checkpoint
    node* k = create_node();
    read_tree(k, info);
    if (k->key != -1) { //degenerate case 
        k->left = create_node();
        k->left->key = k->key;
        k->key = -1;
    }
    decode_text(out, k, info);
    free_tree(k);
    free(info->buffer);
    free(info);
} 
//encode
typedef struct alphabet {
    unsigned long long frequency;
    node* k;
} alphabet;

typedef struct write_info {
    FILE* out;
    uchar buffer;
    int bufcur;
} write_info;

void count(FILE *in, alphabet* a) { //counting frequency of symbols
    int c;
    while((c = fgetc(in)) != EOF) {
        a[(uchar) c].frequency++;
    }
}

node* build_huffman_tree(alphabet* a) {
    int chars = 0;
    for(int i = 0; i < 256; i++)
        if (a[i].frequency != 0) chars++;
    while (chars >= 2) {
        unsigned long long i1 = 0, i2 = 0, frequency_min1 = ULONG_MAX, frequency_min2 = ULONG_MAX;
        for(int i = 0; i < 256; i++) { //searching for two mins
            if (!a[i].frequency) continue;
            else if (a[i].frequency <= frequency_min1) {
                frequency_min2 = frequency_min1;
                frequency_min1 = a[i].frequency;
                i2 = i1;
                i1 = i;
            }
            else if (a[i].frequency <= frequency_min2) {
                frequency_min2 = a[i].frequency;
                i2 = i;
            }
        }
        a[i1].frequency += a[i2].frequency;
        a[i2].frequency = 0;
        node* new_node = create_node();
        new_node->left = a[i1].k;
        new_node->right = a[i2].k;
        a[i1].k = new_node;
        a[i2].k = 0;
        chars--;
    }
    for(int i = 0; i < 256; i++) {
        if (a[i].frequency != 0) {
            return a[i].k;
        }
    }
    exit(0); // i can't be here
}

void generate_codes(node* k, char* way, char codes[256][MAXLENCODE], int code_len[256]) { 
    //generating codes from tree by recursion
    if (k->key != -1) {
        strcat(codes[k->key], way);
        code_len[k->key] = strlen(way);
    }
    else {
        char* way_tmp = (char*)calloc(strlen(way) + 2, sizeof(char));
        strcat(way_tmp, way);
        if (k->left) {
            way_tmp[strlen(way)] = '0';
            generate_codes(k->left, way_tmp, codes, code_len);
        }
        if (k->right) {
            way_tmp[strlen(way)] = '1';
            generate_codes(k->right, way_tmp, codes, code_len);
        }
        free(way_tmp);
    }
}

void bit_write(write_info* info) {
    fprintf(info->out, "%c", info->buffer);
    info->buffer = 0;
    info->bufcur = 0;
}

void set_bit(int bit, write_info* info) {
    if (bit) info->buffer |= 1 << (7-(info->bufcur));
    info->bufcur++;
    if (info->bufcur == 8) bit_write(info);
}

void add_uchar_to_buf(uchar c, write_info* info) { //adds uchar to buffer
    for (int i = 0; i < 8; i++) {
        set_bit(!!(c&(1<<(7-i))), info);
    }
}

void write_tree(node* k, write_info* info) { //printing key for decoding
    if (k->key == -1) {
        set_bit(1, info);
        if (k->left)  write_tree(k->left, info);
        if (k->right) write_tree(k->right, info);
    } else {
        set_bit(0, info);
        add_uchar_to_buf(k->key, info);
    }
}

void add_code_to_buf(char code[MAXLENCODE], int code_len, write_info* info) {
    for (int i = 0; i < code_len; i++) {
        set_bit(code[i] - '0', info);
    }
}

int encode_text(FILE *in, char codes[256][MAXLENCODE], int code_len[256], write_info* info) {
    char buffer[BUFLEN];
    int counter;
    while ((counter = fread(buffer, sizeof(char), BUFLEN, in)) != 0) {
        for(int i = 0; i < counter; i++) {
            add_code_to_buf(codes[(uchar)buffer[i]], code_len[(uchar)buffer[i]], info);
        }
    }
    int active_bits = info->bufcur;
    if (info->bufcur) {
        bit_write(info);
    }
    return active_bits;
}

void encode(FILE *in, FILE *out) {
    fseek(out, 1, SEEK_SET); //pos 0 is reserved for number of active bits in last char
    alphabet* a = (alphabet*)malloc(256 * sizeof(alphabet));
    for(int i = 0; i < 256; i++) { //init alphabet
        a[i].frequency = 0;
        a[i].k = create_node();
        a[i].k->key = i;
    }
    count(in, a);
    node* ht = build_huffman_tree(a);
    write_info* info = (write_info*)calloc(1, sizeof(write_info));
    info->out = out;
    write_tree(ht, info);
    if (ht->key != -1) { //degenerate case
        ht->left = create_node();
        ht->left->key = ht->key;
        ht->key = -1;
    }
    int code_len[256] = {0};
    char codes[256][MAXLENCODE] = {{0}};
    char way[3] = {0};
    generate_codes(ht, way, codes, code_len);
    for(int i = 0; i < 256; i++) free_tree(a[i].k);
    free(a);
    fseek(in, 1, SEEK_SET);
    int active_bits = encode_text(in, codes, code_len, info);
    if (!active_bits && (ftell(out) != 1)) active_bits = 8; //if there are any bytes
    fseek(out, 0, SEEK_SET);
    fprintf(out, "%c", active_bits);
    free(info);
}

int main(void) {
    FILE *in, *out;
    if((in = fopen("in.txt", "rb")) == NULL) printf("Cannot open input file.\n");
    if((out = fopen("out.txt", "wb")) == NULL) printf("Cannot open output file.\n");
    char c = fgetc(in);
    if(c == 'c') encode(in, out);
    else if(c == 'd') decode(in, out);
    fclose(in);
    fclose(out);
    return(0);
}
