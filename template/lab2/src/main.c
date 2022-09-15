#include <stdio.h>
#include <stdbool.h>
#include <string.h>
#include <stdlib.h>

void swap(char *array, int p1, int p2){
    //swaps to integer's values in array.
    int tmp = array[p1];
    array[p1] = array[p2];
    array[p2] = tmp;
}

void reverse(char *s, int l, int r){
    //inplace reverse of the array from l to r element.
    for (int i = l, j = r; i < j; i++, j--){
        int c = s[i];
        s[i] = s[j];
        s[j] = c;
    }
}

void BadInput(){
    printf("bad input");
    exit(0);
}

bool is_permutation(char *input){
    //checking if the string is permutations of "0123456789"
    int numbers[10] = {0};
    for (unsigned int i = 0; i < strlen(input); i++){
        if (('0' <= input[i]) && (input[i] <= '9')){
            numbers[input[i] - '0']++;
        }
        else{
            return false;
        }
    }
    for (int i = 0; i <= 9; i++){
        if(numbers[i]>1){
            return false;
        }
    }
    return true;
}

bool print_next_permutation(char *input){
    /*function that inplace generate next permutation
    in lexicographic order. Returns false if fails, true if succeeds */
    int len = strlen(input);
    int first = -1;
    for(int i = len-2;i>=0;i--){
        if(input[i]<input[i+1]){
            first=i;
            break;
        }
    }
    if(first==-1) return false;
    int second = -1;
    for(int i = len-1;i>first;i--){
        if(input[i]>input[first]){
            second=i;
            break;
        }
    }
    if(second==-1) return false;
    swap(input, first, second);
    reverse(input, first+1, len-1);
    printf("%s\n", input);
    return true;
}

int main(){
    char input[21];
    int n;
    if (NULL == fgets(input, 20, stdin)){
        BadInput();
    }
    input[strlen(input)-1] = input[strlen(input)];
    if ((1 != scanf("%d", &n)) || (!is_permutation(input))){
        BadInput();
    }
    for(int i = 1;i<=n;i++){
        if (!print_next_permutation(input)) return 0;
    }
    return 0;
}
