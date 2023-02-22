#include <stdio.h>
#include <math.h>
#include <iostream>
#include <vector>
#include <algorithm>
#include <map>
using namespace std;

const long long int MAX_SIZE = 312500000;

long long M_min, M_max;
long long C_min, C_max;

vector<long long> isprime;
// vector<long long> isprime(MAX_SIZE, true);
vector<long long> SPF;
// vector<long long> SPF(MAX_SIZE);
vector<long long> prime;
vector<long long> combination;
map<long long, vector<long long>> m_and_used_primes;
map<long long, long long> m_and_posible_c;
long long primes_count;
long long ugly_var;

void long_vector(const vector<long long> &v)
{
    cout << "[";
    for (auto i : v)
    {
        cout << i << " ";
    }
    cout << "]\n";
}

bool getM(const vector<long long> &v)
{
    // cout << "get m \n";

    long long temp = v[0];
    vector<long long> temp_primes{};
    temp_primes.push_back(v[0]);

    for (long long i = 1; i < v.size(); ++i)
    {
        temp = temp * v[i];
        temp_primes.push_back(v[i]);
    }

    if (temp <= M_max && temp >= M_min)
    {
        // cout << temp << ": ";
        // for (auto k : temp_primes)
        // {
        //     cout << k << ", ";
        // }
        // cout << endl;
        m_and_used_primes.insert({temp, temp_primes});
        return true;

        // cout << v[0] << " " << v[1] << " " << v[2] << " " << v[3] << " " << v[4] << " " << endl;
    }

    if (temp > M_max)
    {
        return false;
    }
    else
    {
        return true;
    }

    // return false;
    // cout << "lol \n";
}

void combinate_m(vector<long long> &v, vector<long long> &indexes, long long last_move)
{
    bool inRange = getM(v);

    if (inRange)
    {
        if (last_move < (v.size() - 1))
        {
            // cout << "b\n";
            last_move = v.size() - 1;
        }
        indexes[last_move]++;
        v[last_move] = prime[indexes[last_move]];
    }
    else
    {
        if (last_move > 0)
        {
            indexes[last_move - 1] = indexes[last_move - 1] + 1;
            v[last_move - 1] = prime[indexes[last_move - 1]];

            for (long long i = last_move; i < v.size(); i++)
            {
                indexes[i] = indexes[i - 1] + 1;
                v[i] = prime[indexes[i]];
            }
            --last_move;
        }
        else
        {
            return;
        }
    }

    combinate_m(v, indexes, last_move);
}

void generateM(long long p)
{
    vector<long long> indexes(p);
    vector<long long> v(p);

    for (long long i = 0; i < indexes.size(); i++)
    {
        indexes[i] = i;
        v[i] = prime[i];
    }
    combinate_m(v, indexes, p - 1);
}

long long count_value_c(const vector<long long> &v, long long r)
{
    long long temp = prime[v[0]];

    for (long long i = 1; i < r; i++)
    {
        temp = temp * prime[v[i]];
    }

    return temp;
}

void manipulated_seive(long long N)
{
    isprime[0] = false;
    isprime[1] = false;

    for (long long i = 2; i < N; i++)
    {
        if (isprime[i])
        {
            prime.push_back(i);
            SPF[i] = i;
            primes_count++;
        }

        for (long long j = 0; j < (long long)prime.size() && i * prime[j] < N && prime[j] <= SPF[i]; j++)
        {
            isprime[i * prime[j]] = false;
            SPF[i * prime[j]] = prime[j];
        }
    }
}

long long countNonComprimesInRange(long long n, long long bottom, long long top)
{
    long long top_non_comprimes = (top) / n;
    long long bottom_non_comprimes = (bottom - 1) / n;
    long long non_comprimes_in_range = top_non_comprimes - bottom_non_comprimes;
    return non_comprimes_in_range;
}

void getMultiples(long long r, long long indexes[])
{
    long long temp = 1;
    for (long long j = 0; j < r; j++)
    {
        temp = temp * indexes[j];
    }
    ugly_var += countNonComprimesInRange(temp, C_min, C_max);
}

void combinationUtil(vector<long long> &arr, long long indexes[],
                     long long start, long long end,
                     long long index, long long r)
{
    if (index == r)
    {
        getMultiples(r, indexes);
        return;
    }

    for (long long i = start; i <= end && end - i + 1 >= r - index; i++)
    {
        indexes[index] = arr[i];
        combinationUtil(arr, indexes, i + 1,
                        end, index + 1, r);
    }
}

void longCombination(vector<long long> &arr, long long n, long long r)
{
    long long data[r];
    ugly_var = 0;
    combinationUtil(arr, data, 0, n - 1, 0, r);
}

void calculateCForM(long long m, vector<long long> primes)
{
    long long n = primes.size();

    long long possible_cs = C_max - C_min + 1;

    // cout << "m: " << m << endl;
    for (long long i = 1; i <= primes.size(); i++)
    {
        longCombination(primes, n, i);

        if (i % 2 == 0)
        {
            possible_cs += ugly_var;
        }
        else
        {
            possible_cs -= ugly_var;
        }
    }
    // cout << "result: " << possible_cs << endl;

    m_and_posible_c.insert({m, possible_cs});
}

void calculateCs()
{
    for (auto m_primes : m_and_used_primes)
    {
        calculateCForM(m_primes.first, m_primes.second);
    }
}

void finish_a(long long max_a, long long min_a)
{
    long long result = 0;
    for (auto a : m_and_posible_c)
    {
        long long temp = (max_a - 1) / a.first - (min_a - 2) / a.first;
        result += temp * a.second;

        // cout << "m: " << a.first << " a: " << temp << " c: " << a.second << endl;
    }
    cout << result;
}

// driver program to test above function
int main()
{
    ios::sync_with_stdio(false);
    long long a_min, a_max;
    long long c_min, c_max;
    long long m_min, m_max;
    long long p;

    SPF.reserve(MAX_SIZE);

    // isprime.reserve(MAX_SIZE);
    // fill(isprime.begin(), isprime.end(), true);

    cin >> a_min >> a_max;
    cin >> c_min >> c_max;
    cin >> m_min >> m_max;
    cin >> p;
    M_max = m_max;
    M_min = m_min;
    C_min = c_min;
    C_max = c_max;

    // vector<long long> fuj(MAX_SIZE, true);
    // isprime = fuj;
    // vector<long long> hnuj(MAX_SIZE);
    // SPF = hnuj;

    long long opt = 1;
    int temp_p = p - 2;

    switch (temp_p)
    {
    case 1:
        opt = (m_max / 2);
        break;
    case 2:
        opt = (m_max / 6);
        break;
    case 3:
        opt = (m_max / 30);
        break;
    case 4:
        opt = (m_max / 210);
        break;
    case 5:
        opt = (m_max / 2310);
        break;
    case 6:
        opt = (m_max / 30030);
        break;
    case 7:
        opt = (m_max / 510510);
        break;
    default:
        opt = m_max;
        break;
    }

    for (int i = 0; i <= opt; i++)
    {
        SPF.push_back(1);
        isprime.push_back(true);
    }

    manipulated_seive(opt);

    generateM(p);
    calculateCs();
    finish_a(a_max, a_min);

    return 0;
}