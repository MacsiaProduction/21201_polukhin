#include <stdlib.h>
#include <stdio.h>
#include <stdbool.h>
#include <assert.h>

typedef struct node {
    int number; //actual len of array
    int color;
    short* vertex;
    //color 0 - white - not visited
    //color 1 - gray  - visited
    //color 2 - black - all outgoing vertices are black
} node;

typedef struct arr_with_curlen {
    int* arr;
    int actual_len;
} cool_array;

void free_node(node* list, int N) {
    for (int i = 1; i <= N; i++) free(list[i].vertex);
    free(list);
}

void free_cool_array(cool_array* ans) {
    free(ans->arr);
    free(ans);
}

void add_vertex(node* list, int v, int u) {
    list[v].vertex[(list[v].number)++] = u;
}

node* read_graph(FILE* in, FILE* out, int N, int M) {
    long checkpoint = ftell(in);
    int* frequency = (int*)calloc(N+1, sizeof(int));
    for (int i = 0; i < M; i++) { //analyzing len of adjacency list for each vertex
        int v = 0, u = 0;
        if (2 != fscanf(in, "%d %d", &v, &u)) {
            free(frequency);
            fprintf(out, "bad number of lines\n");
            exit(0);
        }
        if (!((1 <= v)&&(v <= N)&&(1 <= u)&&(u <= N))){
            free(frequency);
            fprintf(out, "bad vertex\n");
            exit(0);
        }
        frequency[v]++;
    }
    node* list = (node*)calloc(N+1, sizeof(node));
    if (!list) fprintf(stderr, "calloc failure");
    for (int i = 1; i <= N; i++) { //init adjacency list
        list[i].vertex = (short*)calloc(frequency[i]+1, sizeof(short));
    }
    free(frequency);
    fseek(in, checkpoint, SEEK_SET);
    for (int i = 0; i < M; i++) { //adding vertex
        int v = 0, u = 0;   
        if (2 != fscanf(in, "%d %d", &v, &u)) exit(-1);
        add_vertex(list, v, u);
    }
    return list;    
}

bool recursion_part(node* list, cool_array* ans, int N, int cur) { //returns true if failed to sort
    list[cur].color = 1;
    for(int j = 0; j < list[cur].number; j++) {
        if (list[list[cur].vertex[j]].color == 2) continue;
        if (list[list[cur].vertex[j]].color == 1) return true;
        if (recursion_part(list, ans, N, list[cur].vertex[j])) return true;
    }
    ans->arr[(ans->actual_len)++] = cur;
    list[cur].color = 2;
    return false;
}

bool topologic_sort(node* list, cool_array* ans, int N) { //returns true if succeded, false if not
    ans->actual_len = 0;
    ans->arr = (int*)calloc(N+1, sizeof(int));
    for(int i = 1; i <= N; i++) {
        if(!list[i].color) {
            if(recursion_part(list, ans, N, i)){
                return false;
            }
        }
    }
    return true;
}

int main(void) {
    FILE *in, *out;
    if((in = fopen("in.txt", "rt")) == NULL) {
        printf("Cannot open input file.\n");
        return 0;
    }
    if((out = fopen("out.txt", "wt")) == NULL) {
        printf("Cannot open output file.\n");
        fclose(in);
        return 0;
    }
    int N = 0, M = 0;
    if (1 != fscanf(in, "%d", &N)) {
        fprintf(out, "bad number of lines\n");
        goto end;
    }
    if ((2000 < N) || (N < 0)) {
        fprintf(out, "bad number of vertices\n");
        goto end;
    }
    if (1 != fscanf(in, "%d", &M)) {
        fprintf(out, "bad number of lines\n");
        goto end;
    }
    if ((N * (N - 1) / 2 < M) || (M < 0)) {
        fprintf(out, "bad number of edges\n");
        goto end;
    }
    node* list = read_graph(in, out, N, M);
    cool_array* ans = (cool_array*)calloc(N, sizeof(cool_array));
    if (topologic_sort(list, ans, N)) {
        for (int i = ans->actual_len - 1; i >= 0; i--) {
            fprintf(out, "%d ", ans->arr[i]);
        }
    } else fprintf(out, "impossible to sort\n");
    free_cool_array(ans);
    free_node(list, N);
    end:
    fclose(in);
    fclose(out);
    return EXIT_SUCCESS;
}
