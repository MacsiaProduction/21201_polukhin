cmake_minimum_required(VERSION 3.8)

project(Interpreter)

set(CMAKE_POSITION_INDEPENDENT_CODE ON)

set(This Interpreter_main)

set (HEADERS
    Interpreter.hpp
    auto_registration.hpp
    default_function_interface.hpp
    default_functions_factory.hpp
    default_functions.hpp
    mine_functions_list.hpp
    variables_list.hpp
)

set ( SOURCES
    Interpreter.cpp
    default_functions.cpp
)

add_executable(${This} main.cpp ${SOURCES} ${HEADERS})

add_library(${This} STATIC ${SOURCES} ${HEADERS})

add_subdirectory(Test)

enable_testing()