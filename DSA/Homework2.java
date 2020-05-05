/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.hw2;

/*
 * Naimplementujte třídu Homework2 implementující rozhraní InterSearch (váš soubor Homework2.java bude toto rozhraní obsahovat).
 */
interface InterSearch {

    /* Požitím interpolačního hledání vrátí index prvku what nalezeného mezi indexy first a last
       pole data nebo -1, pokud tam není. Metoda bude rekurzivní a měla by být odolná vůči chybně 
       zadaným parametrům (v případě chyby vrátí opět -1). Pro zaokrouhlování na celá čísla použijte metodu Math.round(). */
    public int search(int first, int last, int what, int[] data);
}

class Homework2 implements InterSearch {

    @Override
    public int search(int first, int last, int what, int[] data) {

        //indexes out of bounds
        if (first >= data.length || last >= data.length || first < 0) {
            return -1;
        }

        int firstValue = data[first];
        int lastValue = data[last];
        
        if (first == last || firstValue == lastValue) {
            return -1;
        } else if (firstValue == what) {
            return first;
        } else if (lastValue == what){
            return last;
        }

        int index = (int) Math.round(first + ((double) what - firstValue) * (last - first) / (lastValue - firstValue));

        //if index out of range of search
        if (index < first || index > last || data.length <= index) {
            return -1;
        }

        if (data[index] < what) {
            return search(index + 1, last, what, data);
        } else if (data[index] > what) {
            return search(first, index - 1, what, data);
        }

        //recursive return
        return index;
    }
}
