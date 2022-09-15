#include <stdio.h>
#include <stdlib.h>
#include <stdio.h>
#include <time.h>

void BadInput(){
    printf("bad input");
    exit(0);
}

void swap(int *array, int p1, int p2){
    //swaps two integer values in array.
    int tmp = array[p1];
    array[p1] = array[p2];
    array[p2] = tmp;
}

void quick_sort(int *array, int l, int r){
	//recursive inplace quick_sort.
    if (r-l < 1) return;
    int reference = array[(r+l)/2];
    swap(array, (r+l)/2, r);
    int left = l;
    for(int j = l; j < r; j++){
        if (array[j] <= reference){
            swap(array, left++, j);
        }
    }
    swap(array, left, r);
    int middle = left;
    for(int j = left; j >= l; j--){
        if (array[j] == reference){
            swap(array, middle--, j);
        }
    }
    quick_sort(array, l, middle);
    quick_sort(array, left+1, r);
}

int main()
{
	int n;
    if ( 1 != scanf("%d", &n)) BadInput();
    int *array = calloc(n, sizeof(int));
    for (int i = 0; i < n; i++){
        if ( 1 != scanf("%d", &array[i])) BadInput();
    }
    quick_sort(array,0,n-1);
    for(int i = 0; i < n; i++){
        printf("%d ", array[i]);
    }
    free(array);
    return 0;
}
