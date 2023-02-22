#include <iostream>
#include <vector>
#include <climits>
#include <algorithm>

using namespace std;
// int parent[5];
vector<int> parent;

// Find set of vertex i
int find(int i)
{
    while (parent[i] != i)
        i = parent[i];
    return i;
}

// Does union of i and j. It returns
// false if i and j are already in same
// set.
void union1(int i, int j)
{
    int a = find(i);
    int b = find(j);
    parent[a] = b;
}

// Finds MST using Kruskal's algorithm
void kruskalMST(vector<vector<int>> cost, int V, vector<vector<int>> &mst_edges)
{

    // Initialize sets of disjoint sets.
    for (int i = 0; i < V; i++)
        parent[i] = i;

    // Include minimum weight edges one by one
    int edge_count = 0;
    while (edge_count < V - 1)
    {
        int min = INT_MAX, a = -1, b = -1;
        for (int i = 0; i < V; i++)
        {
            for (int j = 0; j < V; j++)
            {
                if (find(i) != find(j) && cost[i][j] < min && cost[i][j] > 0)
                {
                    min = cost[i][j];
                    a = i;
                    b = j;
                }
            }
        }

        union1(a, b);

        // printf("Edge %d:(%d, %d) cost:%d \n",
        //        edge_count++, a, b, min);

        edge_count++;
        if (min < INT_MAX)
            mst_edges.push_back({a, b, min});
    }
}

int main_kruska(vector<vector<int>> graph, size_t V, vector<vector<int>> &mst_edges)
{
    parent.reserve(V);
    kruskalMST(graph, V, mst_edges);

    // for (auto i : mst_edges)
    // {
    //     printf("Edge :(%d, %d) cost:%d \n",
    //            i[0], i[1], i[2]);
    // }
    return 0;
}
int minDistance(int dist[], bool sptSet[], int V)
{
    int min = INT_MAX, min_index;

    for (int v = 0; v < V; v++)
        if (sptSet[v] == false && dist[v] <= min)
            min = dist[v], min_index = v;

    return min_index;
}

void print_solution(int dist[], int V)
{
    cout << "Distances \n";
    for (int i = 0; i < V; i++)
        cout << i << " " << dist[i] << "\n";
}

void dijkstra(vector<vector<int>> graph, int src, size_t V, int dist[])
{
    // int dist[V];
    bool sptSet[V];

    for (int i = 0; i < V; i++)
        dist[i] = INT_MAX, sptSet[i] = false;

    dist[src] = 0;

    for (int count = 0; count < V - 1; count++)
    {
        int u = minDistance(dist, sptSet, V);

        sptSet[u] = true;

        for (int v = 0; v < V; v++)
        {
            if (!sptSet[v] && graph[u][v] && dist[u] != INT_MAX && dist[u] + graph[u][v] < dist[v])
            {
                dist[v] = dist[u] + graph[u][v];
            }
        }
    }
}

struct Road
{
    int farm_one, farm_two, distance;

    bool operator<(const Road &road1) const
    {
        return distance < road1.distance;
    }
};

ostream &operator<<(ostream &out, const Road &road)
{
    out << road.farm_one << " <" << road.distance << "> " << road.farm_two << "\n";
    return out;
}

class Graph
{
public:
    int vertexes_count, egdes_count;

    std::vector<Road> edges;

    void add_edge(int one, int two, int dst)
    {
        edges.push_back({one, two, dst});
    }

    void print_graph()
    {
        for (auto i : edges)
        {
            std::cout << i;
        }
    }
};

void print_graph_table(vector<vector<int>> table, size_t V)
{
    std::cout << "\n";

    for (int i = 0; i < V; i++)
    {
        for (int j = 0; j < V; j++)
        {
            std::cout << table[i][j] << ", ";
        }
        std::cout << "\n";
    }
}

bool is_undicided(std::vector<int> &input)
{
    std::sort(input.begin(), input.end());
    return input[0] == input[1];
}

void remove_undecided_farms(vector<vector<int>> &graph, vector<int> undecided, int num_farms)
{
    for (auto i : undecided)
    {
        for (int j = 0; j < num_farms; j++)
        {
            graph[j][i] = 0;
            graph[i][j] = 0;
        }
    }
}

