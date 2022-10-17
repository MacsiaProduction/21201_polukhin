#include <string>
#include <iostream>
#include <fstream>
#include "Interpreter.hpp"

int main() {
    std::ifstream input("in.txt");
    Interpreter I;
    std::string line;
    while(std::getline(input, line)) I.process_line(line);
    return 0;
}   