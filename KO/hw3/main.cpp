#include <stdio.h>
#include <iostream>
#include <vector>
#include <climits>
#include <algorithm>
#include <stack>
#include <queue>
#include <map>
#include <fstream>
#include <sstream>
#include <string>

using namespace std;

class Edge
{
public:
    int dest;
    int source;
    int weight;
    int lower_bound;
    int upper_bound;
    bool is_forward;
    Edge *counterpart;

    Edge(int src, int des, int wt, int lower_bound, int upper_bound, bool fwd)
    {
        this->source = src;
        this->dest = des;
        this->lower_bound = lower_bound;
        this->upper_bound = upper_bound;
        this->weight = wt;
        this->is_forward = fwd;
        this->counterpart = NULL;
    }

    void set_couterpart(Edge *n)
    {
        counterpart = n;
    }
};

class Graph
{
private:
    vector<Edge *> *adjacency_list;

public:
    int n;
    int customers;
    int products;

    Graph(int vertices, int customers, int products)
    {
        this->n = vertices;
        this->customers = customers;
        this->products = products;
        this->adjacency_list = new vector<Edge *>[n];
    }

    vector<Edge *> getNeighbours(int v)
    {
        return adjacency_list[v];
    }

    void addEdge(int src, int dest,  int weight, int lower_bound, int upper_bound)
    {
        Edge *N_fwd = new Edge(src, dest, weight, lower_bound, upper_bound, true);
        Edge *N_rev = new Edge(dest, src, 0, lower_bound, upper_bound, false);
        N_fwd->set_couterpart(N_rev);
        N_rev->set_couterpart(N_fwd);
        adjacency_list[src].push_back(N_fwd);
        adjacency_list[dest].push_back(N_rev);
    }

    void write_output(const char * path)
    {
        vector<vector<int>> result(customers + 1);

        for (int i = customers + 1; i <= customers + products; i++)
        {
            for (auto it : adjacency_list[i])
            {
                if (!it->is_forward)
                {
                    if (it->weight > 0)
                    {
                        result[it->dest].push_back(i - customers);
                    }
                }
            }
        }

        // Create and open a text file
        ofstream output_file(path);
        for(int i = 1; i < result.size(); i++)
        {
            for (auto i : result[i])
            {
                output_file << i << " ";
            }
            output_file << endl;
        }

        output_file.close();
    }
};

vector<Edge *> find_path_BFS(Graph &graph, int source, int sink)
{
    vector<Edge *> path;
    queue<int> to_visit;
    bool visited[graph.n];
    Edge *parent[graph.n];

    for (int i = 0; i < graph.n; i++)
    {
        visited[i] = false;
    }

    visited[source] = true;
    to_visit.push(source);

    while (!to_visit.empty() && !visited[sink])
    {
        auto next_vertex = to_visit.front();
        to_visit.pop();

        for (auto out_edge : graph.getNeighbours(next_vertex))
        {
            if (!visited[out_edge->dest] && out_edge->weight > 0)
            {
                to_visit.push(out_edge->dest);
                visited[out_edge->dest] = true;
                parent[out_edge->dest] = out_edge;
            }
        }
    }

    if (!visited[sink])
    {
        return path;
    }

    int current = sink;
    while (current != source)
    {
        path.push_back(parent[current]);
        current = parent[current]->source;
    }

    return path;
}

void max_flow(Graph &graph, int source, int sink)
{
    auto path = find_path_BFS(graph, source, sink);

    while (!path.empty())
    {
        int delta = INT32_MAX;
        for (auto edge : path)
        {
            delta = min(delta, edge->weight);
        }
        for (auto edge : path)
        {
            edge->counterpart->weight += delta;
            edge->weight -= delta;
        }
        path = find_path_BFS(graph, source, sink);
    }
}

Graph read_file(const char * path)
{
    int customers, products;
    ifstream indata;
    indata.open(path);

    string line;
    std::getline(indata, line);
    istringstream iss(line);
    iss >> customers >> products;

    int graph_size = customers + products + 2;

    Graph graph(graph_size, customers, products);

    // add customers and product edges
    for (int i = 0; i < customers; i++)
    {
        int lower_bound, upper_bound;
        getline(indata, line);
        istringstream iss(line);
        iss >> lower_bound >> upper_bound;
        graph.addEdge(0, i + 1, upper_bound, lower_bound, upper_bound);

        int val;
        while (iss >> val)
        {
            graph.addEdge(i + 1, val + customers, 1, 0, 1);
        }
    }

    getline(indata, line);
    istringstream pr(line);
    // add products to end
    for (int i = 1; i <= products; i++)
    {
        int val;
        pr >> val;
        graph.addEdge(i + customers, graph_size - 1, customers, val, customers);
    }

    return graph;
}

