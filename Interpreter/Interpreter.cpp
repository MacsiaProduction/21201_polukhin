#include "Interpreter.hpp"

void Interpreter::process_file(std::ifstream &in)
{
    LOG(INFO) << "calling processing text from file";
    std::stringstream input_flow;
    std::string tmp;
    while (std::getline(in, tmp))
        input_flow << tmp << '\n';
    process_text(input_flow);
}

void Interpreter::process_text(std::stringstream &in)
{
    std::string tmp;
    while (in >> tmp)
        process_word(tmp, in);
}

int Interpreter::categorize(std::string &name)
{
    if (name == "i")
        LOG(ERROR) << "using i outside the loop";
    for (int i = 0; i < name.length(); i++)
    {
        if (i == 0 && name.length() != 1 && name[i] == '-')
            continue;
        if (!isdigit(name[i]))
            break;
        if (isdigit(name[i]) & (i == name.length() - 1))
            return number;
    }
    if (name == ":")
        return add_mine;
    if (name == "variable")
        return add_var;
    if (default_functions.in_list(name))
        return def_func;
    if (mine_functions.count(name) != 0)
        return use_mine;
    if (variables.in_list(name))
        return use_var;
    return error;
};

void Interpreter::process_word(std::string &tmp, std::stringstream &in)
{
    switch (categorize(tmp))
    {
    case number:
        LOG(INFO) << "pushing " << tmp << " to stack";
        stack.push(stoi(tmp));
        break;
    case def_func:
        use_function(tmp, in);
        break;
    case add_var:
        add_variable(in);
        break;
    case use_var:
        use_variable(tmp);
        break;
    case add_mine:
        add_mine_function(in);
        break;
    case use_mine:
        use_mine_function(tmp);
        break;
    default:
        LOG(ERROR) << "wrong input, \'" << tmp << "\' wasn't recognized";
        throw std::invalid_argument("received unrecognized input");
        break;
    }
}

void Interpreter::use_function(std::string &name, std::stringstream &in)
{
    LOG(INFO) << "called function '" << name << "'";
    std::stringstream out;
    default_functions.get_by_name(name)->work(stack, variables, in, out);
    process_text(out);
};

void Interpreter::add_mine_function(std::stringstream &in)
{
    std::string name;
    in >> name;
    std::string body_str;
    std::getline(in, body_str);
    mine_functions.insert({name, body_str});
    LOG(INFO) << "added new function " << name << " with body " << body_str;
}

void Interpreter::use_mine_function(std::string &name)
{
    std::stringstream body;
    body << mine_functions.at(name);
    LOG(INFO) << "called function " << name << " with body " << body.str();
    process_text(body);
}

void Interpreter::add_variable(std::stringstream &in)
{
    std::string name;
    in >> name;
    variables.add_variable(name);
}

void Interpreter::use_variable(std::string &name)
{
    stack.push(variables.id_by_name(name));
}
