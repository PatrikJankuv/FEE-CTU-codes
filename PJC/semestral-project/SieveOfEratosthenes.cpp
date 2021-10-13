#include "PrimeNumbersAlg.hpp"
#include <iostream>
#include <chrono>

using namespace std;
using namespace std::chrono;

//
// Created by pjank on 1/3/2021.
//
/**
 * v tejto metode bezi algoritmus
 */
void Eratosthenes::run() {
    for (unsigned long long int i = 2; i * i <= limit; i++) {
        if (!sieve[i])
            continue;
        for (unsigned long long int j = i * i; j < limit; j += i) {
            sieve.at(j) = false;
        }
    }
};

Eratosthenes::Eratosthenes(unsigned long long int size) : limit(size+1), sieve(size+1, true) {};

Eratosthenes::~Eratosthenes() {
//    delete[] sieve;
}

unsigned long long int Eratosthenes::getSize() const {
    return limit;
}

void Eratosthenes::printSieve() {
//    getName();
    for (unsigned long long int i = 2; i < limit; i++) {
        if (sieve[i])
            std::cout << i << " ";
    }

    std::cout << "\n";
//    std::cout << "\nFound " << primeNumbersCount() << " prime numbers\n";
}

unsigned long long int Eratosthenes::primeNumbersCount() {
    int count = 0;

    for (unsigned long long int i = 2; i < limit; i++) {
        if (sieve[i])
            count += 1;
    }

    return count;
}

 const char * Eratosthenes::getName() {
    return "SEIVE OF ERATOSTHENES\n";
};
