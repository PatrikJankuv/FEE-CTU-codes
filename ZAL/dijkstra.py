class Vertex:
    def __init__(self, id, name):
        self.id = id
        self.name = name
        self.minDistance = float('inf')
        self.previousVertex = None
        self.edges = []
        self.unvisited = True
        self.previousEdges = []

class Edge:
    def __init__(self, source, target, weight):
        self.source = source
        self.target = target
        self.weight = weight


class Dijkstra:
    def __init__(self):
        self.vertexes = []
        self.graph = {} # {} oznacenie pre slovnik


    def computePath(self, sourceId):
        for keys in self.vertexes: #cyklus najde vertex od ktoreho to ma pocitat
            if keys.id == sourceId:
                keys.minDistance = 0
                presDist = keys.minDistance
                self.unvisited = False
                srcEdges = self.graph[sourceId]
                keys.previousVertex = None
                keys.previousEdges.append(keys)
                srcPath = keys.previousEdges
                break

        self.compDist(srcEdges, 0, srcPath)     #volam trieda, ktora vie dopocitat minDistance pre susedne vertexi

    def compDist(self, srcEdge, presDist, srcPath): #dopocita minDistance pre susedne vertexi a vyberie najblizsi vertex
        srcEdges = srcEdge
        minDist = None
        nrstVer = None
        currentId = None

        for i in range(len(srcEdges)):  #prejde vsetky susedne vertexi a nastavi minDist a ostatne veci
            self.najstNastavit(srcEdges[i][0], srcEdges[i][1], presDist, srcPath)

        #cykus na hladanie dalsie vertexu, ktory je najblizsie ale je este neanavstiveny
        for keys in self.vertexes:
            if minDist == None and keys.unvisited:
                minDist = keys.minDistance
                srcEdges = self.graph[keys.id]
                currentId = keys.id
                srcPath = keys.previousEdges
            elif minDist is not None:
                if keys.minDistance < minDist and keys.unvisited:
                    minDist = keys.minDistance
                    srcEdges = self.graph[keys.id]
                    currentId = keys.id
                    srcPath = keys.previousEdges

        #najdenie sucasneho vertexu a jeho nastavenie ze sme ho uz navstivili
        for keys in self.vertexes:
            if currentId == keys.id:
                keys.unvisited = False
                break

        #ak sa nenasiel ziaden, vertex ktory je nenavstiveny, tak tato podmienka nenastane a ukonci sa   rekurzia
        if minDist is not None:
            self.compDist(srcEdges, minDist, srcPath) #toto je ekurzia


    def najstNastavit(self, target, minDist, presDist, srcPath):   #najprv musi najst dany vertex
        for keys in self.vertexes:     #prechadza cely graph
            if keys.id == target:   # plati ak sa nasiel
                if keys.minDistance == float('inf'):    #ak je este nenavstiveny vertex tak nastavi podla toho od akeho vertexu prisiel a prida este aj cestu do seba
                    keys.minDistance = minDist + presDist
                    keys.previousEdges.clear()
                    keys.previousEdges = srcPath[:]
                    keys.previousEdges.append(keys)
                elif minDist  + presDist < keys.minDistance:    #ak vsak uz bol navstiveny, tak musi porovnavat a podla vysledku to prepise ak nasiel kratsiu cestu
                    keys.minDistance = minDist + presDist
                    keys.previousEdges.clear()
                    keys.previousEdges = srcPath[:]
                    keys.previousEdges.append(keys)
                break

    def getShortestPathTo(self, targetId):  #vrati previousVertex pre dany vertex
        for keys in self.vertexes:
            if keys.id == targetId:
                return keys.previousEdges

    def createGraph(self, vertexes, edgesToVertexes):

        for vert in vertexes:
            self.graph[vert.id] = []
            self.vertexes.append(vert)

        for i in range(len(edgesToVertexes)):
            self.graph[edgesToVertexes[i].source].append([edgesToVertexes[i].target, edgesToVertexes[i].weight])
            for k in range(len(self.vertexes)):
                if edgesToVertexes[i].source == self.vertexes[k].id:
                    self.vertexes[k].edges.append(edgesToVertexes[i])
            # for x in self.graph:
            #     if edgesToVertexes[i].source == x:  #ak sa zhoduje so source, tak prida do dictionary
            #         self.graph[x].append([edgesToVertexes[i].target, edgesToVertexes[i].weight])


        for key, value in self.graph.items() :
            print (key, value)



    def resetDijkstra(self):    #prenastavy na povodne hodnoty
        for vertex in self.vertexes:
            vertex.unvisited = True
            vertex.minDistance = float('inf')
            vertex.previousEdges.clear()


    def getVertexes(self):
        return self.vertexes


    def getIdName(self, sourceId):
        for x in self.graph:
            if x.id == sourceId:
                return x.name
