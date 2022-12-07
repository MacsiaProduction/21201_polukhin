#ifndef variables_list_HPP
#define variables_list_HPP

#include <map>
#include <stack>

class variables_list
{
public:
    // adds variable to list
    void add_variable(std::string name)
    {
        vars.push(0);
        long long *reference = &(vars.top());
        long long tmp = reinterpret_cast<long long>(reference);
        variables.insert({name, tmp});
    };
    // checks if variable is in list
    bool in_list(std::string name)
    {
        return variables.find(name) != variables.end();
    };
    // returns casted pointer be name of var
    long long pointer_by_name(std::string name)
    {
        return variables.at(name);
    }

private:
    std::stack<long long> vars;
    std::map<std::string, long long> variables;
};
#endif