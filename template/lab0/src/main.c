#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

void BadInput(){
    printf("bad input format");
    exit(0);
}

int ctoi(char a) {
    int tmp = a - '0';
    if (tmp > 9) {
        tmp = tolower(a) - 'a' + 10;
    }
    return tmp;
}

char itoc(int a) {
    if(a<10){
        return a + '0';
    }
    if(a<16){
        return a + 'A' - 10;   
    }
    printf("bad input itoc");
    return 0;
}

int check(char *s1, int b1, unsigned int *point){
    for (unsigned int i = 0; i < strlen(s1); i++) {
        if (s1[i] == '.') {
            if (*point != strlen(s1)){
                printf("bad input");
                return 0;
            }
            *point = i;
            continue;
        }
        if ((ctoi(s1[i]) >= b1) || (ctoi(s1[i])<0)) {
            printf("bad input");
            return 0;
        }
    }
    return 1;
}

void reverse(char *s){
    int length = strlen(s);
    for (int i = 0, j = length - 1; i < j; i++, j--){
        int c = s[i];
        s[i] = s[j];
        s[j] = c;
    }
}

void strcat_c (char *str, char c){
    char tmp[2] = { c , 0 };
    strcat(str, tmp);
}
  
int main()
{
    unsigned int b1, b2;
    char s1[100000] = "", s2[100000] = "";
    if (3 != scanf("%u%u%100s", &b1, &b2, s1)) {
        BadInput();
    }
    unsigned int point = strlen(s1);
    if (!check(s1, b1, &point)){ 
        return 0;
    }
    //b1 -> 10
    unsigned long long int_part_sum = 0, numerator_sum = 0, denumerator_sum = 1;
    for (unsigned int i = 0; i < point; i++) {
        int_part_sum = ctoi(s1[i]) + int_part_sum * b1;
    }
    for (unsigned int i = point+1; i <= strlen(s1)-1; i++) {
        numerator_sum = numerator_sum * b1 + ctoi(s1[i]);
        denumerator_sum*=b1;
    }
    if (((int_part_sum+numerator_sum)==0)&&((s1[0] != '0')||(strlen(s1)!=1))){
        printf("bad input");
        return 0;
    }
    //10 -> b2
    while(int_part_sum>=b2){
        char digit = itoc(int_part_sum%b2);
        int_part_sum/=b2;
        strcat_c(s2, digit);
    }
    strcat_c(s2, itoc(int_part_sum));
    reverse(s2);
    if(numerator_sum!=0){
        char digit = '.';
        strcat_c(s2, digit);
    }
    int counter = 0;
    while(numerator_sum!=0){
        numerator_sum*=b2;
        char digit = itoc(numerator_sum / denumerator_sum);
        numerator_sum -= denumerator_sum * (numerator_sum / denumerator_sum);
        strcat_c(s2, digit);
        if (++counter==12) {
            break; 
        }
    }
    printf("%s", s2);
    return 0;
}
