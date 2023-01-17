#ifndef default_functions_factory_HPP
#define default_functions_factory_HPP

#include <map>
#include <memory>
#include "default_function_interface.hpp"

template <class T>
class factory
{
public:
    // get factory instance
    static factory *get_factory_instance()
    {
        static factory instance;
        return &instance;
    }
    // checs if function with 'name' is in factory
    bool in_list(std::string &name)
    {
        return (default_functions.count(name) != 0);
    };
    // returns object by name
    std::unique_ptr<T> &get_by_name(std::string &name)
    {
        return default_functions.at(name);
    }
    // adding additional default_function to factory
    void add_object(std::unique_ptr<T> &&func)
    {
        default_functions.insert({func->get_name(), std::move(func)});
    }

private:
    factory() = default;
    factory(factory &) = delete;
    std::map<std::string, std::unique_ptr<T>> default_functions;
};
#endif