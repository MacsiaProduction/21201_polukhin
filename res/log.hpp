#ifndef LOG_HPP
#define LOG_HPP
#include <iostream>

enum typelog
{
    DEBUG,
    INFO,
    WARN,
    ERROR,
    SILIENCE
};

extern typelog log_level;

class LOG
{
public:
    LOG() = default;
    LOG(typelog type)
    {
        if (type == SILIENCE)
            throw std::invalid_argument("SILIENCE is flag to turn off logs");
        msglevel = type;
        operator<<("[" + getLabel(type) + "] ");
    }
    ~LOG()
    {
        if (opened)
        {
            std::cout << std::endl;
        }
        opened = false;
    }
    template <class T>
    LOG &operator<<(const T &msg)
    {
        if (msglevel >= log_level)
        {
            std::cout << msg;
            opened = true;
        }
        return *this;
    }

private:
    bool opened = false;
    typelog msglevel = DEBUG;
    inline std::string getLabel(typelog type)
    {
        std::string label;
        switch (type)
        {
        case DEBUG:
            label = "\033[1;36mDEBUG\033[0m";
            break;
        case INFO:
            label = "\033[1;37mINFO\033[0m";
            break;
        case WARN:
            label = "\033[1;33mWARN\033[0m";
            break;
        case ERROR:
            label = "\033[1;31mERROR\033[0m";
            break;
        }
        return label;
    }
};
#endif