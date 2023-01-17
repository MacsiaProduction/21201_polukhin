#ifndef variables_list_HPP
#define variables_list_HPP

#include <map>
#include <stack>
#include <string>

class variables_list
{
public:
    // adds variable to list
    void add_variable(std::string &name)
    {
        number_of_vars++;
        _id_by_name.insert({name, number_of_vars});
        _value_by_id.insert({number_of_vars, 0});
    };
    bool in_list(std::string &name)
    {
        return _id_by_name.count(name) != 0;
    };
    int id_by_name(std::string &name)
    {
        return _id_by_name.at(name);
    }
    int &value_by_id(int id)
    {
        return _value_by_id.at(id);
    }

private:
    size_t number_of_vars = 0;
    std::map<std::string, int> _id_by_name;
    std::map<int, int> _value_by_id;
};
#endif