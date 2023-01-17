#ifndef default_function_interface_HPP
#define default_function_interface_HPP
#include "variables_list.hpp"
#include <stack>
#include <sstream>

// default function interface
struct default_function
{
    virtual ~default_function() = default;
    // function's body
    virtual void work(std::stack<long long> &stack, variables_list &vars,
                      std::stringstream &in, std::stringstream &out) = 0;
    // fucntion's name
    virtual std::string get_name() = 0;
};
#endif