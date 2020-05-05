import math

def newtonPi(init):
    xk = init
    condition = True
    while condition:
        xkplusone = xk - (math.sin(xk)/math.cos(xk))
        condition = xk != xkplusone
        xk = xkplusone
    return xkplusone




def leibnizPi(length):
    pom = 0
    for i in range(length):
        if (i+2) % 2 == 0:          #(i+2) potrebne lebo i sa zacina od 0
            minus = 4/((i*2)+1)     #(i+2) potrebne lebo i sa zacina od 0, +1 ide vzdy o neparne cislo
            pom = pom + minus
        else:
            plus = 4/((i*2)+1)      #to iste ako hore
            pom = pom - plus

    return pom


def nilakanthaPi(length):
    pom = 3.0
    citatel = 2
    for i in range(length - 1):
        if (i+2) % 2 == 0:          #(i+2) potrebne lebo i sa zacina od 0
            plus = 4 / (citatel * (citatel+1) * (citatel+2))
            pom = pom + plus
            citatel = citatel + 2   #potrebne navysit pre dalsi clen, ktory ma o 2 vacsie menovatele
        else:
            minus = 4 / (citatel * (citatel+1) * (citatel+2))
            pom = pom - minus
            citatel = citatel + 2


    return pom


print(leibnizPi(3))
