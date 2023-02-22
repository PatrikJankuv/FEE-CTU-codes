#!/usr/bin/env python3
import gurobipy as g
import sys

if __name__ == "__main__":
    with open(sys.argv[1], mode='r') as f:
        d = list(map(int, f.read().split()))

    n = len(d)
    m = g.Model()

    # kolko ludi zacalo pracovat v hodinu i
    x = m.addVars(n, vtype=g.GRB.INTEGER, name="x", obj=1)
    z = m.addVars(n, vtype=g.GRB.INTEGER, name="z", obj=1)

    for i in range(n):
        m.addConstr(d[i] - g.quicksum(x[(i-j) % 24] for j in range(8)) <= z[i])
        m.addConstr(g.quicksum(x[(i-j) % 24] for j in range(8)) - d[i] <= z[i])
        m.addConstr(z[i] >= 0)

    m.optimize()

    count = 0
    x_out = ''

    for i in range(n):
        count += round(z[i].x)
        x_out += str(round(x[i].X))
        x_out += " "

    with open(sys.argv[2], mode='w') as f:
        f.write(f'{count}\n')
        f.write(f'{x_out}\n')
