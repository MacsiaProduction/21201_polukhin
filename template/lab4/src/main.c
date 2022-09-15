#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <math.h>
 
#define MAX_LEN 1024
 
typedef struct array_with_curlen{
    int value[MAX_LEN];
    int actual_len;
} cool_array;

void syntax_error(FILE* out) {
    fprintf(out, "syntax error");
    exit(0);
}

void division_by_zero(FILE* out) {
    fprintf(out, "division by zero");
    exit(0);
}

void push(cool_array* input, int val) {
    input->value[++input->actual_len] = val;
}

int is_empty(cool_array* input) {
    return input->actual_len < 0;
}
 
int pop(FILE* out, cool_array* input) {
    if (is_empty(input)) syntax_error(out);
    return input->value[input->actual_len--];
}
 
int peek(FILE* out, cool_array* input) {
    if (is_empty(input)) syntax_error(out);
    return input->value[input->actual_len];
}
 
int is_operation(char ch) {
    return ch == '+' || ch == '-' || ch == '/' || ch == '*';
}
 
int check_sequence(FILE* out, char* s, int str_len) { //returns syntactically correct len of a sequence, exits if bad sequence
    int real_str_len = 0;
    for (int i = 0; i < str_len; i++) {
        if (isspace(s[i]) && (i == str_len-1)) continue;
        else if (!is_operation(s[i]) && !isdigit(s[i]) && (s[i] != '(') && (s[i] != ')')) syntax_error(out);
        else real_str_len++;
    }
    return real_str_len;
}

void use_operation(FILE* out, cool_array* digits, char op) {
    int digit2 = pop(out, digits);
    int digit1 = pop(out, digits);
    if (op == '+')  push(digits, digit1 + digit2);
    else if (op == '-') push(digits, digit1 - digit2);
    else if (op == '*') push(digits, digit1 * digit2);
    else if (op == '/') {
        if (!digit2) division_by_zero(out);
        push(digits, digit1 / digit2);
    }
    else syntax_error(out);
}
 
int get_number(char* s, int *actual_len) {
    char *hmm;
    int decimal = strtol(s + *actual_len, &hmm, 10);
    if (decimal) *actual_len += floor(log10(decimal));
    return decimal;
}

int compute(FILE* out, char* s, int str_len) {
    cool_array digits = { .actual_len = -1 };
    cool_array ops = { .actual_len = -1 };
    char op_priority[256] = {0};
    op_priority['+'] = 1;
    op_priority['-'] = 1;
    op_priority['*'] = 2;
    op_priority['/'] = 2;
    for (int i = 0; i < str_len; i++) {
        if (s[i] == '(') push(&ops, '(');
        else if (s[i] == ')') {
            if (!i || s[i - 1] == '(')
                syntax_error(out);
            char tmp;
            while ((tmp = (char)pop(out, &ops)) != '(')
                use_operation(out, &digits, tmp);
        }
        else if (is_operation(s[i])) {
            while (!is_empty(&ops) && op_priority[peek(out, &ops)] >= op_priority[(int)s[i]])
                use_operation(out, &digits, (char)pop(out, &ops));
            push(&ops,(int)s[i]);
        }
        else if (isdigit(s[i])) 
            push(&digits, get_number(s, &i));
    }
    while (!is_empty(&ops))
        use_operation(out, &digits, (char)pop(out, &ops));
    return pop(out, &digits);
}

int main() {
    FILE *in = fopen("in.txt", "rt");
    FILE *out = fopen("out.txt", "wt");
    char input[MAX_LEN];
    int str_len;
    if ((str_len = fread(input, sizeof(char), MAX_LEN, in)) == 0) exit(0);
    int real_len = check_sequence(out, input, str_len);
    int res = compute(out, input, real_len);
    fprintf(out, "%d\n", res);
    fclose(in);
    fclose(out);
    return EXIT_SUCCESS;
}
