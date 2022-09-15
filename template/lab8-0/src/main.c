#include <stdlib.h>
#include <stdio.h>
#include <stdbool.h>
#include <assert.h>
#include <limits.h>

typedef struct arr_with_len {
    int* arr;
    int actual_len;
} cool_array;

typedef struct node {
    int start;
    int end;
    int len;
} node;

void get_N_M(int *N, int *M) {
    if (2 != scanf("%d\n%d", N, M)) {
        printf("bad number of lines\n");
        exit(0);
    }
    if ((5000 < (*N)) || ((*N) < 0)) {
        printf("bad number of vertices\n");
        exit(0);
    }
    if ((((*N) * ((*N) - 1) / 2) < (*M)) || ((*M) < 0)) {
        printf("bad number of edges\n");
        exit(0);
    }
    if ((*N) == 0) {
        printf("no spanning tree\n");
        exit(0);
    }
    if (((*M) == 0) && ((*N) == 1)) exit(0);
}

int cmp(const void* a, const void* b) {
    return ((node*)a)->len - ((node*)b)->len;
}

node* get_list(int N, int M) {
    node* list = (node*)calloc(M, sizeof(node));
    for (int i = 0; i < M; i++) { //init list
        int v, u;
        long long int w;
        if (3 != scanf("%d %d %lld", &v, &u, &w)) {
            printf("bad number of lines\n");
            free(list);
            exit(0);
        }
        if (!((1 <= v) && (v <= N) && (1 <= u) && (u <= N))){
            printf("bad vertex\n");
            free(list);
            exit(0);
        }
        if (!((w>=0) && (w<=INT_MAX))) {
            printf("bad length\n");
            free(list);
            exit(0);
        }
        list[i].start = v;
        list[i].end = u;
        list[i].len = w;
    }
    qsort(list, M, sizeof(node), cmp);
    return list;
}

int get_root(int* p, int v) {
    if (p[v] == v) return v;
    else return p[v] = get_root(p, p[v]);
}

bool merge(int* p, int* rk, int a, int b) {
    int ra = get_root(p, a), rb = get_root(p, b);
    if (ra == rb) return false;
    if (rk[ra] < rk[rb]) {
        p[ra] = rb;
    } else if (rk[rb] < rk[ra]) {
        p[rb] = ra;
    } else {
        p[ra] = rb;
        rk[rb]++;
    }
    return true;
}

bool MinimumSpanningTree(node* arr, cool_array* ans, int N, int M) { //returns true if succeded
    ans->arr = (int*)malloc(N*sizeof(int));
    int* p = (int*)malloc((N+1)*sizeof(int));
    int* rk = (int*)malloc((N+1)*sizeof(int));
    for (int i = 0; i <= N; i++) { //init dsu
        p[i] = i;
        rk[i] = 1;
    }
	for (int i = 0; i < M; i++) { 
        if (merge(p, rk, arr[i].start, arr[i].end)) {
            ans->arr[(ans->actual_len)++] = i;
        }
    }
    free(p);
    free(rk);
    return ans->actual_len == (N-1);
}

int main(void) {
    int N, M;
    get_N_M(&N, &M);
    node* list = get_list(N, M);
    cool_array* ans = (cool_array*)calloc(1, sizeof(cool_array));;
    if (MinimumSpanningTree(list, ans, N, M)) {
        for (int i = 0; i < ans->actual_len; i++) {
            printf("%d %d\n", list[ans->arr[i]].start, list[ans->arr[i]].end);
        }
    } else printf("no spanning tree\n");
    free(ans->arr);
    free(ans);
    free(list);
    return 0;
}
