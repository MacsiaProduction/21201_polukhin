#ifndef default_functions_HPP
#define default_functions_HPP

#include <sstream>
#include "../res/out.hpp"
#include "../res/log.hpp"
#include "default_function_interface.hpp"
#include "auto_registration.hpp"

struct multiply : default_function
{
    void work(std::stack<long long> &stack, std::stringstream &in, std::stringstream &out) override;
    std::string get_name() override;
};

struct divide : default_function
{
    void work(std::stack<long long> &stack, std::stringstream &in, std::stringstream &out) override;
    std::string get_name() override;
};

struct plus : default_function
{
    void work(std::stack<long long> &stack, std::stringstream &in, std::stringstream &out) override;
    std::string get_name() override;
};

struct minus : default_function
{
    void work(std::stack<long long> &stack, std::stringstream &in, std::stringstream &out) override;
    std::string get_name() override;
};

struct mod : default_function
{
    void work(std::stack<long long> &stack, std::stringstream &in, std::stringstream &out) override;
    std::string get_name() override;
};

struct copy : default_function
{
    void work(std::stack<long long> &stack, std::stringstream &in, std::stringstream &out) override;
    std::string get_name() override;
};

struct drop : default_function
{
    void work(std::stack<long long> &stack, std::stringstream &in, std::stringstream &out) override;
    std::string get_name() override;
};

struct print : default_function
{
    void work(std::stack<long long> &stack, std::stringstream &in, std::stringstream &out) override;
    std::string get_name() override;
};

struct swap : default_function
{
    void work(std::stack<long long> &stack, std::stringstream &in, std::stringstream &out) override;
    std::string get_name() override;
};

// works in real Forth way))
struct rot : default_function
{
    void work(std::stack<long long> &stack, std::stringstream &in, std::stringstream &out) override;
    std::string get_name() override;
};

struct over : default_function
{
    void work(std::stack<long long> &stack, std::stringstream &in, std::stringstream &out) override;
    std::string get_name() override;
};

struct emit : default_function
{
    void work(std::stack<long long> &stack, std::stringstream &in, std::stringstream &out) override;
    std::string get_name() override;
};

struct cr : default_function
{
    void work(std::stack<long long> &stack, std::stringstream &in, std::stringstream &out) override;
    std::string get_name() override;
};

struct less : default_function
{
    void work(std::stack<long long> &stack, std::stringstream &in, std::stringstream &out) override;
    std::string get_name() override;
};

struct greater : default_function
{
    void work(std::stack<long long> &stack, std::stringstream &in, std::stringstream &out) override;
    std::string get_name() override;
};

struct equal : default_function
{
    void work(std::stack<long long> &stack, std::stringstream &in, std::stringstream &out) override;
    std::string get_name() override;
};

struct reference : default_function
{
    void work(std::stack<long long> &stack, std::stringstream &in, std::stringstream &out) override;
    std::string get_name() override;
};

struct dereference : default_function
{
    void work(std::stack<long long> &stack, std::stringstream &in, std::stringstream &out) override;
    std::string get_name() override;
};

struct print_string : default_function
{
    void work(std::stack<long long> &stack, std::stringstream &in, std::stringstream &out) override;
    std::string get_name() override;
};

struct conditional_operator : default_function
{
    void work(std::stack<long long> &stack, std::stringstream &in, std::stringstream &out) override;
    std::string get_name() override;
};

struct loop : default_function
{
    void work(std::stack<long long> &stack,
              std::stringstream &in, std::stringstream &out) override;
    std::string get_name() override;
};

auto_registration::register_function<multiply> _multiply;
auto_registration::register_function<divide> _divide;
auto_registration::register_function<plus> _plus;
auto_registration::register_function<minus> _minus;
auto_registration::register_function<mod> _mod;
auto_registration::register_function<copy> _dup;
auto_registration::register_function<drop> _drop;
auto_registration::register_function<print> _print;
auto_registration::register_function<swap> _swap;
auto_registration::register_function<rot> _rot;
auto_registration::register_function<over> _over;
auto_registration::register_function<emit> _emit;
auto_registration::register_function<cr> _cr;
auto_registration::register_function<less> _less;
auto_registration::register_function<greater> _greater;
auto_registration::register_function<equal> _equal;
auto_registration::register_function<reference> _reference;
auto_registration::register_function<dereference> _dereference;
auto_registration::register_function<print_string> _print_string;
auto_registration::register_function<conditional_operator> _conditional_operator;
auto_registration::register_function<loop> _loop;

#endif