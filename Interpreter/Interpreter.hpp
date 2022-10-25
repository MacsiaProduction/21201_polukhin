#include <sstream>
#include "default_functions_factory.hpp"
#include "mine_functions_list.hpp"
#include "variables_list.hpp"
#include "../res/log.hpp"
typelog log_level = INFO;
class Interpreter{
public:
    void process_text(std::ifstream& in) {
        LOG(INFO)<<"calling processing text from file";
        std::stringstream input_flow;
        std::string tmp;
        while(in>>tmp) input_flow<<tmp<<" ";
        process_text(input_flow);
    };
    void process_text(std::stringstream& in) {
        std::string tmp;
        while (in>>tmp) {
            process_word(tmp, in);
        }
    }
protected:
    int categorize(std::string name) {
        if (name == "i") return number;
        for (int i = 0; i < name.length(); i++) {
            if(!isdigit(name[i])) break;
            if(isdigit(name[i]) & (i == name.length()-1)) return number;
        }
        if (name == ".\"") return string;
        if (name == "if") return conditional;
        if (name == "do") return loop;
        if (name == ":") return add_mine;
        if (name == "variable") return add_var;
        if (default_functions.in_list(name)) return def_func;
        if (mine_functions.in_list(name)) return use_mine;
        if (variables.in_list(name)) return use_var;
        return error;
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
                case use_mine:
                    use_mine_function(tmp);
                    break;
                default:
                    LOG(ERROR)<<"wrong input, \'"<<tmp<<"\' wasn't recognized";
                    exit(0);
                    break;
            }
    };
    void add_number(std::string str) {
        LOG(INFO)<<"pushing number = \'"<<str<<"\' to stack";
        if (str == "i") {
            if (!loop_flag) LOG(ERROR)<<"using i outside the loop";
            else stack.push(loop_i);
        }
        else stack.push(stoi(str));
    }
    void use_function(std::string name) {
        LOG(INFO)<<"called default_function '"<<name<<"'";
        default_functions.call_by_name(name, stack);
    };
    void print_string(std::stringstream& in) {
        std::string tmp;
        in>>tmp;
        LOG(INFO)<<"called print string = \""<<tmp;
        if (tmp[tmp.length()-1] != '"') LOG(WARN)<<"string didn't end with \"";
        else tmp.pop_back();
        std::cout<<tmp<<" ";
    };
    void conditional_operator(std::stringstream& in) {
        const bool condition = stack.top();
        LOG(INFO)<<"starting conditional operator, condition = "<<condition;
        enum state {then_b = 0, else_b};
        state b = then_b;
        std::string tmp;
        LOG(INFO)<<"starting \"then\" branch of conditional operator";
        while (in>>tmp) {
            if (tmp == "else") {
                b = else_b;
                LOG(INFO)<<"starting \"else\" branch of conditional operator";
            }
            else if (tmp == "then") {
                in>>tmp;
                if (tmp !=";") {
                    LOG(WARN)<<"didn't found ; after conditional operator";
                    process_word(tmp, in);
                }
                break;
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
        LOG(INFO)<<"called loop from "<<start<<" to "<<end;
        std::stringstream body_flow;
        std::string tmp;
        while (in>>tmp) {
            if (tmp == "loop") {
                in>>tmp; //;
                if (tmp != ";") {
                    LOG(WARN)<<"didn't found ; after loop";
                    process_word(tmp, in);
                }
                break;
            }
            body_flow<<tmp<<" ";
        }
        LOG(INFO)<<"loop's body is \""<<body_flow.str()<<"\"";
        loop_flag = true;
        for(int i = start; i < end; i++) {
            loop_i = i;
            std::stringstream body_flow_copy(body_flow.str());
            process_text(body_flow_copy);
        }
        loop_flag = false;
    };
    void add_mine_function(std::stringstream& in) {
        std::string name;
        in>>name;
        std::string body_str;
        std::getline(in, body_str);
        mine_functions.add_mine_func(name, body_str);
        LOG(INFO)<<"added new function "<<name<<" with body "<<body_str;
    };
    void use_mine_function(std::string name) {
        std::stringstream body;
        body << mine_functions.body_by_name(name);
        LOG(INFO)<<"called function "<<name<<" with body "<<body.str();
        process_text(body);
    };
    void add_variable(std::stringstream& in) {
        std::string name;
        in>>name;
        variables.add_variable(name);
        LOG(INFO)<<"new var "<<name<<" now it's = "<<\
        *reinterpret_cast<long long*>(variables.pointer_by_name(name));
    };
    void use_variable(std::string name) {
        stack.push(variables.pointer_by_name(name));
        LOG(INFO)<<"pushed pointer of var "<<name<<" now it's = "<<\
        *reinterpret_cast<long long*>(variables.pointer_by_name(name));
    };
private:
    int loop_i;
    bool loop_flag = false;
    enum category {error = 0, number, def_func, string, conditional,\
    loop, use_mine, use_var, add_var, add_mine};
    std::stack<long long> stack;
    default_functions_factory default_functions;
    mine_functions_list mine_functions;
    variables_list variables;
};