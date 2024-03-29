#ifndef Interpreter_HPP
#define Interpreter_HPP

#include <stack>
#include <fstream>
#include <sstream>

#include "default_functions_factory.hpp"
#include "default_functions.cpp"
#include "variables_list.hpp"
#include "../res/log.hpp"

typelog log_level = ERROR;
bool mine_testing = true;

class Interpreter
{
public:
    // interprets programm from file
    void process_file(std::ifstream &in);
    // interprets programm from stringstream
    void process_text(std::stringstream &in);

private:
    int categorize(std::string &name);
    void process_word(std::string &tmp, std::stringstream &in);
    void use_function(std::string &name, std::stringstream &in);
    void add_mine_function(std::stringstream &in);
    void use_mine_function(std::string &name);
    void add_variable(std::stringstream &in);
    void use_variable(std::string &name);

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
    factory<default_function> &default_functions =
        *factory<default_function>::get_factory_instance();
    std::map<std::string, std::string> mine_functions;
    variables_list variables;
};
#endif