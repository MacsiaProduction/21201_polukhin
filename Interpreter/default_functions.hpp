#ifndef default_functions_HPP
#define default_functions_HPP

#include <sstream>
#include "../res/out.hpp"
#include "../res/log.hpp"
#include "default_function_interface.hpp"
#include "auto_registration.hpp"

struct multiply : default_function
{
    void work(std::stack<long long> &stack, variables_list& vars, std::stringstream &in, std::stringstream &out) override;
    std::string get_name() override;
};

struct divide : default_function
{
    void work(std::stack<long long> &stack, variables_list& vars, std::stringstream &in, std::stringstream &out) override;
    std::string get_name() override;
};

struct plus : default_function
{
    void work(std::stack<long long> &stack, variables_list& vars, std::stringstream &in, std::stringstream &out) override;
    std::string get_name() override;
};

struct minus : default_function
{
    void work(std::stack<long long> &stack, variables_list& vars, std::stringstream &in, std::stringstream &out) override;
    std::string get_name() override;
};

struct mod : default_function
{
    void work(std::stack<long long> &stack, variables_list& vars, std::stringstream &in, std::stringstream &out) override;
    std::string get_name() override;
};

struct copy : default_function
{
    void work(std::stack<long long> &stack, variables_list& vars, std::stringstream &in, std::stringstream &out) override;
    std::string get_name() override;
};

struct drop : default_function
{
    void work(std::stack<long long> &stack, variables_list& vars, std::stringstream &in, std::stringstream &out) override;
    std::string get_name() override;
};

struct print : default_function
{
    void work(std::stack<long long> &stack, variables_list& vars, std::stringstream &in, std::stringstream &out) override;
    std::string get_name() override;
};

struct swap : default_function
{
    void work(std::stack<long long> &stack, variables_list& vars, std::stringstream &in, std::stringstream &out) override;
    std::string get_name() override;
};

// works in real Forth way))
struct rot : default_function
{
    void work(std::stack<long long> &stack, variables_list& vars, std::stringstream &in, std::stringstream &out) override;
    std::string get_name() override;
};

struct over : default_function
{
    void work(std::stack<long long> &stack, variables_list& vars, std::stringstream &in, std::stringstream &out) override;
    std::string get_name() override;
};

struct emit : default_function
{
    void work(std::stack<long long> &stack, variables_list& vars, std::stringstream &in, std::stringstream &out) override;
    std::string get_name() override;
};

struct cr : default_function
{
    void work(std::stack<long long> &stack, variables_list& vars, std::stringstream &in, std::stringstream &out) override;
    std::string get_name() override;
};

struct less : default_function
{
    void work(std::stack<long long> &stack, variables_list& vars, std::stringstream &in, std::stringstream &out) override;
    std::string get_name() override;
};

struct greater : default_function
{
    void work(std::stack<long long> &stack, variables_list& vars, std::stringstream &in, std::stringstream &out) override;
    std::string get_name() override;
};

struct equal : default_function
{
    void work(std::stack<long long> &stack, variables_list& vars, std::stringstream &in, std::stringstream &out) override;
    std::string get_name() override;
};

struct reference : default_function
{
    void work(std::stack<long long> &stack, variables_list& vars, std::stringstream &in, std::stringstream &out) override;
    std::string get_name() override;
};

struct dereference : default_function
{
    void work(std::stack<long long> &stack, variables_list& vars, std::stringstream &in, std::stringstream &out) override;
    std::string get_name() override;
};

struct print_string : default_function
{
    void work(std::stack<long long> &stack, variables_list& vars, std::stringstream &in, std::stringstream &out) override;
    std::string get_name() override;
};

struct conditional_operator : default_function
{
    void work(std::stack<long long> &stack, variables_list& vars, std::stringstream &in, std::stringstream &out) override;
    std::string get_name() override;
};

struct loop : default_function
{
    void work(std::stack<long long> &stack, variables_list& vars,
              std::stringstream &in, std::stringstream &out) override;
    std::string get_name() override;
};

#endif