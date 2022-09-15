#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#define BUFFER_SIZE 1024
typedef unsigned char uchar;

void BadInput(){
    printf("bad input");
    exit(0);
}

void make_power_table(long long *table, int n){
    //making table of (powers of 3) up to n
    long long tmp = 1;
    for(int i = 0; i <= n; i++){
        table[i] = tmp;
        tmp *= 3;
    }
}

long long get_hash(uchar *str, int len, long long *power_table){
    //function that returns hash value of string
    long long tmp = 0;
    for(int i = 0; i < len;i++){
        tmp+=(str[i]%3)*power_table[i];
    }
    return tmp;
}

void buffer_shift(uchar *buffer, long long *buf_len, long long ex_len){
    //moving last ex_len elements to the beginning and reading others
    memcpy(buffer, &buffer[BUFFER_SIZE-ex_len], ex_len);
    (*buf_len) += fread(&buffer[ex_len], sizeof(char), BUFFER_SIZE-ex_len, stdin);
}

int main(){
    long long* power_table = (long long*)malloc((BUFFER_SIZE+1) * sizeof(long long int));
    make_power_table(power_table, 17);
    uchar example[19];
    uchar *buffer = (uchar*)malloc(BUFFER_SIZE);
    if (fgets((char*)example, 18, stdin) == NULL) BadInput();
    long long ex_len = strlen((char*)example)-1, rel_number = 0,
    ex_hash = get_hash(example, ex_len, power_table),
    buf_len = fread(buffer, sizeof(uchar), BUFFER_SIZE, stdin),
    cur_buf_hash = get_hash(buffer, ex_len, power_table);
    printf("%lld ", ex_hash);
    for(long long i = ex_len-1; i<buf_len; i++){
        if(cur_buf_hash == ex_hash){
            //if hash's are equial
            for(int j = 0; j<ex_len; j++){
                printf("%lld ", (i-ex_len+2)+j);
                if(buffer[rel_number+j] != example[j]) break;
            }
        }
        if(i+1 == buf_len){ // if it's last element trying to get new portion
            buffer_shift(buffer, &buf_len, ex_len);
            rel_number = 0;
            if(i+1 == buf_len) break; // if it's truly last, stoping
        }
        cur_buf_hash = (cur_buf_hash - buffer[rel_number]%3) / 3 + (buffer[rel_number+ex_len]%3)*power_table[ex_len-1];
        rel_number++;
    }
    free(power_table);
    free(buffer);
    return 0;
}
