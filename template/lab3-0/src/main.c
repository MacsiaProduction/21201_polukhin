#include <stdio.h>
#include <stdlib.h>

void BadInput(){
    printf("bad input");
    exit(0);
}

void swap(int *p1, int *p2){
    //swaps two integer values.
    int tmp = *p1;
    *p1 = *p2;
    *p2 = tmp;
}

void heapify(int *arr, int n, int i){
    //recusive building of a heap
    int largest = i;   
    int l = 2*i + 1;
    int r = 2*i + 2;
    if (l < n && arr[l] > arr[largest])
        largest = l;
    if (r < n && arr[r] > arr[largest])
        largest = r;
    if (largest != i){
        swap(&arr[i], &arr[largest]);
        heapify(arr, n, largest);
    }
}

void heapSort(int *arr, int n){
    for (int i = n / 2 - 1; i >= 0; i--)
        heapify(arr, n, i);
    for (int i = n-1; i >= 0; i--){
        swap(&arr[0], &arr[i]);
        heapify(arr, i, 0);
    }
}

int main(){
    int n;
    if(1 != scanf("%d",&n)) BadInput();
    int *arr = malloc(n * sizeof(int));
    for(int i = 0; i < n; i++) 
        if(1 != scanf("%d",&arr[i])) BadInput();
    heapSort(arr, n);
    for (int i = 0; i < n; i++)
        printf("%d ",arr[i]);
    printf("\n");
    free(arr);
}
