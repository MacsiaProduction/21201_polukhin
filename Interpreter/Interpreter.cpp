#include <string>
#include <iostream>
#include <sstream>
#include <fstream>
#include "Interpreter.hpp"
int main() {
    std::ifstream input("in.txt");
    Interpreter I;  
    I.process_text(input);
    return 0;
}   