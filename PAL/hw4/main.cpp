// hw4
#include <stdio.h>
#include <math.h>
#include <iostream>
#include <vector>
#include <algorithm>
#include <map>

using namespace std;

int alphabetConverter(char ch)
{
    return int(ch) - 97;
}

class Automat
{
    int states, alphabet, final_states, positive_examples, negative_examples, example_length;

    int sqrt_l, log_l;
    vector<vector<int>> automat;
    map<string, vector<string>> pos_map;
    map<string, vector<string>> neg_map;
    map<string, vector<int>> skip_to;

public:
    void addItems(vector<int> states);
    void add_positive_example(string example);
    void add_negative_example(string example);
    void manage_exampeles(string example);
    void run();
    void run_skip_to();
    void todo();
    Automat(int states, int alphabet, int final_states, int positive_examles, int negative_examplements, int example_length);
};

Automat::Automat(int states, int alphabet, int final_states, int positive_examles, int negative_examplements, int example_length)
{
    this->states = states;
    this->alphabet = alphabet;
    this->final_states = final_states;
    this->positive_examples = positive_examles;
    this->negative_examples = negative_examplements;
    this->example_length = example_length;
    this->sqrt_l = sqrt(example_length);
    this->log_l = log(example_length);

    this->automat = vector<vector<int>>();
    this->pos_map = map<string, vector<string>>();
    this->neg_map = map<string, vector<string>>();
    this->skip_to = map<string, vector<int>>();
}

void Automat::add_positive_example(string example)
{
    // positive_examples_v.push_back(example);
    string key = example.substr(0, example_length - sqrt_l);
    string rest = example.substr(example_length - sqrt_l, sqrt_l);

    if (pos_map.count(key))
    {
        pos_map[key].push_back(rest);
    }
    else
    {
        vector<string> bob = vector<string>();
        bob.push_back(rest);
        pos_map.insert({key, bob});
    }
}

void Automat::add_negative_example(string example)
{
    // negative_examples_v.push_back(example);
    string key = example.substr(0, example_length - sqrt_l);
    string rest = example.substr(example_length - sqrt_l, sqrt_l);

    if (neg_map.count(key))
    {
        neg_map[key].push_back(rest);
    }
    else
    {
        vector<string> bob = vector<string>();
        bob.push_back(rest);
        neg_map.insert({key, bob});
    }
}

void Automat::todo()
{
    for (auto ex : pos_map)
    {
        cout << ex.first << ": " << endl;
        for (auto i : ex.second)
        {
            cout << i << endl;
        }
    }
    cout << "--------" << endl;
    for (auto ex : neg_map)
    {
        cout << ex.first << ": " << endl;
        for (auto i : ex.second)
        {
            cout << i << endl;
        }
    }
    cout << "------" << endl;
    for (auto ex : skip_to)
    {
        cout << ex.first << ": ";
        for (auto i : ex.second)
        {
            cout << i << ", ";
        }
        cout << endl;
    }
}

void Automat::addItems(vector<int> state)
{
    automat.push_back(state);
}

void Automat::run()
{
    int curr;
    map<int, vector<int>> map;

    for (int i = 0; i < states; i++)
    {
        map.insert({i, vector<int>()});

        curr = i;

        for (auto g : pos_map)
        {
            for (auto p : g.second)
            {
                curr = skip_to[g.first][i];
                for (auto o : p)
                {
                    curr = automat[curr][alphabetConverter(o)];
                }
                map[i].push_back(curr);
            }
        }

        for (auto g : neg_map)
        {
            for (auto p : g.second)
            {
                curr = skip_to[g.first][i];

                for (auto o : p)
                {
                    curr = automat[curr][alphabetConverter(o)];
                }

                if (find(map[i].begin(), map[i].end(), curr) != map[i].end())
                {
                    map[i].clear();
                    goto here;
                }
            }
        }
    here:;
    }

    for (auto it : map)
    {
        sort(it.second.begin(), it.second.end());
        it.second.erase(unique(it.second.begin(), it.second.end()), it.second.end());

        if (it.second.size() == this->final_states)
        {
            cout << it.first << " ";
            for (auto fs : it.second)
            {
                cout << fs << " ";
            }
            cout << endl;
        }
    }
    // cout << "1" << endl;
}

void Automat::run_skip_to()
{

    for (auto prefix : pos_map)
    {
        if (skip_to.count(prefix.first))
        {
            continue;
        }
        else
        {
            int curr;
            skip_to.insert({prefix.first, vector<int>()});

            for (int i = 0; i < states; i++)
            {
                curr = i;

                for (auto g : prefix.first)
                {

                    curr = automat[curr][alphabetConverter(g)];
                }
                skip_to[prefix.first].push_back(curr);

                curr = i;
            }
        }
    }
    for (auto prefix : neg_map)
    {
        if (skip_to.count(prefix.first))
        {
            continue;
        }
        else
        {
            int curr;
            skip_to.insert({prefix.first, vector<int>()});

            for (int i = 0; i < states; i++)
            {
                curr = i;

                for (auto g : prefix.first)
                {

                    curr = automat[curr][alphabetConverter(g)];
                }
                skip_to[prefix.first].push_back(curr);

                curr = i;
            }
        }
    }
}

int main()
{
    ios::sync_with_stdio(false);
    int states, alphabet, final_states, positive_examples, negative_examples, example_length;

    std::cin >> states >> alphabet >> final_states >> positive_examples >> negative_examples >> example_length;

    Automat a(states, alphabet, final_states, positive_examples, negative_examples, example_length);

    int x, y;
    vector<int> n_states;

    for (int i = 0; i < states; i++)
    {
        n_states.clear();
        std::cin >> x;
        for (int j = 0; j < alphabet; j++)
        {
            std::cin >> y;
            n_states.push_back(y);
        }
        a.addItems(n_states);
    }

    string example;

    for (int i = 0; i < positive_examples; i++)
    {
        cin >> example;
        a.add_positive_example(example);
    }

    for (int i = 0; i < negative_examples; i++)
    {
        cin >> example;
        a.add_negative_example(example);
    }
    a.run_skip_to();
    a.run();

    // a.todo();

    return 0;
}