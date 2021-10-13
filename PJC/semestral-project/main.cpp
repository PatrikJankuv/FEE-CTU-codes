#include <iostream>
#include <chrono>
#include <thread>         // std::this_thread::sleep_for

#include "PrimeNumbersAlg.hpp"

using namespace std;
using namespace std::chrono;

bool eratosthenes = false;
bool sundaram = false;
bool atkin = false;
bool wheel_run_bool = false;

template<typename TimePoint>
std::chrono::milliseconds to_ms(TimePoint tp) {
    return std::chrono::duration_cast<std::chrono::milliseconds>(tp);
}

void validate_primary(bool parallel) {
    std::cout << "Vyber cislo, ktore chces otestovat ci je cislo prvocislo [2147483647 8589935681 je pekne cislo]"
              << std::endl;
    unsigned long long int n;
    std::cin >> n;

    auto start = std::chrono::high_resolution_clock::now();
    auto end = std::chrono::high_resolution_clock::now();
    bool is_primary;

    if (parallel) {
        start = std::chrono::high_resolution_clock::now();
        is_primary = Validator::validate_parallel(n);
        end = std::chrono::high_resolution_clock::now();
        std::cout << "Cas " << to_ms(end - start).count() << " ms.\n";
    } else {
        start = std::chrono::high_resolution_clock::now();
        is_primary = Validator::validate_simple(n);
        end = std::chrono::high_resolution_clock::now();
        std::cout << "Cas " << to_ms(end - start).count() << " ms.\n";
    }

    if (is_primary) {
        std::cout << "Cislo " << n << " je prvocislo\n";
    } else {
        std::cout << "Cislo " << n << " nie je prvocislo\n";
    }
}

void eratosthenes_run(bool print, unsigned long long int n) {
    if (eratosthenes) {
        auto start = std::chrono::high_resolution_clock::now();
        auto sieve = new Eratosthenes(n);
        sieve->run();

        auto end = std::chrono::high_resolution_clock::now();
        if (print) sieve->printSieve();
        unsigned long long int temp = sieve->primeNumbersCount();
        std::cout << sieve->getName() << "Cas " << to_ms(end - start).count() << " ms.\n"
                  << "Naslo " << temp << " prvocisel.\n\n";
        delete sieve;
    }
}

void sundaram_run(bool print, unsigned long long int n) {
    if (sundaram) {
        auto start = std::chrono::high_resolution_clock::now();
        auto sieve2 = new Sundaram(n);
        sieve2->run();

        auto end = std::chrono::high_resolution_clock::now();
        if (print) sieve2->printSieve();
        unsigned long long int temp = sieve2->primeNumbersCount();
        std::cout << sieve2->getName() << "Cas " << to_ms(end - start).count() << " ms.\n"
                  << "Naslo " << temp << " prvocisel.\n\n";
        delete sieve2;
    }
}

void atkin_run(bool print, unsigned long long int n) {
    if (atkin) {
        auto start = std::chrono::high_resolution_clock::now();
        auto sieve3 = new Atkin(n);
        sieve3->run();

        auto end = std::chrono::high_resolution_clock::now();
        if (print) sieve3->printSieve();
        unsigned long long int temp = sieve3->primeNumbersCount();
        std::cout << sieve3->getName() << "Cas " << to_ms(end - start).count() << " ms.\n";
        std::cout << "Naslo " << temp << " prvocisel.\n\n";
        delete sieve3;
    }
}

void wheel_run(bool print, unsigned long long int n) {
    if (wheel_run_bool) {
        auto start = std::chrono::high_resolution_clock::now();
        auto wheel = new Wheel(n);
        wheel->run();
        auto end = std::chrono::high_resolution_clock::now();
        if (print) wheel->printWheel();
        unsigned long long int temp = wheel->primeNumbersCount();
        std::cout << wheel->getName() << "Cas " << to_ms(end - start).count() << " ms.\n";
        std::cout << "Naslo " << temp << " prvocisel.\n\n";
        delete wheel;
    }
}

