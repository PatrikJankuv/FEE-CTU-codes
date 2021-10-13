cmake_minimum_required(VERSION 3.17)
project(semestralka)

set(CMAKE_CXX_STANDARD 17)

add_executable(semestralka main.cpp PrimeNumbersAlg.hpp SieveOfEratosthenes.cpp SieveOfAtkin.cpp SieveOfSundaram.cpp Wheel.cpp Validator.cpp)

if ( CMAKE_CXX_COMPILER_ID MATCHES "Clang|AppleClang|GNU" )
    target_compile_options( semestralka PRIVATE -Wall -Wextra -Wunreachable-code -Wpedantic)
endif()
if ( CMAKE_CXX_COMPILER_ID MATCHES "MSVC" )
    target_compile_options( semestralka PRIVATE /W4 )
endif()

set(THREADS_PREFER_PTHREAD_FLAG ON)
find_package(Threads REQUIRED)
target_link_libraries(semestralka Threads::Threads)