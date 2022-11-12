#ifndef default_functions_HPP
#define default_functions_HPP
#include <sstream>
#include "../res/out.hpp"
#include "default_function_interface.hpp"
#include "auto_registration.hpp"

struct multiply : default_function
{
    void work(std::stack<long long> &stack, std::stringstream &in, std::stringstream &out) override
    {
        int first = stack.top();
        stack.pop();
        int second = stack.top();
        stack.pop();
        stack.push(second * first);
    }
    std::string get_name() override
    {
        return "*";
    }
};

struct divide : default_function
{
    void work(std::stack<long long> &stack, std::stringstream &in, std::stringstream &out) override
    {
        int first = stack.top();
        stack.pop();
        int second = stack.top();
        stack.pop();
        if (first == 0)
            throw std::invalid_argument("dividing by zero");
        stack.push(second / first);
    }
    std::string get_name() override
    {
        return "/";
    }
};

struct plus : default_function
{
    void work(std::stack<long long> &stack, std::stringstream &in, std::stringstream &out) override
    {
        int first = stack.top();
        stack.pop();
        stack.top() = first + stack.top();
    }
    std::string get_name() override
    {
        return "+";
    }
};

struct minus : default_function
{
    void work(std::stack<long long> &stack, std::stringstream &in, std::stringstream &out) override
    {
        int first = stack.top();
        stack.pop();
        int second = stack.top();
        stack.pop();
        stack.push(second - first);
    }
    std::string get_name() override
    {
        return "-";
    }
};

struct mod : default_function
{
    void work(std::stack<long long> &stack, std::stringstream &in, std::stringstream &out) override
    {
        int first = stack.top();
        stack.pop();
        int second = stack.top();
        stack.pop();
        stack.push(second % first);
    }
    std::string get_name() override
    {
        return "mod";
    }
};

struct copy : default_function
{
    void work(std::stack<long long> &stack, std::stringstream &in, std::stringstream &out) override
    {
        int tmp = stack.top();
        stack.push(tmp);
    }
    std::string get_name() override
    {
        return "dup";
    }
};

struct drop : default_function
{
    void work(std::stack<long long> &stack, std::stringstream &in, std::stringstream &out) override
    {
        stack.pop();
    }
    std::string get_name() override
    {
        return "drop";
    }
};

struct print : default_function
{
    void work(std::stack<long long> &stack, std::stringstream &in, std::stringstream &out) override
    {
        if (stack.empty())
            throw std::out_of_range("empty stack");
        long long tmp = stack.top();
        stack.pop();
        mine_out() << int(tmp) << " ";
    }
    std::string get_name() override
    {
        return ".";
    }
};

struct swap : default_function
{
    void work(std::stack<long long> &stack, std::stringstream &in, std::stringstream &out) override
    {
        int tmp = stack.top();
        stack.pop();
        int tmp2 = stack.top();
        stack.pop();
        stack.push(tmp);
        stack.push(tmp2);
    }
    std::string get_name() override
    {
        return "swap";
    }
};

// works in real Forth way))
struct rot : default_function
{
    void work(std::stack<long long> &stack, std::stringstream &in, std::stringstream &out) override
    {
        int tmp1 = stack.top();
        stack.pop();
        int tmp2 = stack.top();
        stack.pop();
        int tmp3 = stack.top();
        stack.pop();
        stack.push(tmp2);
        stack.push(tmp1);
        stack.push(tmp3);
    }
    std::string get_name() override
    {
        return "rot";
    }
};

struct over : default_function
{
    void work(std::stack<long long> &stack, std::stringstream &in, std::stringstream &out) override
    {
        int tmp1 = stack.top();
        stack.pop();
        int tmp2 = stack.top();
        stack.push(tmp2);
        stack.push(tmp1);
        stack.push(tmp2);
    }
    std::string get_name() override
    {
        return "over";
    }
};

struct emit : default_function
{
    void work(std::stack<long long> &stack, std::stringstream &in, std::stringstream &out) override
    {
        std::cout << (char)stack.top() << " ";
        stack.pop();
    }
    std::string get_name() override
    {
        return "emit";
    }
};

struct cr : default_function
{
    void work(std::stack<long long> &stack, std::stringstream &in, std::stringstream &out) override
    {
        std::cout << std::endl;
    }
    std::string get_name() override
    {
        return "cr";
    }
};

