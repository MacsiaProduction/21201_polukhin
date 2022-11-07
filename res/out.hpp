#ifndef out_HPP
#define out_HPP
#include <iostream>
#include <sstream>
#include <string>

#define mine_testing

class mine_out
{
public:
    mine_out() = default;
    template <class T>
    mine_out &operator<<(const T &msg)
    {
#ifndef mine_testing
        std::cout << msg;
#else
        get_output() << msg;
#endif
        return *this;
    }
    static std::stringstream &get_output()
    {
        static std::stringstream buffer;
        return buffer;
    }
    static std::string flush_buffer()
    {
        std::string tmp = get_output().str();
        get_output().str("");
        return tmp;
    }
};
#endif