#include "PrimeNumbersAlg.hpp"
#include <iostream>
#include <chrono>

using namespace std;
using namespace std::chrono;
//
// Created by pjank on 1/3/2021.
//

void Sundaram::run() {
    for (unsigned long long int i = 1; i < limit; i++) {
        for (unsigned long long int j = i; (i + j + 2 * i * j) < limit; ++j) {
               sieve.at(i + j + 2 * i * j) = false;
        }
    }
}

Sundaram::Sundaram(unsigned long long int size) : limit(((size + 1) / 2)), sieve(((size + 1) / 2), true), isTwo(size == 2) {};

void Sundaram::printSieve() {
//    getName();
    if (limit >= 2 || isTwo) {
        std::cout << 2 << " ";
    }

    for (unsigned long long int i = 1; i < limit; ++i) {
        if (sieve[i])
            std::cout << i * 2 + 1 << " ";
    }

    std::cout << "\n";
//    std::cout << "\nFound " << primeNumbersCount() << " prime numbers\n";
}

unsigned long long int Sundaram::getSize() const {
    return limit;
}

unsigned long long int Sundaram::primeNumbersCount() {
    int count = 0;
    if (limit >= 2 || isTwo) {
        count += 1;
    }

    for (unsigned long long int i = 1; i < limit; i++) {
        if (sieve[i])
            count += 1;
    }

    return count;
}

Sundaram::~Sundaram() {

}

const char * Sundaram::getName() {
    return "SEIVE OF SUNDARAM\n";
};