#ifndef default_functions_CPP
#define default_functions_CPP

#include "default_functions.hpp"

void multiply::work(std::stack<long long> &stack, std::stringstream &in, std::stringstream &out)
{
    int first = stack.top();
    stack.pop();
    int second = stack.top();
    stack.pop();
    stack.push(second * first);
}
std::string multiply::get_name()
{
    return "*";
}

void divide::work(std::stack<long long> &stack, std::stringstream &in, std::stringstream &out)
{
    int first = stack.top();
    stack.pop();
    int second = stack.top();
    stack.pop();
    if (first == 0)
        throw std::invalid_argument("dividing by zero");
    stack.push(second / first);
}
std::string divide::get_name()
{
    return "/";
}

void plus::work(std::stack<long long> &stack, std::stringstream &in, std::stringstream &out)
{
    int first = stack.top();
    stack.pop();
    stack.top() = first + stack.top();
}
std::string plus::get_name()
{
    return "+";
}

void minus::work(std::stack<long long> &stack, std::stringstream &in, std::stringstream &out)
{
    int first = stack.top();
    stack.pop();
    int second = stack.top();
    stack.pop();
    stack.push(second - first);
}
std::string minus::get_name()
{
    return "-";
}

void mod::work(std::stack<long long> &stack, std::stringstream &in, std::stringstream &out)
{
    int first = stack.top();
    stack.pop();
    int second = stack.top();
    stack.pop();
    stack.push(second % first);
}
std::string mod::get_name()
{
    return "mod";
}

void copy::work(std::stack<long long> &stack, std::stringstream &in, std::stringstream &out)
{
    int tmp = stack.top();
    stack.push(tmp);
}
std::string copy::get_name()
{
    return "dup";
}

void drop::work(std::stack<long long> &stack, std::stringstream &in, std::stringstream &out)
{
    stack.pop();
}
std::string drop::get_name()
{
    return "drop";
}

void print::work(std::stack<long long> &stack, std::stringstream &in, std::stringstream &out)
{
    if (stack.empty())
        throw std::out_of_range("empty stack");
    long long tmp = stack.top();
    stack.pop();
    mine_out() << int(tmp) << " ";
}
std::string print::get_name()
{
    return ".";
}

void swap::work(std::stack<long long> &stack, std::stringstream &in, std::stringstream &out)
{
    int tmp = stack.top();
    stack.pop();
    int tmp2 = stack.top();
    stack.pop();
    stack.push(tmp);
    stack.push(tmp2);
}
std::string swap::get_name()
{
    return "swap";
}

void rot::work(std::stack<long long> &stack, std::stringstream &in, std::stringstream &out)
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
std::string rot::get_name()
{
    return "rot";
}

void over::work(std::stack<long long> &stack, std::stringstream &in, std::stringstream &out)
{
    int tmp1 = stack.top();
    stack.pop();
    int tmp2 = stack.top();
    stack.push(tmp2);
    stack.push(tmp1);
    stack.push(tmp2);
}
std::string over::get_name()
{
    return "over";
}

void emit::work(std::stack<long long> &stack, std::stringstream &in, std::stringstream &out)
{
    std::cout << (char)stack.top() << " ";
    stack.pop();
}
std::string emit::get_name()
{
    return "emit";
}

void cr::work(std::stack<long long> &stack, std::stringstream &in, std::stringstream &out)
{
    std::cout << std::endl;
}
std::string cr::get_name()
{
    return "cr";
}

void less::work(std::stack<long long> &stack, std::stringstream &in, std::stringstream &out)
{
    int first = stack.top();
    stack.pop();
    int second = stack.top();
    stack.pop();
    stack.push(second < first);
}
std::string less::get_name()
{
    return "<";
}
void greater::work(std::stack<long long> &stack, std::stringstream &in, std::stringstream &out)
{
    int first = stack.top();
    stack.pop();
    int second = stack.top();
    stack.pop();
    stack.push(second > first);
}
std::string greater::get_name()
{
    return ">";
}

void equal::work(std::stack<long long> &stack, std::stringstream &in, std::stringstream &out)
{
    int first = stack.top();
    stack.pop();
    int second = stack.top();
    stack.pop();
    stack.push(second == first);
}
std::string equal::get_name()
{
    return "=";
}

void reference::work(std::stack<long long> &stack, std::stringstream &in, std::stringstream &out)
{
    stack.top() = *reinterpret_cast<long long *>(stack.top());
}
std::string reference::get_name()
{
    return "@";
}
void dereference::work(std::stack<long long> &stack, std::stringstream &in, std::stringstream &out)
{
    long long tmp1 = stack.top();
    stack.pop();
    long long tmp2 = stack.top();
    stack.pop();
    *(reinterpret_cast<long long *>(tmp1)) = tmp2;
}
std::string dereference::get_name()
{
    return "!";
}

void print_string::work(std::stack<long long> &stack, std::stringstream &in, std::stringstream &out)
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
std::string print_string::get_name()
{
    return ".\"";
}

void conditional_operator::work(std::stack<long long> &stack, std::stringstream &in, std::stringstream &out)
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
std::string conditional_operator::get_name()
{
    return "if";
};

void loop::work(std::stack<long long> &stack,
                std::stringstream &in, std::stringstream &out)
{
    int start = stack.top();
    stack.pop();
    int end = stack.top();
    stack.pop();
    LOG(INFO) << "called loop from " << start << " to " << end;
    std::stringstream body_flow;
    std::string tmp;
    unsigned long long counter = 1;
    while (in >> tmp)
    {
        if (tmp == "do")
            counter++;
        if (tmp == "loop")
        {
            counter--;
            if (counter == 0)
                break;
        }
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
std::string loop::get_name()
{
    return "do";
}

#endif