Graph create_init_graph(const char * path){
    int customers, products;
    ifstream indata;
    indata.open(path);

    string line;
    std::getline(indata, line);
    istringstream iss(line);
    iss >> customers >> products;

    int graph_size = customers + products + 2;

    Graph graph(graph_size + 2, customers, products);

    // add customers and product edges
    for (int i = 0; i < customers; i++)
    {
        int lower_bound, upper_bound;
        getline(indata, line);
        istringstream iss(line);
        iss >> lower_bound >> upper_bound;

        auto new_bound = upper_bound - lower_bound;
        graph.addEdge(0, i + 1, new_bound, 0, new_bound);

        int val;
        while (iss >> val)
        {
            graph.addEdge(i + 1, val + customers, 1, 0, 1);
        }
    }

    getline(indata, line);
    istringstream pr(line);

    // add products to end
    for (int i = 1; i <= products; i++)
    {
        int val;
        pr >> val;
        auto new_bound = customers - val;
        graph.addEdge(i + customers, graph_size - 1, new_bound, 0, new_bound);
    }

    return graph;
}

void add_edges_with_apostrophs(Graph &graph, vector<int> &balances){
    auto s_apostrof = graph.n - 2;
    auto t_apostrof = graph.n - 1;
    
    for (int i = 0; i < balances.size(); i++){
        auto b = balances[i];
        if(b > 0){
            graph.addEdge(s_apostrof, i, b, 0, b);
        }else if(b < 0){
            graph.addEdge(i, t_apostrof, -b, 0, -b);
        }
    }
}
bool is_graph_saturated(Graph &graph, int s_apostrof){
        for (int i = 0; i < graph.n; i++)
        {
            for (auto it : graph.getNeighbours(i))
            {
                if (!it->is_forward && it->dest == s_apostrof)
                {
                    if(it->upper_bound != it->weight){
                        return false;
                    }
                }
            }
        }
        return true;
}

void add_flow_from_init_to_original_graph(Graph &initial, Graph &original){
    int n = original.n;

    for (int i = 0; i < n; i++){
        auto init = initial.getNeighbours(i);
        auto org =  original.getNeighbours(i);

        for(int j = 0; j < org.size(); j++){
            auto o = org[j];
            auto in = init[j];
           
            if(in->is_forward){
                int origin_weight = o->upper_bound - o->lower_bound;
                int flow_in_init = origin_weight - in->weight;
                o->weight = o->weight - flow_in_init - o->lower_bound;
            }
            else{
                o->weight = in->weight + o->lower_bound;
            }
        }
    }
}

bool edit_init_graph(Graph &graph, Graph &original){
    // add edge between end and start
    graph.addEdge(graph.n-3, 0, INT32_MAX, 0, INT32_MAX);
    auto s_apostrof = graph.n - 2;
    auto t_apostrof = graph.n - 1;

    vector<int> balances(graph.n, 0);

    for(int i = 0; i < original.n; i++){
        auto balance = 0;
        auto vertez = original.getNeighbours(i);
        for(auto i : vertez){
            i->is_forward ? balance = balance - i->lower_bound : balance = balance + i->lower_bound;
        }
        balances[i] = balance;
    }
    add_edges_with_apostrophs(graph, balances);
    max_flow(graph, s_apostrof, t_apostrof);

    if(is_graph_saturated(graph, s_apostrof)){
        add_flow_from_init_to_original_graph(graph, original);
        return true;
    }
    else{
        return false;
    }
}

void write_minus_one(const char * path){
    ofstream output_file(path);
    output_file << "-1";
    output_file.close();
}


int main(int argc, const char * argv[])
{
    Graph graph = read_file(argv[1]);
    int end_root = graph.products + graph.customers + 1;
    Graph init_graph = create_init_graph(argv[1]);
    bool is_saturated = edit_init_graph(init_graph, graph);

    if(is_saturated){
        max_flow(graph, 0, end_root);
        graph.write_output(argv[2]);
    }else{
        write_minus_one(argv[2]);
    }

    return 0;
}