void parallel_test(bool print, unsigned long long int n) {
    thread thread1(eratosthenes_run, print, n);
    thread thread2(sundaram_run, print, n);
    thread thread3(atkin_run, print, n);
    thread thread4(wheel_run, print, n);

    thread1.join();
    thread2.join();
    thread3.join();
    thread4.join();

}

void test(bool print, bool parallel) {
    std::cout << "Vyber velkost sita, pre ktoru chces otestovat algoritmy" << std::endl;
    unsigned long long int n;
    std::cin >> n;

    if (parallel) {
        parallel_test(print, n);
    } else {
        eratosthenes_run(print, n);

        sundaram_run(print, n);

        atkin_run(print, n);

        wheel_run(print, n);
    }
}


int main(int argc, char *argv[]) {
    bool parallel = false;
    bool validate = false;
    bool print = false;

    if (argc == 1) {
        eratosthenes = true;
        sundaram = true;
        atkin = true;
        wheel_run_bool = true;
    }

    for (int i = 1; i < argc; i++) {
        string argument(argv[i]);
        if (argument == "-p" || argument == "--parallel") {
            parallel = true;
            cout << "Program bezi v paralelnom mode\n";
            if (argc == 2) {
                eratosthenes = true;
                sundaram = true;
                atkin = true;
                wheel_run_bool = true;
            }
        } else if ((argument == "--help")) {
            // print help
            cout << "Porovnanie algoritmov na hladanie prvocisel\n"
                    "\n"
                    "Tento nastroj porovnava styri algoritmy na hladanie prvocisel v urcitom rozsahu \n"
                    "It reads words from standard input and returns available corrections for each word on separate line.\n"
                    "   -Eratestovo sito\n"
                    "   -Sundaramovo sito\n"
                    "   -Atkinovo sito\n"
                    "   -Faktorizacne koleso\n"
                    "\n"
                    "Tento program okrem toho umoznuje overovanie ci je cislo prvocislo. Overenie realizuje pomocou dvoch algroritmou:\n"
                    //            18,446,744,073,709,551,615
                    "   -Delenie kazdym cislom, ktore je mensie ako hladane cislo - velmi pomale (implementovana aj viacvlaknova verzia)\n"
//                    "   -Optimalizovany delenie na zaklade druhej mocnini"

                    "Pouzitiel: semestralka [moznosti]\n"
                    "  prepinace:\n"
                    "      --print: vypise do konzoly vsetky prvocisla, ktore boli najdene\n"
                    "      --eratosthenes: na hladanie prvocisel je pouzite iba Eratostovo sito\n"
                    "      --sundaram: na hladanie prvocisel je pouzite iba Sundaramovo sito\n"
                    "      --atkin: na hladanie prvocisel je pouzite iba Atkinovo sito\n"
                    "      --wheel: na hladanie prvocisel je pouzite iba Faktorizacne koleso\n"
                    "      --validate: algoritmus overi ci je dane cislo prvocislo\n"
                    "      --parallel: program pobezi paralelne\n";
            return 0;

        } else if ((argument == "--print")) {
            print = true;
            if (argc == 2) {
                eratosthenes = true;
                sundaram = true;
                atkin = true;
                wheel_run_bool = true;
            }
        } else if ((argument == "--eratosthenes")) {
            eratosthenes = true;
        } else if ((argument == "--sundaram")) {
            sundaram = true;
        } else if ((argument == "--atkin")) {
            atkin = true;
        } else if ((argument == "--wheel")) {
            wheel_run_bool = true;
        } else if ((argument == "--validate")) {
            validate = true;
        } else {
// unsupported flag
            if (argument.rfind('-', 0) == 0) {
                cerr << "nepodporovany argument " << argument << endl;
                return 1;
            }
        }
    }

    char cont = 'Y';

    while (cont == 'y' || cont == 'Y') {

        if (validate) {
            validate_primary(parallel);
        } else {
            test(print, parallel);
        }
        std::cout << "Ak chces zopakovat test stlac \"y\"\n";
        std::cin >> cont;
    }

    std::cin.get();

    return 0;
}