struct less : default_function
{
    void work(std::stack<long long> &stack, std::stringstream &in, std::stringstream &out) override
    {
        int first = stack.top();
        stack.pop();
        int second = stack.top();
        stack.pop();
        stack.push(second < first);
    }
    std::string get_name() override
    {
        return "<";
    }
};

struct greater : default_function
{
    void work(std::stack<long long> &stack, std::stringstream &in, std::stringstream &out) override
    {
        int first = stack.top();
        stack.pop();
        int second = stack.top();
        stack.pop();
        stack.push(second > first);
    }
    std::string get_name() override
    {
        return ">";
    }
};

struct equal : default_function
{
    void work(std::stack<long long> &stack, std::stringstream &in, std::stringstream &out) override
    {
        int first = stack.top();
        stack.pop();
        int second = stack.top();
        stack.pop();
        stack.push(second == first);
    }
    std::string get_name() override
    {
        return "=";
    }
};

struct reference : default_function
{
    void work(std::stack<long long> &stack, std::stringstream &in, std::stringstream &out) override
    {
        stack.top() = *reinterpret_cast<long long *>(stack.top());
    }
    std::string get_name() override
    {
        return "@";
    }
};

struct dereference : default_function
{
    void work(std::stack<long long> &stack, std::stringstream &in, std::stringstream &out) override
    {
        long long tmp1 = stack.top();
        stack.pop();
        long long tmp2 = stack.top();
        stack.pop();
        *(reinterpret_cast<long long *>(tmp1)) = tmp2;
    }
    std::string get_name() override
    {
        return "!";
    }
};

struct print_string : default_function
{
    void work(std::stack<long long> &stack, std::stringstream &in, std::stringstream &out) override
    {
        char tmp = in.get();
        if (tmp != ' ')
            throw std::logic_error("expect ' ' in \" string\"");
        std::string str = "";
        while (true)
        {
            tmp = in.get();
            if (tmp == EOF)
                throw std::logic_error("expect '\"' after string");
            if (tmp == '"')
                break;
            str += tmp;
        }
        mine_out() << str << " ";
    }
    std::string get_name() override
    {
        return ".\"";
    }
};

struct conditional_operator : default_function
{
    void work(std::stack<long long> &stack, std::stringstream &in, std::stringstream &out) override
    {
        const bool condition = stack.top();
        stack.pop();
        LOG(INFO) << "starting conditional operator, condition = " << condition;
        enum state
        {
            then_b = 0,
            else_b,
            complete
        };
        state b = then_b;
        std::string tmp;
        while (in >> tmp)
        {
            if (tmp == "else")
            {
                b = else_b;
            }
            else if (tmp == "then")
            {
                b = complete;
                break;
            }
            else
            {
                switch (b)
                {
                case then_b:
                    if (condition)
                        out << tmp << " ";
                    break;
                case else_b:
                    if (!condition)
                        out << tmp << " ";
                    break;
                case complete:
                    throw std::logic_error("incomplete if");
                    break;
                }
            }
        }
        if (b != complete)
            throw std::logic_error("incomplete if");
        LOG(INFO) << "body of conditional operator is: '" + out.str() + "'";
        in >> tmp;
        if (tmp != ";")
        {
            LOG(ERROR) << "didn't found ; after conditional operator";
            throw std::logic_error("didn't get ; after if");
        }
    };
    std::string get_name() override
    {
        return "if";
    };
};

struct loop : default_function
{
    void work(std::stack<long long> &stack, std::stringstream &in, std::stringstream &out) override
    {
        int start = stack.top();
        stack.pop();
        int end = stack.top();
        stack.pop();
        LOG(INFO) << "called loop from " << start << " to " << end;
        std::stringstream body_flow;
        std::string tmp;
        while (in >> tmp)
        {
            if (tmp == "loop")
                break;
            body_flow << tmp << " ";
        }
        if (tmp != "loop")
            throw std::logic_error("incomplete loop");
        LOG(INFO) << "loop's body is \"" << body_flow.str() << "\"";
        for (int i = start; i < end; i++)
        {
            std::stringstream body_flow_copy(body_flow.str());
            while (body_flow_copy >> tmp)
            {
                if (tmp == "i")
                    out << i << " ";
                else
                    out << tmp << " ";
            }
        }
        in >> tmp; //;
        if (tmp != ";")
        {
            LOG(ERROR) << "didn't found ; after loop";
            throw std::logic_error("didn't get ; after loop");
        }
    }
    std::string get_name() override
    {
        return "do";
    }
};

#endif