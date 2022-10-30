#ifndef default_functions_factory_HPP
#define default_functions_factory_HPP
#include <iostream>
#include <map>
#include <stack>
#include <memory>
#include "default_function_interface.hpp"
class default_functions_factory{
public:
    static default_functions_factory* get_factory_instance() {
        static default_functions_factory instance;
        return &instance;
    }   
    bool in_list(std::string& name) {
        return (default_functions.find(name) != default_functions.end());
    };
    void call_by_name(std::string& name, std::stack<long long>& stack, std::stringstream& in, std::stringstream& out) {
        default_functions.at(name)->work(stack, in, out);
    }
    void add_default_function(std::shared_ptr<default_function> func) {
        default_functions.insert({func->get_name(), func});
    }
private:
    default_functions_factory() = default;
    std::map<std::string, std::shared_ptr<default_function>> default_functions;
};
#endif