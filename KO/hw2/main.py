#!/usr/bin/env python3

import gurobipy as g
import numpy as np
from itertools import permutations, combinations
import sys


def distance(A, B):
    A = np.array(A[:, -3:])
    B = np.array(B[:, :3])
    return np.sum(np.absolute(A - B))


def sub_sub_tour(edges):
    start = edges[0][0]
    temp = edges[0][1]

    tour = []
    tour.append(edges[0])
    edges.remove(edges[0])

    while True:
        for edge in edges:
            if edge[0] == temp:
                tour.append(edge)
                if edge[1] == start:
                    edges.remove(edge)
                    return tour, edges

                temp = edge[1]
                edges.remove(edge)
                break


def subtour(edges):
    short_tour = edges[:]

    while len(edges) > 1:
        tour, edges = sub_sub_tour(edges)
        if len(tour) < len(short_tour):
            short_tour = tour
    return short_tour


def subtourelim(model, where):
    if where == g.GRB.callback.MIPSOL:
        vals = model.cbGetSolution(model._vars)
        selected = g.tuplelist((i, j)
                               for i, j in model._vars.keys()if vals[i, j] > 0.5)

        tour = subtour(selected)
        if len(tour) < n:
            expr = g.quicksum(model._vars[tour[i][0], tour[i][1]]
                              for i in range(len(tour)))
            model.cbLazy(expr <= len(tour)-1)


if __name__ == "__main__":

    f = open(sys.argv[1])

    data = []

    for line in f:
        data_line = []
        for j in line.split(' '):
            data_line.append(j)
        data.append(data_line)

    n = int(data[0][0])
    w = int(data[0][1])
    h = int(data[0][2])

    picture = []
    piece = []

    for i in range(0, n):
        piece = np.asarray(data[i+1])
        piece = np.resize(piece, (h, w*3))
        picture.append(piece)

    picture = np.array(picture)
    picture = picture.astype(int)

    m = g.Model()

    vars = {}

    for p in permutations(np.arange(n), 2):
        dist = distance(picture[p[0]], picture[p[1]])
        vars[p[0], p[1]] = m.addVar(
            obj=dist, vtype=g.GRB.BINARY, name=str(p[0]) + '-' + str(p[1]))

    for i in range(n):
        vars[i, i] = m.addVar(obj=0, vtype=g.GRB.BINARY, ub=0,
                              name=str(i) + '-' + str(i))
        vars[n, i] = m.addVar(obj=0, vtype=g.GRB.BINARY,
                              name=str(n) + '-' + str(i))
        vars[i, n] = m.addVar(obj=0, vtype=g.GRB.BINARY,
                              name=str(i) + '-' + str(n))

    vars[n, n] = m.addVar(obj=0, ub=0, vtype=g.GRB.BINARY,
                          name=str(n) + '-' + str(n))
    m.update()

    for j in range(n + 1):
        m.addConstr(g.quicksum(vars[i, j] for i in range(n + 1)) == 1)

    for i in range(n + 1):
        m.addConstr(g.quicksum(vars[i, j] for j in range(n + 1)) == 1)

    m._vars = vars
    m.params.LazyConstraints = 1
    m.optimize(subtourelim)
    m.write('m.lp')

    path = []

    for i in vars:
        if(vars[i].X == 1):
            path.append(i)

    temp = n
    x_out = ''
    for i in path:
        if (i[0] == temp):
            temp = i[1]

    while temp != n:
        for i in path:
            if(i[0] == temp):
                x_out += str(temp+1) + " "
                temp = i[1]
                break

    print(sys.argv[2])
    with open(sys.argv[2], mode='w') as f:
        f.write(f'{x_out}')
