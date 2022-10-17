#include <sstream>
#include <string>
#include "functional_list.hpp"
class Interpreter : private function_list{
public:
    void process_line(std::string line) {
        std::stringstream in(line);
        std::string tmp;
        while (in>>tmp) {
            process_word(tmp,in);
        }
        std::cout<<"ok\n"<<std::endl;
    };
private:
    int categorize(std::string name) {
        for (int i = 0; i < name.length(); i++) {
            if(!isdigit(name[i])) break;
            if(isdigit(name[i]) & i == name.length()-1) return number;
        } 
        if (functions.find(name) != functions.end()) return func;
        if (name == ".\"") return string;
        if (name == "if") return conditional;
        if (name == "do") return loop;
        /*
        if (name == ":") return add_mine;
        if (name == "variable") return var;
        */
        //exception
        exit(0);
    };
    void process_word(std::string tmp, std::stringstream& in) {
        switch(categorize(tmp)) {
                case number:
                    add_number(tmp);
                    break;
                case func:
                    use_function(tmp);
                    break;
                case string:
                    print_string(in);
                    break;
                case conditional:
                    conditional_operator(in);
                    break;
                case loop:
                    do_loop(in);
                    break;
                /*
                case use_mine:
                    use_mine_function(tmp);
                    break;
                case var:
                    add_var();
                    break;
                case alloc:
                    arr_alloc();
                    break;
                case add_mine:
                    add_mine_function();
                    break;
                */
                default:
                    std::cout<<"wrong input"<<std::endl;
                    break;
            }
    };
    void add_number(std::string str) {
        stack.push(stoi(str));
    }
    void use_function(std::string str) {
        functions[str](stack);
    };
    void print_string(std::stringstream& in) {
        std::string tmp;
        in>>tmp;
        if (tmp[tmp.length()-1] != '"') std::cout<<"bad string";
        else tmp.pop_back();
        std::cout<<tmp;
    };
    void conditional_operator(std::stringstream& in) {
        std::string tmp;
        const bool condition = stack.top();
        enum state {then_b = 0, else_b};
        state x = then_b;
        while (in>>tmp) {
            if (tmp == "else") x = else_b;
            else if (tmp == "then") {
                in>>tmp;
                if (tmp==";") break;
                //else exception 
            }
            else {
                switch (x) {
                case then_b:
                    if (condition) process_word(tmp,in);
                    break;
                case else_b:
                    if (!condition) process_word(tmp,in);
                    break;
                }
            }
        }
    };
    void do_loop(std::stringstream& in) {
        int start = stack.top(); stack.pop();
        int end = stack.top(); stack.pop();
        std::stringstream body_flow;
        std::string tmp;
        while (in>>tmp) {
            if (tmp == "loop") {
                in>>tmp; //;
                //if (tmp != ";") exception;
                break;
            }
            body_flow<<tmp<<" ";
        }
        std::string body = body_flow.str();
        for(int i = start; i < end; i++) {
            std::stringstream body_tmp(body);
            while(body_tmp>>tmp) {
                if (tmp == "i") tmp = std::to_string(i);
                process_word(tmp, body_tmp);
            }
        }
    };
    /*
    void add_mine_function();
    void add_var();
    void arr_alloc();
    void use_mine_function(std::string str);
    */
private:
    enum category {number = 1, func, string, conditional, loop, use_mine, var, alloc, add_mine};
    std::stack<int> stack;
};