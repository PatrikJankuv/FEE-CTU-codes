#include <iostream>
#include "PrimeNumbersAlg.hpp"
#include <chrono>

using namespace std;
using namespace std::chrono;

//
// Created by pjank on 1/3/2021.
//
void Wheel::run() {
    for (unsigned long long int i = 2; i < limit; i++) {
        if (wheel.at(i) == 0)
            wheel.at(i) = 1;
        unsigned long long int c = 2;
        unsigned long long int mul = i * c;
        for (; mul < limit;) {
            wheel.at(mul) = -1;
            c++;
            mul = i * c;
        }
    }
}

Wheel::Wheel(unsigned long long int size) : limit(size+1), wheel(size+1, 1) {}

void Wheel::printWheel() {
//    getName();
    for (unsigned long long int i = 2; i < limit; i++) {
        if (wheel[i] == 1) {
            cout << i << " ";
        }
    }

    std::cout << "\n";
//    std::cout << "\nFound " << primeNumbersCount() << " prime numbers\n";
}

unsigned long long int Wheel::getSize() const {
    return limit;
}

unsigned long long int Wheel::primeNumbersCount() {
    int found = 0;
    for (unsigned long long int i = 2; i < limit; i++) {
        if (wheel[i] == 1) {
            found += 1;
        }
    }
    return found;
}

Wheel::~Wheel() = default;

const char * Wheel::getName() {
    return "WHEEL OF FACTORIZATION\n";
};