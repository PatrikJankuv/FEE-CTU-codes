class Node:
    def __init__(self, nextNode, prevNode, data):
        self.nextNode = nextNode
        self.prevNode = prevNode
        self.data = data

class LinkedList:
    def __init__(self):
        self.head = None
        self.count = 0

    def push(self, auto):
        if self.head == None:
            self.head = Node(None, None, auto)
            self.count += 1
        else:
            self.head = Node(self.head, None, auto)
            self.head.nextNode.prevNode = self.head
            self.count += 1

            while self.head.nextNode and (self.head.data.price >= self.head.nextNode.data.price):
                self.head.data, self.head.nextNode.data = self.head.nextNode.data, self.head.data
                self.head = self.head.nextNode

            while  self.head.prevNode is not None:  #navratenie na zaciatok
                self.head=self.head.prevNode

    def updateName(self, id, newName):
        temp = self.head
        while temp.data.identification is not id and temp.nextNode is not None:
            temp = temp.nextNode

        if temp.data.identification is id:
            temp.data.name = newName

        while  self.head.prevNode is not None:  #navratenie na zaciatok
            self.head=self.head.prevNode

    def updateBrand(self, id, newBrand):
        temp = self.head
        while temp.data.identification is not id and temp.nextNode is not None:
            temp = temp.nextNode

        if temp.data.identification is id:
            temp.data.brand = newBrand

        while  self.head.prevNode is not None:  #navratenie na zaciatok
            self.head=self.head.prevNode

    def activateCar(self,id):
        temp = self.head

        while temp.data.identification is not id and temp.nextNode is not None:
            temp = temp.nextNode

        if temp.data.identification is id:
            temp.data.active = True

        while  self.head.prevNode is not None:  #navratenie na zaciatok
            self.head=self.head.prevNode

    def deactivateCar(self, id):
        temp = self.head

        while temp.data.identification is not id and temp.nextNode is not None:
            temp = temp.nextNode

        if temp.data.identification is id:
            temp.data.active = False

        while  self.head.prevNode is not None:  #navratenie na zaciatok
            self.head=self.head.prevNode

    def calculateCarPrice(self):
        temp = self.head
        sum = 0

        while temp is not None:
            if temp.data.active:
                sum += temp.data.price
            temp = temp.nextNode

        while  self.head.prevNode is not None:  #navratenie na zaciatok
            self.head=self.head.prevNode



class Car:
    def __init__(self, identification, name, brand, price, active):
        self.identification = identification
        self.name = name
        self.brand = brand
        self.price = price
        self.active = active


db = LinkedList()

def add(car):
    db.push(car)

def init(car):
    for i in range(len(car)):
        add(car[i])

def updateName(identification, name):
    db.updateName(identification, name)


def updateBrand(identification, brand):
    db.updateBrand(identification, brand)


def activateCar(identification):
    db.activateCar(identification)


def deactivateCar(identification):
    db.deactivateCar(identification)

def getDatabaseHead():
    return db.head

def getDatabase():
    return db

def calculateCarPrice():
    db.calculateCarPrice()


def clean():
    db.head = None
