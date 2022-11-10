cmake_minimum_required(VERSION 2.6)
project(Interpreter)
# Locate GTest
find_package(GTest REQUIRED)
include_directories(${GTEST_INCLUDE_DIRS})
# Link runTests with what we want to test and the GTest and pthread library
add_executable(Interpreter_test Tests.cpp)
target_link_libraries(Interpreter_test ${GTEST_LIBRARIES} pthread)