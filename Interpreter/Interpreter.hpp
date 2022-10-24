#include <sstream>
#include <string>
#include "default_functions_list.hpp"
#include "mine_functions_list.hpp"
#include "variables_list.hpp"
class Interpreter{
public:
    void process_text(std::stringstream& in) {
        std::string tmp;
        while (in>>tmp) {
            process_word(tmp,in);
        }
    }
protected:
    int  categorize(std::string name) {
        if (name == "i") return number;
        for (int i = 0; i < name.length(); i++) {
            if(!isdigit(name[i])) break;
            if(isdigit(name[i]) & i == name.length()-1) return number;
        } 
        if (default_functions.in_list(name)) return def_func;
        if (name == ".\"") return string;
        if (name == "if") return conditional;
        if (name == "do") return loop;
        if (name == ":") return add_mine;
        if (name == "variable") return add_var;
        if (mine_functions.in_list(name)) return use_mine;
        if (variables.in_list(name)) return use_var;
        //exception
        exit(0);
    };
    void process_word(std::string tmp, std::stringstream& in) {
        switch(categorize(tmp)) {
                case number:
                    add_number(tmp);
                    break;
                case def_func:
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
                case add_var:
                    add_variable(in);
                    break;
                case use_var:
                    use_variable(tmp);
                    break;
                case add_mine:
                    add_mine_function(in);
                    break;
                default:
                    std::cout<<"wrong input"<<std::endl;
                    break;
            }
    };
    void add_number(std::string str) {
        if (str == "i") {
            if (!loop_flag) std::cout<<"using i outside the loop";
            else stack.push(loop_i);
        }
        else stack.push(stoi(str));
    }
    void use_function(std::string name) {
        default_functions.call_by_name(name, stack);
    };
    void print_string(std::stringstream& in) {
        std::string tmp;
        in>>tmp;
        if (tmp[tmp.length()-1] != '"') std::cout<<"bad string";
        else tmp.pop_back();
        std::cout<<tmp;
    };
    void conditional_operator(std::stringstream& in) {
        const bool condition = stack.top();
        enum state {then_b = 0, else_b};
        state b = then_b;
        std::string tmp;
        while (in>>tmp) {
            if (tmp == "else") b = else_b;
            else if (tmp == "then") {
                in>>tmp;
                if (tmp==";") break;
                //else exception 
            } else {
                switch (b) {
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
                if (tmp != ";") std::cout<<"missimg ; after loop";
                break;
            }
            body_flow<<tmp<<" ";
        }
        loop_flag = true;
        for(int i = start; i < end; i++) {
            loop_i = i;
            process_text(body_flow);
        }
        loop_flag = false;
    };
    void add_mine_function(std::stringstream& in) {
        std::string name;
        in>>name;
        std::string body_str;
        std::getline(in, body_str);
        mine_functions.add_mine_func(name, body_str);
    };
    void use_mine_function(std::string name) {
        std::stringstream body;
        body << mine_functions.body_by_name(name);
        process_text(body);
    };
    void add_variable(std::stringstream& in) {
        std::string name;
        in>>name;
        variables.add_variable(name);
    };
    void use_variable(std::string name) {
        stack.push(variables.pointer_by_name(name));
    };
private:
    int loop_i;
    bool loop_flag = false;
    enum category {number = 1, def_func, string, conditional, loop,\
    use_mine, use_var, add_var, add_mine};
    std::stack<long long> stack;
    default_functions_factory default_functions;
    mine_functions_list mine_functions;
    variables_list variables;
};