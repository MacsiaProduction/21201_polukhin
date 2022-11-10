#include <map>
class mine_functions_list
{
public:
    //adds custom function to list
    void add_mine_func(std::string name, std::string body)
    {
        mine_functions.insert({name, body});
    };
    //checks if function is in list
    bool in_list(std::string name)
    {
        return mine_functions.find(name) != mine_functions.end();
    };
    // run custom function by name
    std::string body_by_name(std::string name)
    {
        return mine_functions.at(name);
    }

protected:
    std::map<std::string, std::string> mine_functions;
};