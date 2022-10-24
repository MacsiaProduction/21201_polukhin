#include <stack>
#include <string>
#include <iostream>

//default function interface
struct default_function {
    public:
    virtual void work(std::stack<long long>&) = 0;
    virtual std::string get_name() = 0;
};

struct multiply : default_function {
    void work(std::stack<long long>& stack) override {
        int first = stack.top(); stack.pop();
        int second = stack.top(); stack.pop();
        stack.push(second*first);        
    }
    std::string get_name() override {
        return "*";
    }
};

struct divide : default_function {
    void work(std::stack<long long>& stack) override {
        int first = stack.top(); stack.pop();
        int second = stack.top(); stack.pop();
        stack.push(second/first);      
    }
    std::string get_name() override {
        return "/";
    }
};

struct plus : default_function {
    void work(std::stack<long long>& stack) override {
        int first = stack.top(); stack.pop();
        stack.top() = first + stack.top();     
    }
    std::string get_name() override {
        return "+";
    }
};

struct minus : default_function {
    void work(std::stack<long long>& stack) override {
        int first = stack.top(); stack.pop();
        stack.top() = first - stack.top();     
    }
    std::string get_name() override {
        return "-";
    }
};

struct mod : default_function {
    void work(std::stack<long long>& stack) override {
        int first = stack.top(); stack.pop();
        stack.top() = first % stack.top();    
    }
    std::string get_name() override {
        return "mod";
    }
};

struct dup : default_function {
    void work(std::stack<long long>& stack) override {
        int tmp = stack.top();
        stack.push(tmp);   
    }
    std::string get_name() override {
        return "dup";
    }
};

struct drop : default_function {
    void work(std::stack<long long>& stack) override {
        stack.pop();   
    }
    std::string get_name() override {
        return "drop";
    }
};

struct print : default_function {
    void work(std::stack<long long>& stack) override {
        std::cout<<int(stack.top())<<" ";
        stack.pop();   
    }
    std::string get_name() override {
        return ".";
    }
};

struct swap : default_function {
    void work(std::stack<long long>& stack) override {
        int tmp = stack.top();
        stack.pop();
        int tmp2 = stack.top();
        stack.push(tmp);
        stack.push(tmp2);  
    }
    std::string get_name() override {
        return "swap";
    }
};

struct rot : default_function {
    void work(std::stack<long long>& stack) override {
        int tmp1 = stack.top();
        stack.pop();
        int tmp2 = stack.top();
        stack.pop();
        int tmp3 = stack.top();
        stack.pop();
        stack.push(tmp1);
        stack.push(tmp3);
        stack.push(tmp2); 
    }
    std::string get_name() override {
        return "rot";
    }
};

struct over : default_function {
    void work(std::stack<long long>& stack) override {
        int tmp1 = stack.top();
        stack.pop();
        int tmp2 = stack.top();
        stack.push(tmp2);
        stack.push(tmp1);
        stack.push(tmp2);
    }
    std::string get_name() override {
        return "over";
    }
};

struct emit : default_function {
    void work(std::stack<long long>& stack) override {
        std::cout<<(char)stack.top();
        stack.pop();
    }
    std::string get_name() override {
        return "emit";
    }
};

struct cr : default_function {
    void work(std::stack<long long>& stack) override {
        std::cout<<std::endl;
    }
    std::string get_name() override {
        return "cr";
    }
};

struct less : default_function {
    void work(std::stack<long long>& stack) override {
        int first = stack.top(); stack.pop();
        stack.top() = (first < stack.top());
    }
    std::string get_name() override {
        return "<";
    }
};

struct greater : default_function {
    void work(std::stack<long long>& stack) override {
        int first = stack.top(); stack.pop();
        stack.top() = (first > stack.top());
    }
    std::string get_name() override {
        return ">";
    }
};

struct equal : default_function {
    void work(std::stack<long long>& stack) override {
        int first = stack.top(); stack.pop();
        stack.top() = (first == stack.top());
    }
    std::string get_name() override {
        return "=";
    }
};

struct reference : default_function {
    void work(std::stack<long long>& stack) override {
        long long tmp = stack.top();
        long long* pointer = reinterpret_cast<long long*>(tmp);
        stack.top() = (*pointer);
    }
    std::string get_name() override {
        return "@";
    }
};

struct dereference : default_function {
    void work(std::stack<long long>& stack) override {
        long long* tmp = reinterpret_cast<long long*>(stack.top());
        stack.pop();    
        (*tmp) = stack.top();
        stack.pop();
    }
    std::string get_name() override {
        return "!";
    }
};