def polyEval(poly, x):
    pom = 0
    for i in range(len(poly)):
        e = i
        pom += memberValue(poly[i], e, x)

    return pom



def polySum(poly1, poly2):
    pomPoly = []
    pollen = arrayComp(poly1, poly2)
    zeroCounter = 0
    for i in range(pollen):
        pom = 0
        pomPoly1 = 0
        pomPoly2 = 0

        if len(poly1) > i:          #podmienka nutna, aby nepadol program, lebo ak je pole mensie nepriradi ziadnu hodnotu a program pradne
            pomPoly1 = poly1[i]
        if len(poly2) > i:          #podmienku priradi na obe lebo nevieme, ktore kedy bude dlhsie
            pomPoly2 = poly2[i]

        pom = pomPoly1 + pomPoly2

        if pom != 0 and zeroCounter == 0:       #zabezpecuje aby sa nezapisovali nulove hodnoty and zeroCounter podmienka potrebna, aby mohla platit aj druha podmienka ked nie je zeroCounter 0
            pomPoly.append(pom)
        elif pom != 0 and zeroCounter > 0:      #ked nie je pom nula ale counterZero zapezpecuje aby zapisao predosle exponenty
            for i in range(zeroCounter):
                pomPoly.append(0)
            pomPoly.append(pom)
            zeroCounter = 0
        elif pom == 0:                          #ak pom je nula zapise do counterZero ze je nula, aby sa mohlo podom dopisat
            zeroCounter += 1

    return pomPoly

def polyMultiply(poly1, poly2):
    pom = 0
    pomPoly = []
    for i in range(len(poly1)):
        for j in range(len(poly2)):
            pom = 0
            pom = poly1[i] * poly2[j]
            if (i+j) < len(pomPoly):           # i+j potrebne lebe nasobenie exponentov je scčítavanie, podmienka: ak je i+j je uz raz nasobene, tak sa pridava iba uz k existujucim
                pomPoly[j+i] += pom
            else:                               #ak este clen neexistuje tak ho vytvorim
                pomPoly.append(pom)

    return pomPoly

def memberValue(poly, sqrtSize, x):   #vypocita hodnotu clena
    return poly * (x**sqrtSize)

def arrayComp(poly1, poly2):        #metoda na porovnavanie dlzky poli a vrati dlhsi
    if len(poly1) > len(poly2):
        return len(poly1)
    elif len(poly2) >= len(poly1):
        return len(poly2)
