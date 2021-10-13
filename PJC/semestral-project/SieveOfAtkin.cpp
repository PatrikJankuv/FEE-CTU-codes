#include <iostream>
#include "PrimeNumbersAlg.hpp"
#include <chrono>

using namespace std;
using namespace std::chrono;

//
// Created by pjank on 1/3/2021.
//
void Atkin::run() {

    for (unsigned long long int x = 1; x * x < limit; x++) {
        for (unsigned long long int y = 1; y * y < limit; y++) {

            unsigned long long int n = (4 * x * x) + (y * y);
            if (n < limit && (n % 12 == 1 || n % 12 == 5))
                sieve.at(n) = !sieve.at(n);

            n = (3 * x * x) + (y * y);
            if (n < limit && n % 12 == 7)
                sieve.at(n) = !sieve.at(n);

            n = (3 * x * x) - (y * y);
            if (x > y && n < limit && n % 12 == 11)
                sieve.at(n) = !sieve.at(n);
        }
    }

    for (unsigned long long int r = 5; r * r <= limit; r++) {
        if (sieve[r]) {
            for (unsigned long long int i = r * r; i <= limit; i += r * r)
                sieve[i] = false;
        }
    }
}

Atkin::Atkin(int size) : limit(size+1), sieve(size+2, false) {}

void Atkin::printSieve() {
//    getName();
    if (limit > 2)
        cout << 2 << " ";
    if (limit > 3)
        cout << 3 << " ";

    for (int i = 1; i <= limit; i++)
        if (sieve[i]) {
            std::cout << i << " ";
        }

    std::cout << "\n";
//    std::cout << "\nFound " << primeNumbersCount() << " prime numbers\n";
}

unsigned long long int Atkin::getSize() const {
    return limit;
}

unsigned long long int Atkin::primeNumbersCount() {
    int found = 0;

    if (limit > 2)
        found += 1;
    if (limit > 3)
        found += 1;

    for (int i = 1; i <= limit; i++)
        if (sieve[i]) {
            found += 1;
        }

    return found;
}

Atkin::~Atkin() {
//    delete []sieve;
};

const char * Atkin::getName() {
    return "SEIVE OF ATKIN\n";
};