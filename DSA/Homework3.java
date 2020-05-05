/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
 * Naimplementujte třídu Homework3 implementující rozhraní HeapStorage a třídu Heap. Odevzdejte pouze kód těchto dvou tříd.
 */
class Homework3<E extends DSAComparable<E>> implements HeapStorage<E> {

    ArrayList<E> heapList;
    private E[] heapStorage;
    // Vytvori novy objekt HeapStorage nad polem elements, jeho velikost je stejna jako delka pole. 

    Homework3(E[] elements) {
        this.heapStorage = elements;
        heapList = Stream.of(elements).collect(Collectors.toCollection(ArrayList::new));
    }

    // metody 
    @Override
    public int getSize() {
        return heapList.size();
    }

    @Override
    public boolean isEmpty() {
        return heapList.isEmpty();
    }

    @Override
    public E getElement(int index) {
        return heapList.get(index);
    }

    @Override
    public void swap(int index, int index2) {
        E first = heapList.get(index);
        E second = heapList.get(index2);

        heapList.set(index2, first);
        heapList.set(index, second);

        E element = heapStorage[index];
        heapStorage[index] = heapStorage[index2];
        heapStorage[index2] = element;
    }

    @Override
    public E extractLast() {
        return heapList.remove(heapList.size() - 1);
    }

    @Override
    public int insertLast(E element) {
        heapList.add(element);
        return heapList.indexOf(element);
    }

    @Override
    public String toString() {
        return Arrays.toString(heapList.toArray());
    }

}

// Trida Heap reprezentuje haldu (s maximem ve vrcholu). 
class Heap<E extends DSAComparable<E>> {

    int n;
    HeapStorage<E> storage; // Vytvori haldu nad danym HeapStorage (tzn. zavola algoritmus build heap). 

    Heap(HeapStorage<E> storage) {
        this.storage = storage;
    }

    // Zavola algoritmus heapify nad uzlem na indexu index. 
    void heapify(int index) {
        int largest = index;
        int left = (2 * index) + 1;
        int right = (2 * index) + 2;
        int m = this.n;

        //     System.out.println("storage " + storage.toString());
        if (left < m && storage.getElement(largest).less(storage.getElement(left))) {
            largest = left;
        }

        if (right < m && storage.getElement(largest).less(storage.getElement(right))) {
            largest = right;
        }

        if (largest != index) {
            this.storage.swap(index, largest);
            heapify(largest);
        }

    }

    // Vlozi do haldy novy prvek. Muzete predpokladat, ze v poli uvnitr HeapStorage na nej misto je. 
    void insert(E element) {
        storage.insertLast(element);

        for (int i = storage.getSize(); i > 0; i--) {
            heapify(i);
        }
    }

    // Odstrani a vrati z haldy maximalni prvek. 
    E extractMax() {
        return storage.extractLast();
    }

    // Vrati true, pokud je halda prazdna. 
    boolean isEmpty() {
        return storage.isEmpty();
    }

// Pomoci algoritmu trideni haldou vzestupne setridi pole array. 
    static <E extends DSAComparable<E>> void heapsort(E[] array) {
        Homework3 storage = new Homework3(array);
        Heap heap = new Heap(storage);
        int n = heap.storage.getSize();
        heap.n = heap.storage.getSize();

        for (int i = n / 2 - 1; i >= 0; i--) {
            heap.heapify(i);
        }

        for (int i = n - 1; i >= 0; i--) {
            heap.n = heap.n - 1;
            storage.swap(0, i);
            heap.heapify(0);

        }

    }

}
