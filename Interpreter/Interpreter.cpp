#include <string>
#include <iostream>
#include <sstream>
#include "Interpreter.hpp"

int main() {
    std::stringstream input("in.txt");
    Interpreter I;
    I.process_text(input);
    return 0;
}   