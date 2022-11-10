#ifndef default_function_interface
#define default_function_interface
#include <stack>
#include <sstream>
//default function interface
struct default_function {
    // function body
    virtual void work(std::stack<long long>& stack,\
    std::stringstream& in, std::stringstream& out) = 0;
    //fucntion name
    virtual std::string get_name() = 0;
};
#endif