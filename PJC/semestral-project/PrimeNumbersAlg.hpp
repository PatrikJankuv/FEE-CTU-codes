//
// Created by pjank on 12/28/2020.
//
#pragma once

#include <vector>

class Eratosthenes{
public:
    void run();

    explicit Eratosthenes(unsigned long long int size);

    void printSieve();

    unsigned long long int getSize() const;

    const char * getName();

    unsigned long long int primeNumbersCount();

    virtual ~Eratosthenes();

private:
    unsigned long long int limit;
    std::vector<bool> sieve;
};

class Sundaram{
public:
    void run();

    explicit Sundaram(unsigned long long int size);

    void printSieve();

    unsigned long long int primeNumbersCount();

    const char * getName();

    virtual ~Sundaram();

    unsigned long long int getSize() const;

private:
    unsigned long long int limit;
    std::vector<bool> sieve;
    bool isTwo;
};

class Atkin{
public:
    void run();

    explicit Atkin(int size);

    void printSieve();

    unsigned long long int primeNumbersCount();

    const char * getName();

    virtual ~Atkin();

    unsigned long long int getSize() const;

private:
    unsigned long long int limit;
    std::vector<bool> sieve;
//    bool *sieve;
};

class Wheel{
public:
    void run();

    explicit Wheel(unsigned long long int size);

    void printWheel();

    unsigned long long int primeNumbersCount();

    const char * getName();

    virtual ~Wheel();

    unsigned long long int getSize() const;

private:
    unsigned long long int limit;
    std::vector<unsigned long long int> wheel;
};

struct Validator{
    static bool validate_simple(unsigned long long int number);

    static bool validate_parallel(unsigned long long int number);

    static bool validate_little_faster(unsigned long long int number);
};


