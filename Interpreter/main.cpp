#include "Interpreter.hpp"
#include <fstream>
int main()
{
    std::ifstream input("in.txt");
    Interpreter I;
    I.process_file(input);
    return 0;
}