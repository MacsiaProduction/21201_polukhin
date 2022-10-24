#include <map>
class mine_functions_list{
public:
    void add_mine_func(std::string name, std::string body) {
        mine_functions.insert(std::map<std::string,std::string>::value_type(name, body));
    };
    bool in_list(std::string name) {
        return mine_functions.find(name) != mine_functions.end();
    };
    std::string body_by_name(std::string name) {
        return mine_functions.at(name);
    }
protected:
    std::map<std::string, std::string> mine_functions;
};