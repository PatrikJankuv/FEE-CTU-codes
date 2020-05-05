perms = []
fixed = []

def permutations(array):
    perms.clear()

    arial = permut(array)
    return arial

def permut(array):
    pom = array[:]
    arraylen = len(array)

    for i in range(arraylen):
        array = pom[:]
        swap(array, 0, i)
        fixed.append(array[0])
        permut(array[1:])
        array.append(fixed[-1])
        fixed.pop()

    if array == []:
        perms.append(fixed[:])

    return perms


def swap(array, a, b):
    array[a], array[b] = array[b], array[a]
    return array
