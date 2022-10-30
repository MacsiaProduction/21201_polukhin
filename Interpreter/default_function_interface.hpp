#ifndef default_function_interface
#define default_function_interface
#include <stack>
#include <sstream>
//default function interface
struct default_function {
    virtual void work(std::stack<long long>& stack,\
    std::stringstream& in, std::stringstream& out) = 0;
    virtual std::string get_name() = 0;
};
#endif