int eliminita_undecided_farms(vector<vector<int>> &graph, int num_farms, int hubs[], int num_hubs)
{
    // vector<vector<int>> distances(num_hubs, vector<int>(num_farms, 0));
    int distances[num_hubs][num_farms];

    for (int i = 0; i < num_hubs; i++)
    {
        dijkstra(graph, hubs[i], num_farms, distances[i]);
    }

    // print in table
    // for (int i = 0; i < num_hubs; i++)
    // {
    //     cout << hubs[i] << " : ";
    //     for (int j = 0; j < num_farms; j++)
    //     {
    //         cout << j << ":" << distances[i][j] << ", ";
    //     }
    //     cout << "\n";
    // }

    //transform intu vector
    vector<vector<int>> transform(num_farms, vector<int>(num_hubs));
    for (int i = 0; i < num_farms; i++)
        for (int j = 0; j < num_hubs; j++)
        {
            transform[i][j] = distances[j][i];
        }

    vector<int> undecided;

    for (int i = 1; i < num_farms; i++)
    {
        if (is_undicided(transform[i]))
            undecided.push_back(i);
    }

    remove_undecided_farms(graph, undecided, num_farms);

    return undecided.size();
    // for (int i = 0; i < num_farms; i++)
    // {
    //     cout << i << " | ";
    //     for (int j = 0; j < num_hubs; j++)
    //     {
    //         cout << hubs[j] << ":" << transform[i][j] << ", ";
    //     }
    //     cout << " " << is_undicided(transform[i]) << "\n";
    // }

    // // print undecided
    // cout << "undecided: ";
    // for (auto item : undecided)
    // {
    //     cout << item << ", ";
    // }
    // cout << '\n';
}

int finish_him(vector<vector<int>> &graph, int num_farms, int hubs[], int num_hubs, vector<vector<int>> &mst_edge)
{
    // vector<vector<int>> distances(num_hubs, vector<int>(num_farms, 0));
    int distances[num_hubs][num_farms];

    for (int i = 0; i < num_hubs; i++)
    {
        dijkstra(graph, hubs[i], num_farms, distances[i]);
    }

    // for (int i = 0; i < num_hubs; i++)
    // {
    //     cout << hubs[i] << " : ";
    //     for (int j = 0; j < num_farms; j++)
    //     {
    //         cout << j << ":" << distances[i][j] << ", ";
    //     }
    //     cout << "\n";
    // }

    vector<int> nearest(num_farms);

    for (int i = 0; i < num_farms; i++)
    {
        int min = INT_MAX;
        int temp = -1;
        for (int j = 0; j < num_hubs; j++)
        {
            if (distances[j][i] < min)
            {
                min = distances[j][i];
                temp = hubs[j];
            }
        }
        nearest[i] = temp;
    }

    int distance = 0;

    for (auto item : mst_edge)
    {
        if (nearest[item[0]] == nearest[item[1]])
        {
            distance = distance + item[2];
        }
    }

    // for (int i = 1; i < num_farms; i++)
    // {
    //     if (parent[i] > -1 && (nearest[i] == nearest[parent[i]]))
    //     {
    //         distance = distance + graph[i][parent[i]];
    //     }
    // }

    // for (int i = 0; i < num_farms; i++)
    // {
    //     cout << i << "I" << nearest[i] << ", ";
    // }
    return distance;
}

int main()
{
    std::ios::sync_with_stdio(false);

    int num_farms, num_roads;

    std::cin >> num_farms >> num_roads;

    vector<vector<int>> graph_table(num_farms, vector<int>(num_farms, 0));

    int x, y, z;

    // get edges
    // Graph *graph = new Graph();

    for (int i = 0; i < num_roads; i++)
    {
        std::cin >> x >> y >> z;

        // graph->add_edge(x, y, z);

        graph_table[x][y] = z;
        graph_table[y][x] = z;
    }

    int num_hubs;

    std::cin >> num_hubs;
    int hubs[num_hubs];
    // get hubs
    for (int i = 0; i < num_hubs; i++)
    {
        std::cin >> hubs[i];
    }

    // print_graph_table(graph_table, num_farms);
    int un_farms = eliminita_undecided_farms(graph_table, num_farms, hubs, num_hubs);
    // print_graph_table(graph_table, num_farms);

    //get mst
    vector<vector<int>> mst_edges;
    main_kruska(graph_table, num_farms, mst_edges);

    // int parent[num_farms];
    // prims(graph_table, num_farms, parent);

    vector<vector<int>> min_tree_graph(num_farms, vector<int>(num_farms, 0));

    // cout << "Edge \tWeight\n";
    // for (int i = 1; i < num_farms; i++)
    // {
    //     cout << parent[i] << " - " << i << " \t" << graph_table[i][parent[i]] << " \n";
    // }

    // for (int i = 1; i < num_farms; i++)
    // {
    //     if (parent[i] > -1)
    //     {
    //         min_tree_graph[parent[i]][i] = graph_table[i][parent[i]];
    //         min_tree_graph[i][parent[i]] = graph_table[i][parent[i]];
    //     }
    // }

    for (auto item : mst_edges)
    {
        min_tree_graph[item[0]][item[1]] = item[2];
        min_tree_graph[item[1]][item[0]] = item[2];
    }

    int distance = finish_him(min_tree_graph, num_farms, hubs, num_hubs, mst_edges);

    distance = 0;
    for (auto item : mst_edges)
    {
        // if (nearest[item[0]] == nearest[item[1]])
        // {
        distance = distance + item[2];
        // }
    }

    std::cout << distance << " " << un_farms << endl;

    return 0;
}