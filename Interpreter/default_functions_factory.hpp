#include <iostream>
#include <map>
#include <stack>
#include "default_functions.hpp"

class default_functions_factory{
public:
    default_functions_factory() {
        init_factory();
    }
    ~default_functions_factory() {
        for(auto& item : default_functions) {
            delete item.second;
        }
    }
    bool in_list(std::string& name) {
        return (default_functions.find(name) != default_functions.end());
    };
    void call_by_name(std::string& name, std::stack<long long>& stack) {
        default_functions.at(name)->work(stack);
    }
protected:
    //adds all default functions
    void init_factory() {
        add_default_function(new multiply);
        add_default_function(new divide);
        add_default_function(new plus);
        add_default_function(new minus);
        add_default_function(new mod);
        add_default_function(new dup);
        add_default_function(new drop);
        add_default_function(new print);
        add_default_function(new swap);
        add_default_function(new rot);
        add_default_function(new over);
        add_default_function(new emit);
        add_default_function(new cr);
        add_default_function(new less);
        add_default_function(new greater);
        add_default_function(new equal);
        add_default_function(new reference);
        add_default_function(new dereference);
    }
    void add_default_function(default_function* func) {
        default_functions.insert(std::map<std::string, default_function*>::value_type\
        (func->get_name(), func));
    }
private:
    std::map<std::string, default_function*> default_functions;
};