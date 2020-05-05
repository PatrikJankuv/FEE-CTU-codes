import sys
def writeTextToFile(parameterToAdd):
    STATICKÝ_TEXT = "This is my static text which must be added to file. It is very long text and I do not know what they want to do with this terrible text. "
    STATICKÝ_TEXT = STATICKÝ_TEXT + str(parameterToAdd)
    with open('output.txt', 'w') as vystup:
          vystup.write(STATICKÝ_TEXT)
    print('hello')           
    return 'output.txt'