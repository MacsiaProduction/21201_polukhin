#include <stdio.h>
#include <stdlib.h>
#include <limits.h>
#include <stdbool.h>
#include <assert.h>

typedef struct pair {
    int dest;
    int weight;
} pair;

typedef struct adj_table {
    pair* arr;
    int actual_len;
    int checked_len;
} adj_table;

void get_N_M(FILE* in, FILE* out, int *N, int *M) {
    if (2 != fscanf(in, "%d\n%d", N, M)) {
        fprintf(out, "bad number of lines\n");
        //fclose(in);
        //fclose(out);
        exit(0);
    }
    if ((*N) == 0) {
        fprintf(out, "no spanning tree\n");
        //fclose(in);
        //fclose(out);
        exit(0);
    }
    if (((*M) == 0) && ((*N) == 1)) {
        //fclose(in);
        //fclose(out);
        exit(0);
    }
    if ((5000 < (*N)) || ((*N) < 0)) {
        fprintf(out, "bad number of vertices\n");
        //fclose(in);
        //fclose(out);
        exit(0);
    }
    if (((*N) * ((*N) - 1) / 2 < (*M)) || ((*M) < 0)) {
        fprintf(out, "bad number of edges\n");
        //fclose(in);
        //fclose(out);
        exit(0);
    }
}

void free_adj_table(adj_table* list, int N) {
    for (int i = 1; i <= N; i++) free(list[i].arr);
    free(list);
}

int cmp(const void* a, const void* b) {
    return ((pair*)a)->weight - ((pair*)b)->weight;
}

void add_vertex(adj_table* list, int v, int u, int w) {
    list[v].arr[(list[v].actual_len)].dest = u;
    list[v].arr[(list[v].actual_len)++].weight = w;
    list[u].arr[(list[u].actual_len)].dest = v;
    list[u].arr[(list[u].actual_len)++].weight = w;
}

adj_table* create_adj_table(FILE* in, FILE* out, int N, int M) {
    long checkpoint = ftell(in);
    int* degree = (int*)calloc(N+1, sizeof(int));
    for (int i = 0; i < M; i++) { //analyzing len of adjacency list for each vertex
        int v, u;
        long long int w;
        if (3 != fscanf(in, "%d %d %lld", &v, &u, &w)) {
            fprintf(out, "bad number of lines\n");
            free(degree);
            //fclose(in);
            //fclose(out);
            exit(0);
        }
        if (!((1 <= v)&&(v <= N)&&(1 <= u)&&(u <= N))){
            fprintf(out, "bad vertex\n");
            free(degree);
            //fclose(in);
            //fclose(out);
            exit(0);
        }
        if (!((w>=0)&&(w<=INT_MAX))) {
            fprintf(out, "bad length\n");
            free(degree);
            //fclose(in);
            //fclose(out);
            exit(0);
        }
        degree[v]++;
        degree[u]++;
    }
    adj_table* list = (adj_table*)calloc(N+1, sizeof(adj_table));
    for (int i = 1; i <= N; i++) { //init adjacency list
        list[i].arr = (pair*)calloc(degree[i]+1, sizeof(pair));
    }
    free(degree);
    fseek(in, checkpoint, SEEK_SET);
    return list;
}       

adj_table* read_graph(FILE* in, FILE* out, int N, int M) {
    adj_table* list = create_adj_table(in, out, N, M);
    for (int i = 0; i < M; i++) {
        int v = 0, u = 0, w = 0;
        if (3 != fscanf(in, "%d %d %d", &v, &u, &w)) exit(-1);
        add_vertex(list, v, u, w);
    }
    for (int i = 1; i <= N; i++) { //sorting adjacency lists
        qsort(list[i].arr, list[i].actual_len, sizeof(pair), cmp);
    }
    return list;
}

bool MinimumSpanningTree(adj_table* graph, pair* ans, int N) { //returns true if suceeded
    bool* selected = (bool*)calloc(N+1, sizeof(bool));
    int N_cur = 0;
    selected[1] = true; //start from first =)
    while (N_cur != N - 1) {
        int min = INT_MAX, x = 0, y = 0;
        for (int i = 1; i <= N; i++) {
            if (selected[i]) {
                for (int j = graph[i].checked_len; j < graph[i].actual_len; j++) {
                    if (!selected[graph[i].arr[j].dest]) {
                        if (min >= graph[i].arr[j].weight) {
                            min = graph[i].arr[j].weight;
                            x = i;
                            y = graph[i].arr[j].dest;
                        }
                        break;
                    } else graph[i].checked_len = j;
                }
            }
        }
        if ((x == 0)&&(y==0)) {
            free(selected);
            return false;
        }
        ans[N_cur].dest = x;
        ans[N_cur].weight = y;
        selected[y] = true;
        N_cur++;
    }
    free(selected);
    return true;
}

int main () {
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
    int N, M;
    get_N_M(in, out, &N, &M);
    adj_table* graph = read_graph(in, out, N, M);
    pair* ans = (pair*)calloc(N, sizeof(pair));
    if (MinimumSpanningTree(graph, ans, N)) {
        for (int i = 0; i < N - 1; i++) {
            fprintf(out, "%d %d\n", ans[i].dest, ans[i].weight);
        }
    } else fprintf(out, "no spanning tree\n");
    free_adj_table(graph, N);
    free(ans);
    fclose(in);
    fclose(out);
    return 0;
}
