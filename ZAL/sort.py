def sortNumbers(weights, condition):
    pomPole = weights

    if condition == "ASC":
        return asc(pomPole)

    elif condition == "DESC":
        return desc(pomPole)


def sortData(weights, data, condition):
    if len(weights) != len(data):           #podmienka, ktora porovnava dlzku poli
        raise ValueError('Invalid input data')
    elif condition == "ASC":
        return ascCar(weights, data)
    elif condition == "DESC":
        return descCar(weights, data)

def asc(weights):
    pom = 0
    changes = 1             #defnicia, nastavenie na inu ako 0, aby sa vyhli podmienke vo while

    while changes != 0:     # change e na to aby sa opakovalo zoradenie dokial pocet zmen po prejdeni je 0
        changes = 0         #po skonceni cyklu for, ktory prejde celý zoznam raz sa nastavý na 0, aby mohlo od znova pocitat zmeny
        for i in range(len(weights)):       #prejde 1 krat cely zoznam
            if ((i+1) < len(weights)):      #aby index bol v dlske pola, inak hadze error
                if weights[i] > weights[i+1]:       #porovna s vacsim
                    pom = weights[i]
                    weights[i] = weights[i+1]
                    weights[i+1] = pom
                    changes += 1

    return weights

def ascCar(weights, data):
    pom = 0
    changes = 1
    
    while changes != 0:
        changes = 0
        for i in range(len(weights)):
            if ((i+1) < len(weights)):
                if weights[i] > weights[i+1]:

                    pom = weights[i]
                    weights[i] = weights[i+1]
                    weights[i+1] = pom
                    pomData = data[i]
                    data[i] = data[i+1]
                    data[i+1] = pomData
                    changes += 1


    print(weights)

    return data

def desc(weights):      #vid komentare k: def asc(weigths):
    pom = 0
    changes = 1

    while changes != 0:
        changes = 0
        for i in range(len(weights)):
            if ((i+1) < len(weights)):
                if weights[i] < weights[i+1]:
                    pom = weights[i]
                    weights[i] = weights[i+1]
                    weights[i+1] = pom
                    changes += 1

    return weights

def descCar(weights, data):
    pom = 0
    changes = 1

    while changes != 0:
        changes = 0
        for i in range(len(weights)):
            if ((i+1) < len(weights)):
                if weights[i] < weights[i+1]:
                    pom = weights[i]
                    pomData = data[i]
                    weights[i] = weights[i+1]
                    data[i] = data[i+1]
                    weights[i+1] = pom
                    data[i+1] = pomData
                    changes +=1


    return data
