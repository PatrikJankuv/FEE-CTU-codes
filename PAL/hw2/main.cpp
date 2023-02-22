//hw2
#include <stdio.h>
#include <iostream>
#include <list>
#include <vector>
#include <climits>
#include <algorithm>
#include <stack>

using namespace std;

struct Edge
{
    int src, distance;
};

struct Vertex
{
    int name;
    int parent;
};

struct Note
{
    int parent;
    int flag;
    int distance_to_centre;
    bool is_source;
    int component;
};

class Graph
{
    int V, E, C;
    int component_index;

    vector<vector<int>> followers;
    vector<vector<int>> new_followers;

    vector<vector<Edge>> distances;
    vector<vector<int>> sccs;

    vector<Note> notes;
    vector<int> sources;

public:
    void DFS(int source, vector<bool> &visited);
    void BFS(int source);
    void SCCUtil(int u, int disc[], int low[],
                 stack<int> *st, bool stackMember[]);
    void solveProblem();
    void convertGraph();

    Graph(int V, int E, int C);
    void addEdge(int v, int w);
    void addDistance(int v, int w);
    void addEditedEdges(int v, int w);
};

void Graph::DFS(int source, vector<bool> &visited)
{
    stack<int> toVisit;
    vector<int> front;
    // vector<bool> visited = vector<bool>(V, false);

    if (!visited[source])
    {
        // cout << "push " << v << endl;
        sources.push_back(source);
        // visited[v] = v;
        notes[source].flag = source;
        notes[source].is_source = true;
    }

    toVisit.push(source);

    while (!toVisit.empty())
    {
        int v = toVisit.top();
        toVisit.pop();
        if (!visited[v])
        {
            visited[v] = true;
            front.push_back(v);

            if (v == C)
            {
                // cout << v << endl;
                continue;
            }

            for (int n : new_followers[v])
            {
                if (!visited[n])
                {
                    notes[n].parent = source;
                    toVisit.push(n);
                }
                else
                {
                    //add new vertex to source
                    if (notes[n].parent == -1 && source != notes[n].flag)
                    {
                        sources.erase(remove(sources.begin(), sources.end(), n), sources.end());
                        notes[n].is_source = false;
                    }
                    else if (notes[n].parent != source)
                    {
                        visited[n] = false;
                        notes[n].parent = source;
                        toVisit.push(n);

                        if (notes[n].is_source && n != source)
                        {
                            // cout << n << " je to zdroj " << source << endl;
                            sources.erase(remove(sources.begin(), sources.end(), n), sources.end());
                            notes[n].is_source = false;
                        }
                    }
                }
            }
        }
    }

    // for (auto i : front)
    // {
    //     cout << i << ", ";
    // }
    // cout << endl;
};

void Graph::BFS(int source)
{
    bool *visited = new bool[V];

    for (int i = 0; i < V; i++)
    {
        visited[i] = false;
    }

    list<Edge> queue;

    notes[source].distance_to_centre = 0;
    queue.push_back({source, 0});

    while (!queue.empty())
    {
        Edge s = queue.front();

        queue.pop_front();

        if (!visited[s.src])
        {
            visited[s.src] = true;
            for (auto n : distances[s.src])
            {
                if (notes[n.src].distance_to_centre > notes[s.src].distance_to_centre + n.distance)
                {
                    notes[n.src].distance_to_centre = notes[s.src].distance_to_centre + n.distance;
                    visited[n.src] = false;
                }
                // notes[n.src].distance_to_centre = notes[s.src].distance_to_centre + n.distance;
                queue.push_back(n);
            }
        }
        // if (notes[s.src].distance_to_centre >)
    }
}

Graph::Graph(int V, int E, int C)
{
    this->V = V;
    this->E = E;
    this->C = C;
    this->component_index = 0;

    this->followers = vector<vector<int>>(V);
    // this->distances = vector<vector<Edge>>(V);
    this->notes = vector<Note>(V, {-1, -1, INT_MAX});
}

void Graph::addEdge(int v, int w)
{
    followers[v].push_back(w);

    // distances[v].push_back({w, 0});
    // distances[w].push_back({v, 1});
}

void Graph::addEditedEdges(int v, int w)
{
    new_followers[v].push_back(w);
    distances[v].push_back({w, 0});
    distances[w].push_back({v, 1});
}

void Graph::convertGraph()
{
    this->new_followers = vector<vector<int>>(component_index);
    this->distances = vector<vector<Edge>>(component_index);

    for (auto i : sccs)
    {
        // cout << i[0];
        for (auto v : i)
        {
            // cout << v;
            for (auto n : followers[v])
            {
                // cout << n << " . " << notes[n].component << endl;
                //
                if (notes[n].component != notes[v].component)
                {
                    // cout << v << " . " << n << endl;
                    // cout << "push somethibg " << notes[v].component << " " << notes[n].component << endl;
                    addEditedEdges(notes[v].component, notes[n].component);
                }
            }
        }
        // cout << endl;
    }

    this->C = notes[C].component;
    this->notes = vector<Note>(component_index, {-1, -1, INT_MAX});
}

void Graph::SCCUtil(int u, int disc[], int low[], stack<int> *st,
                    bool stackMember[])
{
    static int time = 0;
    disc[u] = low[u] = ++time;
    st->push(u);
    stackMember[u] = true;

    for (auto v : followers[u])
    {
        if (disc[v] == -1)
        {
            SCCUtil(v, disc, low, st, stackMember);
            low[u] = min(low[u], low[v]);
        }
        else if (stackMember[v] == true)
            low[u] = min(low[u], disc[v]);
    }

    int w = 0;
    vector<int> component;

    if (low[u] == disc[u])
    {
        while (st->top() != u)
        {
            w = (int)st->top();
            component.push_back(w);
            notes[w].component = this->component_index;

            stackMember[w] = false;
            st->pop();
        }
        w = (int)st->top();
        component.push_back(w);
        notes[w].component = this->component_index;

        stackMember[w] = false;
        st->pop();
    }

    if (component.size() > 0)
    {
        sccs.push_back(component);
        this->component_index++;
    }
}

void Graph::solveProblem()
{
    int *disc = new int[V];
    int *low = new int[V];
    bool *stackMember = new bool[V];
    stack<int> *st = new stack<int>();

    for (int i = 0; i < V; i++)
    {
        disc[i] = -1;
        low[i] = -1;
        stackMember[i] = false;
    }

    for (int i = 0; i < V; i++)
        if (disc[i] == -1)
            SCCUtil(i, disc, low, st, stackMember);

    //print vector
    convertGraph();

    vector<bool> visited = vector<bool>(component_index, false);
    for (int i = 0; i < component_index; i++)
    {
        DFS(i, visited);
    }

    BFS(C);

    int temp = 0;
    for (auto s : sources)
    {
        temp += notes[s].distance_to_centre;
    }

    cout << temp << endl;
}

int main()
{
    ios::sync_with_stdio(false);
    int n, m, c;

    std::cin >> n >> m >> c;

    Graph graph(n, m, c);

    int x, y;

    for (int i = 0; i < m; i++)
    {
        std::cin >> x >> y;
        graph.addEdge(x, y);
    }

    // graph.solveProblem();
    graph.solveProblem();

    return 0;
}