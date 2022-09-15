#include <stdio.h>
#include <limits.h>
#include <stdbool.h>
#include <stdlib.h>

void get_params(int* N, int* M, int* start, int* end) {
    if (4 != scanf("%d\n%d %d\n%d", N, start, end, M)) {
        printf("bad number of lines");
        exit(0);
    }
    if (((*N)<0) || ((*N)>5000)) {
        printf("bad number of vertices");
        exit(0);
    }
    if (((*M)<0) || ((*M)>(*N)*((*N)+1)/2)) {
        printf("bad number of edges");
        exit(0);
    }
    if ((*start)<=0 || (*start)>(*N) || (*end)<=0 || (*end)>(*N)) {
        printf("bad vertex");
        exit(0);
    }
}

void init(int** ways, int** visited, long long** weight, int** prev, int** matrix, int N) {
    (*matrix) = (int*)malloc(N*N*sizeof(int));
    (*weight) = (long long*)malloc(N*sizeof(long long));
    (*prev) = (int*)calloc(N, sizeof(int));
    (*visited) = (int*)malloc(N*sizeof(int));
    (*ways) = (int*)calloc(N, sizeof(int));
    for(int i = 0; i < N; i++){
        for(int j = 0; j < N; j++) (*matrix)[i*N+j] = -1;
        (*weight)[i] = LONG_MAX;
        (*visited)[i] = false;
    }
}

void read_square_matrix(int* matrix, int N, int M) {
    for(int i = 0; i < M; i++){
        long long x, y, w;
        if (3 != scanf("%lld %lld %lld", &x, &y, &w)) {
            printf("bad number of lines");
            free(matrix);
            exit(0);
        }
        if (x<=0 || x>N || y<=0 || y>N) {
            printf("bad vertex");
            free(matrix);
            exit(0);
        }
        if ((w < 0) || (w > INT_MAX)){
            printf("bad length");
            free(matrix);
            exit(0);
        }
        matrix[(x-1)*N+y-1] = w;
        matrix[(y-1)*N+x-1] = w;
    }
}

void Dijkstra(int* ways, int* visited, int* prev, int* matrix, long long* weight, int N, int start) {
    weight[start-1] = 0;
    for(int i = 0; i < N; i++){
        long long min = LONG_MAX, index_min = -1;
        for (int j = 0; j < N; j++){ //searching for min weight
            if ((!visited[j]) && (weight[j] < min)) {
                min = weight[j];
                index_min = j;
            }
        }
        if (index_min == -1) break;
        visited[index_min] = true;
        if (weight[index_min] != -1) {
            for (int j = 0; j < N; j++) {
            if ((!visited[j]) && (matrix[index_min*N+j] != -1) && (weight[index_min] + matrix[index_min*N+j] <= weight[j])) {
                if (weight[index_min] + matrix[index_min*N+j] == weight[j]) ways[j]++;
                else ways[j] = 1;
                weight[j] = weight[index_min] + matrix[index_min*N+j];
                prev[j] = index_min;
                }
            }
        }
    }
}

void print_ans(int* ways, int* visited, int* prev, long long* weight, int N, int start, int end) {
    for (int i = 0; i < N; i++) {
        if (weight[i] == LONG_MAX) printf("oo ");
        else if (weight[i] > INT_MAX) {
            printf("INT_MAX+ ");
        }
        else printf("%lld ", weight[i]);
    }
    printf("\n");
    if (!visited[end-1]) printf("no path\n");
    else if ((weight[end-1] > INT_MAX)&&(ways[end-1] >= 2)) printf("overflow\n");
    else for(int i = end-1;; i = prev[i]) { //print way of ans
        printf("%d ", i+1);
        if (i == start-1) break;
    }
    
}

int main() {
    int N, M, start, end;
    get_params(&N, &M, &start, &end);
    long long *weight;
    int *prev, *ways, *visited, *matrix;
    init(&ways, &visited, &weight, &prev, &matrix, N);
    read_square_matrix(matrix, N, M);
    Dijkstra(ways, visited, prev, matrix, weight, N, start);
    print_ans(ways, visited, prev, weight, N, start, end);
    free(matrix); free(prev); free(ways); free(visited); free(weight);
    return 0;
}
