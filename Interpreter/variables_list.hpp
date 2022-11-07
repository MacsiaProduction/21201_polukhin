#include <map>
#include <stack>
class variables_list{
public:
    void add_variable(std::string name) {
        vars.push(0);
        long long* reference = &(vars.top());
        long long tmp = reinterpret_cast<long long>(reference);
        variables.insert({name, tmp});
    };
    bool in_list(std::string name) {
        return variables.find(name) != variables.end();
    };
    long long pointer_by_name(std::string name) {
        return variables.at(name);
    }
protected:
    std::stack <long long> vars;
    std::map<std::string, long long> variables;
};