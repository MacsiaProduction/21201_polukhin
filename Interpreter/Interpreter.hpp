#ifndef Interpreter_HPP
#define Interpreter_HPP

#include <stack>
#include <fstream>
#include <sstream>

#include "default_functions_factory.hpp"
#include "default_functions.cpp"
#include "mine_functions_list.hpp"
#include "variables_list.hpp"
#include "../res/log.hpp"

typelog log_level = ERROR;

class Interpreter
{
public:
    // interprets programm from file
    void process_file(std::ifstream &in);
    // interprets programm from stringstream
    void process_text(std::stringstream &in);

private:
    int categorize(std::string name);
    void process_word(std::string tmp, std::stringstream &in);
    void use_function(std::string name, std::stringstream &in);
    void add_mine_function(std::stringstream &in);
    void use_mine_function(std::string name);
    void add_variable(std::stringstream &in);
    void use_variable(std::string name);

private:
    enum category
    {
        error = 0,
        iterator,
        number,
        def_func,
        add_mine,
        use_mine,
        add_var,
        use_var
    };
    std::stack<long long> stack;
    default_functions_factory<default_function> &default_functions =
        *default_functions_factory<default_function>::get_factory_instance();
    mine_functions_list mine_functions;
    variables_list variables;
};
#endif