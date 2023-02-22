//hw3
#include <stdio.h>
#include <iostream>
#include <vector>
#include <climits>
#include <algorithm>
#include <stack>
#include <queue>
#include <map>

using namespace std;

struct Edge
{
    int src, distance;
};

struct LevelTwo
{
    int vertex;
    int rank;
};

struct Vertex
{
    int name;
    int parent;
};

class Graph
{
    int V, E, C, P;
    vector<vector<int>> graph;
    map<string, vector<vector<int>>> same_shape;
    map<string, string> cycles;
    vector<vector<int>> combinations;
    vector<int> combination;
    vector<vector<int>> result;
    map<int, bool> visited;

public:
    Graph(int V, int E, int P, int C);
    void addEdge(int v, int w);
    void printShapes();
    void permutate();
    void print();
    void printVector(vector<int> vec);
    void neighborsVector();
    void comb(int a, int b);
    void print_combinations();
    void count_cycles();
    void dfs_cycle(int i);
};

bool compInt(int a, int b)
{
    return a < b;
}

bool compLevel(LevelTwo a, LevelTwo b)
{
    return a.rank < b.rank;
}

void Graph::print_combinations()
{
    for (auto c : combinations)
    {
        for (auto i : c)
        {
            cout << i << " ";
        }
        cout << endl;
    }
}

void Graph::comb(int a, int b)
{
    std::string bitmask(b, 1);
    bitmask.resize(a, 0);

    do
    {
        for (int i = 0; i < a; ++i)
        {
            if (bitmask[i])
                // std::cout << " " << i;
                combination.push_back(i);
        }
        // std::cout << std::endl;
        combinations.push_back(combination);
        combination.clear();
    } while (std::prev_permutation(bitmask.begin(), bitmask.end()));
}

void Graph::neighborsVector()
{
    int sub_index = 0;
    // string vector;
    // som kokot, pomenovat vector vector
    vector<LevelTwo> vector;
    int ranks[V];
    auto neighborsVector = std::vector<std::vector<int>>(V);

    for (int i = 0; i < V; i++)
    {
        ranks[i] = 0;
    }

    string shape;

    for (auto sub : combinations)
    {
        // vector = "";
        int sum = 0;
        for (auto s : sub)
        {
            int temp = 0;

            for (int n : graph[s])
            {
                if (binary_search(sub.begin(), sub.end(), n))
                {
                    temp++;
                    sum++;
                    neighborsVector[s].push_back(n);
                }
            }
            vector.push_back({s, temp});

            if (temp == 0)
            {
                // cout << "inconnected" << endl;
                // continue;
                goto cnt;
            }
            ranks[s] = temp;
            // vector += to_string(temp);
        }

        // // cout << endl;

        if (sum == (2 * C))
        {
            // cout << "invalid " << endl;
            std::sort(vector.begin(), vector.end(), compLevel);
            int curr_rank = vector[0].rank;
            std::vector<int> temp_vec;

            for (auto l : vector)
            {
                if (l.rank != curr_rank)
                {
                    std::sort(temp_vec.begin(), temp_vec.end(), compInt);
                    // printVector(temp_vec);
                    // cout << endl;

                    for (auto i : temp_vec)
                    {
                        shape += to_string(i);
                    }
                    // shape += to_string(l.rank);
                    curr_rank = l.rank;
                    temp_vec.clear();
                }

                for (auto n : neighborsVector[l.vertex])
                {
                    temp_vec.push_back(ranks[n]);

                    // cout << "[" << n << "|" << ranks[n] << "], ";
                }
                neighborsVector[l.vertex].clear();
                // }

                // shape += to_string(l.rank);
                curr_rank = l.rank;
            }

            std::sort(temp_vec.begin(), temp_vec.end(), compInt);
            // printVector(temp_vec);
            // cout << endl;

            for (auto i : temp_vec)
            {
                shape += to_string(i);
            }
            temp_vec.clear();

            same_shape[shape].push_back(sub);

            // cout << "-----" << endl;
        }
    cnt:;
        for (auto l : sub)
        {
            neighborsVector[l].clear();
            ranks[l] = 0;
        }
        vector.clear();
        shape = "";
        sub_index++;
        // neighborsVector.clear();
    }
}

Graph::Graph(int V, int E, int P, int C)
{
    this->V = V;
    this->E = E;
    this->C = C;
    this->P = P;

    this->graph = vector<vector<int>>(V);
}

bool compVector(vector<int> a, vector<int> b)
{
    for (int i = 0; i < a.size(); i++)
    {
        if (a[i] == b[i])
        {
            continue;
        }
        else
        {
            return a[i] < b[i];
        }
    }
    return false;
}

void Graph::print()
{
    std::sort(result.begin(), result.end(), compVector);
    for (auto x : result)
    {
        for (auto y : x)
        {
            cout << y << " ";
        }
        cout << endl;
    }
}

void Graph::printVector(vector<int> vector)
{
    for (auto n : vector)
    {
        cout << n << " ";
    }
}

void Graph::permutate()
{
    for (auto shape : same_shape)
    {
        std::sort(shape.second.begin(), shape.second.end(), compVector);

        // shape.second.erase(unique(shape.second.begin(), shape.second.end()), shape.second.end());

        bool temp;

        for (int i = 0; i < shape.second.size(); i++)
        {
            for (int j = i + 1; j < shape.second.size(); j++)
            {
                temp = true;
                for (auto n : shape.second[i])
                {
                    if (std::find(shape.second[j].begin(), shape.second[j].end(), n) != shape.second[j].end())
                    {
                        temp = false;
                    }
                }
                if (temp)
                {
                    vector<int> temp_result(shape.second[j]);
                    vector<int> temp_result2(shape.second[i]);
                    temp_result.insert(temp_result.begin(), temp_result2.begin(), temp_result2.end());
                    result.push_back(temp_result);
                }
            }
        }
    }
}

void Graph::printShapes()
{
    for (auto shape : same_shape)
    {
        cout << "shape " << shape.first << endl;
        for (int i = 0; i < shape.second.size(); i++)
        {
            printVector(shape.second[i]);
            cout << endl;
        }
    }
}

void Graph::addEdge(int v, int w)
{
    graph[v].push_back(w);
    graph[w].push_back(v);
}

int main()
{
    ios::sync_with_stdio(false);
    int n, m, c, p;

    std::cin >> n >> m >> p >> c;

    Graph graph(n, m, p, c);

    int x, y;

    for (int i = 0; i < m; i++)
    {
        std::cin >> x >> y;
        graph.addEdge(x, y);
    }

    graph.comb(n, p);
    graph.neighborsVector();
    // graph.count_cycles();
    // graph.printShapes();
    graph.permutate();
    graph.print();
    return 0;
}