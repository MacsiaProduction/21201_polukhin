#include <iostream>
#include <map>
#include <stack>
#include <functional>
class function_list{
public:
    //adds all default funcs 
    function_list() {
        // * / + - % 
        add_default_func("*", [](std::stack<int>& stack){
            int first = stack.top(); stack.pop();
            int second = stack.top(); stack.pop();
            stack.push(second*first);
        });
        add_default_func("/", [](std::stack<int>& stack){
            int first = stack.top(); stack.pop();
            int second = stack.top(); stack.pop();
            stack.push(second/first);
        });
        add_default_func("+", [](std::stack<int>& stack){
            int first = stack.top(); stack.pop();
            stack.top() = first + stack.top();
        });
        add_default_func("-", [](std::stack<int>& stack){
            int first = stack.top(); stack.pop();
            stack.top() = first - stack.top();
        });
        add_default_func("mod", [](std::stack<int>& stack){
            int first = stack.top(); stack.pop();
            stack.top() = first % stack.top();
        });
        //dup drop . swap rot over emit cr
        add_default_func("dup", [](std::stack<int>& stack){
            int tmp = stack.top();
            stack.push(tmp);
        });
        add_default_func("drop", [](std::stack<int>& stack){
            stack.pop();
        });
        add_default_func(".", [](std::stack<int>& stack){
            std::cout<<stack.top()<<" ";
            stack.pop();
        });
        add_default_func("swap", [](std::stack<int>& stack){
            int tmp = stack.top();
            stack.pop();
            int tmp2 = stack.top();
            stack.push(tmp);
            stack.push(tmp2);
        });
        add_default_func("rot", [](std::stack<int>& stack){
            int tmp1 = stack.top();
            stack.pop();
            int tmp2 = stack.top();
            stack.pop();
            int tmp3 = stack.top();
            stack.pop();
            stack.push(tmp1);
            stack.push(tmp3);
            stack.push(tmp2);
        });
        add_default_func("over", [](std::stack<int>& stack){
            int tmp1 = stack.top();
            stack.pop();
            int tmp2 = stack.top();
            stack.push(tmp2);
            stack.push(tmp1);
            stack.push(tmp2);
        });
        add_default_func("emit", [](std::stack<int>& stack){
            std::cout<<(char)stack.top();
            stack.pop();
        });
        add_default_func("cr", [](std::stack<int>& stack){
            std::cout<<std::endl;
        });
        // < > =
        add_default_func("<", [](std::stack<int>& stack){
            int first = stack.top(); stack.pop();
            stack.top() = (first < stack.top());
        });
        add_default_func(">", [](std::stack<int>& stack){
            int first = stack.top(); stack.pop();
            stack.top() = (first > stack.top());
        });
        add_default_func("=", [](std::stack<int>& stack){
            int first = stack.top(); stack.pop();
            stack.top() = (first == stack.top());
        });
    }
    void add_default_func(std::string name, std::function<void(std::stack<int>&)> body) {
        functions.insert(std::map<std::string,\
        std::function<void(std::stack<int>&)>>::value_type(name, body));
    };
protected:
    std::map<std::string, std::function<void(std::stack<int>&)>> functions;
};