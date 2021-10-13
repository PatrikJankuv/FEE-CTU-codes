# **Porovnání algoritmu na hledaní prvočísel**

V semestrálke porovnám časovú zložitosť algoritmov:
- Eratostenovo sito
- Sundaramovo sito 
- Atkinovo sito
- Wheel factorizatio

## **Manual**
Užívateľ zadá hornú hranicu n, následne algoritmy nájdu všetky prvočísla na intervale <1, n>. Na konci behu programu dôjde k vypísaniu prvočísel a potrebného času jednotlivých algoritmov.


Beh programu je mozne upravovat pomocou prepinacov:
-       --help : vypise ako program funguje
-       --print : vypise do konzoly vsetky prvocisla, ktore boli najdene
-       --eratosthenes : na hladanie prvocisel je pouzite iba Eratostovo sito
-       --sundaram : na hladanie prvocisel je pouzite iba Sundaramovo sito
-       --atkin : na hladanie prvocisel je pouzite iba Atkinovo sito
-       --wheel : na hladanie prvocisel je pouzite iba Faktorizacne koleso
-       --validate : program overi ci je dane cislo prvocislo
-       --parallel : program pobezi paralelne

Algoritmy som sa snažil implementovať aj paralelne, ale nepodarilo sa mi nájsť dostatočňe rýchlu implementáciu, aby bola rýchlejšia ako jednovláknova implemntácia. Pripisujem to tomu, že jednotlivé algoritmy využívajú std::vector<bool>, ktoré predstavujú sita. Tým, že vlákna musia pristupovať do rovnakého vektora, tak beh programu je pomalší. 


Implementoval som aj overovanie či je číslo _n_ prvočíslo. Toto overenie je realizované dosť naivne, a to takým spôsobom, že algoritmus delí všetky čísla menšie ako _n/2_. Podarilo sa mi aj neimplementovať viac vláknovú verziu, ktorá rozdelí čísla na intervaly _<1,n/8>, <n/8, n/4>, <n/4, 3n/8>, <3n/8, n/2>_ a čísla následne čísla z intervalov delí v rôznych vláknach.

## **Meranie**

Realizoval som ho na dvojjadrovom procesore i5-6200U, pre n = 10000000, commit 106b177a. Na základe opakovaných testov môžem konštatovať, že najrýchlejší algoritmus je Atkinovo sito. 

| algoritmus | cas | nájdene prvočísla |
| ------ | ------ |------ |
| SEIVE OF ERATOSTHENES | 8525 ms | 664579 |
| SEIVE OF SUNDARAM | 6648 ms | 664579 |
| SEIVE OF ATKIN | 3311 ms | 664579 |
| WHEEL OF FACTORIZATION | 3755 ms | 664579 |



Algoritmus na overenie či je číslo n prvočíslo som realizoval na rovnakom zariadení. Viac vláknová verzia je približne 2,5 krát rýchlejšia, pre n = 2147483647
|  | čas |
| ------ | ------ |
| jednovláknova verzia | 12465 ms |
| viacvláknova verzia | 4967 ms |


