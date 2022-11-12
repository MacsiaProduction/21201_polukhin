cmake_minimum_required(VERSION 3.8)

project(Interpreter)

set (This Interpreter_testing)

find_package(GTest REQUIRED)
include_directories(${GTEST_INCLUDE_DIRS})

add_executable(${This} Tests.cpp)

target_link_libraries(${This} ${GTEST_LIBRARIES} pthread)