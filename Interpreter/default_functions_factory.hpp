#ifndef default_functions_factory_HPP
#define default_functions_factory_HPP
#include <iostream>
#include <map>
#include <stack>
#include <memory>
#include "default_function_interface.hpp"
class default_functions_factory{
public:
    //get factory instance
    static default_functions_factory* get_factory_instance() {
        static default_functions_factory instance;
        return &instance;
    }
    //checs if function with 'name' is in factory
    bool in_list(std::string& name) {
        return (default_functions.find(name) != default_functions.end());
    };
    //runs function by name
    void call_by_name(std::string& name, std::stack<long long>& stack, std::stringstream& in, std::stringstream& out) {
        default_functions.at(name)->work(stack, in, out);
    }
    //adding additional default_function to factory
    void add_default_function(std::shared_ptr<default_function> func) {
        default_functions.insert({func->get_name(), func});
    }
private:
    default_functions_factory() = default;
    default_functions_factory(default_functions_factory&) = delete;
    std::map<std::string, std::shared_ptr<default_function>> default_functions;
};
#endif