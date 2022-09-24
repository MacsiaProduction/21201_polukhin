cmake_minimum_required(VERSION 3.8)

set(This FlatMap)

project(${This} C CXX)

set(CMAKE_C_STANDARD 99)
set(CMAKE_CXX_STANDARD 11)
set(CMAKE_POSITION_INDEPENDENT_CODE ON)

enable_testing()

add_subdirectory(googletest)

set(Header
    FlatMap.hpp
)
set(Sources
    FlatMap.cpp
)
add_library(${This} STATIC ${Sources} ${Header} )

add_subdirectory(test)
