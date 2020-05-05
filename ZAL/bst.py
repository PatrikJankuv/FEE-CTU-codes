class Node:
    def __init__(self, value, smaller, bigger):
        self.value = value
        self.smaller = None
        self.bigger = None



class BinarySearchTree:
    def __init__(self):
        self.head = None
        self.notes = 1


    def insert(self, value):
        if value is not None:
            if self.head is None:
                self.head = Node(value, None, None)

            else:
                start = self.head

                while self.head is not None:
                    prevNode = self.head
                    if value < self.head.value:
                        self.head = self.head.smaller
                        help = 1
                    else:
                        self.head = self.head.bigger
                        help = 2


                self.head = Node(value, None, None)

                if help == 1:
                    prevNode.smaller = self.head
                elif help == 2:
                    prevNode.bigger = self.head

                self.head = start

    def fromArray(self, array):
        for i in range(len(array)):
            self.insert(int(array[i]))

    def search(self, value):
        self.notes = 1
        start = self.head

        if value == self.head.value:
            return True
        else:
            while self.head.value != value:
                if self.head.value < value and self.head.bigger is not None:
                    self.head = self.head.bigger
                    self.notes += 1
                elif self.head.value > value and self.head.smaller is not None:
                    self.head = self.head.smaller
                    self.notes += 1
                else:
                    break

        if value == self.head.value:
            self.head = start
            return True
        else:
            self.head = start
            return False


    def min(self):
        self.notes = 1
        start = self.head

        while self.head.smaller is not None:
            self.head = self.head.smaller
            self.notes +=1

        minValue = self.head.value
        self.head = start

        return minValue


    def max(self):
        self.notes = 1
        start = self.head

        while self.head.bigger is not None:
            self.head = self.head.bigger
            self.notes +=1

        maxValue = self.head.value
        self.head = start

        return maxValue

    def visitedNodes(self):
        return self.notes
