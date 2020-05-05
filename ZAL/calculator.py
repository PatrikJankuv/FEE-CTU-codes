import math

def addition(x, y):
    result = x + y
    return result
    
def subtraction(x, y):
    result = x - y
    return result


def multiplication(x, y):
    result = x * y
    return result


def division(x, y):
    if y == 0:
        raise ValueError('This operation is not supported for given input parameters')
    else:
        result = x / y
    return result

def modulo(x, y):
    if (x >= y and y > 0):
        result = x % y
    else:
        raise ValueError('This operation is not supported for given input parameters') 
    return result


def secondPower(x):
    result = x * x
    return float(result)


def power(x, y):
    if (y >= 0) and (y-int(y) == 0):	#if y is 1,5 then int from is 1, 1,5 - 1 is not 0 and that can be power
        return float(x**y)
    else:
        raise ValueError('This operation is not supported for given input parameters')	


def secondRadix(x):
    if x > 0:
        result = math.sqrt(x)
    else:
        raise ValueError('This operation is not supported for given input parameters')
    return result


def magic(x, y, z, k):
    l = x + k
    m = y + z
    if m != 0:
        z = (l/m) + 1
    else:
        raise ValueError('This operation is not supported for given input parameters')
    return z 


def control(a, x, y, z, k):
    q = x
    p = y
    if a == 'ADDITION':
        return addition(q, p)
    elif a == 'SUBTRACTION':
        return subtraction(x, y)
    elif a == 'MULTIPLICATION':
        return multiplication(x, y)
    elif a == 'DIVISION':
        return division(x, y)
    elif a == 'MOD':
        return modulo(x, y)
    elif a == 'POWER':
        return power(x, y)
    elif a == 'SECONDRADIX':
        return secondRadix(x)
    elif a == 'MAGIC':
        return magic(x, y, z, k)
    else:
        raise ValueError('This operation is not supported for given input parameters')