#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#define BUFSIZE 32
typedef unsigned char uchar;

void buffer_shift(uchar *buffer, long long *text_len, int ex_len){
    //moving last ex_len elements to the beginning and reading others
    memcpy(buffer, &buffer[BUFSIZE-ex_len], ex_len);
    (*text_len) += fread(&buffer[ex_len], sizeof(char), BUFSIZE-ex_len, stdin);
}

void prepare_shift_table(uchar *example, int *numbers, int ex_len){
    for(int i = 0; i < 256; i++) numbers[i] = ex_len;
    for(int i = ex_len - 2; i >= 0; i--){
        numbers[example[i]] = ex_len - 1 - i;
        for(int j = i + 1; j <= ex_len - 2; j++){
            if(example[i]==example[j]){
                numbers[example[i]] = numbers[example[j]];
            }
        }
    }
    for(int i = ex_len - 2; i >= 0; i--){
        if(example[i]==example[ex_len - 1]){
            numbers[example[ex_len - 1]]=numbers[example[i]];
            break;
        }
    }
}

int main(){
    uchar example[20];
    if (fgets((char*)example, 19, stdin) == NULL) return 0;
    int ex_len = strlen((char*)example)-1, shift_table[256];
    uchar buffer[BUFSIZE+1];
    long long text_len = fread(buffer, sizeof(uchar), BUFSIZE, stdin);
    prepare_shift_table(example, shift_table, ex_len);
    //start of algorithm
    int rel_number = ex_len - 1;
    long long abs_number = rel_number;
    while(abs_number < text_len){
        for(int j = 0; j < ex_len; j++){
            printf("%lld ",abs_number+1-j);
            if (buffer[rel_number-j] != example[ex_len-j-1]){
                break;
            }
        }
        int shift = shift_table[buffer[rel_number]];
        rel_number+=shift;
        abs_number+=shift;
        if(rel_number > BUFSIZE){
            buffer_shift(buffer, &text_len, ex_len);
            rel_number = (rel_number % BUFSIZE) + ex_len;
        }
    }
    return 0;
}
