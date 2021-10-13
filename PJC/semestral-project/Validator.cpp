#include <iostream>
#include "PrimeNumbersAlg.hpp"
#include <thread>

using namespace std;
//
// Created by pjank on 1/18/2021.
//
bool t1 = false;
bool t2 = false;
bool t3 = false;
bool t4 = false;

//tieto metody testuju ci je cislo prvocislo, ak ano vrati true inak false
bool Validator::validate_simple(unsigned long long int number) {
    unsigned long long int temp = number / 2;

    if (number <= 1)
        return false;

    for (unsigned long long int i = 2; i < temp; i++) {
        if (number % i == 0)
            return false;
    }
    return true;
}


bool partial_validate_simple(unsigned long long bottom, unsigned long long top, unsigned long long int number) {
    for (unsigned long long int i = bottom; i < top; i++) {
        if (number % i == 0) {
            std::cout << i << " j \n";
            return false;
        }
    }
    return true;
}

void th1_run(unsigned long long bottom, unsigned long long top, unsigned long long int number) {
    t1 = partial_validate_simple(bottom, top, number);
}

void th2_run(unsigned long long bottom, unsigned long long top, unsigned long long int number) {
    t2 = partial_validate_simple(bottom, top, number);
}

void th3_run(unsigned long long bottom, unsigned long long top, unsigned long long int number) {
    t3 = partial_validate_simple(bottom, top, number);
}

void th4_run(unsigned long long bottom, unsigned long long top, unsigned long long int number) {
    t4 = partial_validate_simple(bottom, top, number);
}


bool Validator::validate_parallel(unsigned long long number) {
    unsigned long long temp2 = number / 2;
    unsigned long long temp4 = number / 4;
    unsigned long long temp8 = number / 8;
    unsigned long long temp83 = (number / 8) * 3;


    if (number <= 1)
        return false;
    if (number <= 3)
        return true;
    if (number == 5)
        return true;

    thread th1(th1_run, 2, temp8, number);
    thread th2(th2_run, temp8, temp4, number);
    thread th3(th3_run, temp4, temp83, number);
    thread th4(th4_run, temp83, temp2, number);

    th1.join();
    th2.join();
    th3.join();
    th4.join();

    std::cout << t1 << " " << t2 << " " << t3 << " " << t4 << "\n";
//    return t1 && t2;
    return (t1 && t2 && t3 && t4);
}

// tento skrip vyuziva tzv. 6+-1 optimalizaciu
bool Validator::validate_little_faster(unsigned long long int number) {
    if (number <= 3) {
        return number > 1;
    }
    if (number % 2 == 0 || number % 3 == 0) {
        return false;
    }

    int i = 5;
    while (i * i <= number) {
        if ((number % i == 0) || (number % (i + 2) == 0)) {
            return false;
        }
        i += 6;
    }

    return true;
}
//    return true;



