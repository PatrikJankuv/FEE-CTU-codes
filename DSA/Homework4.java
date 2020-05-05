/*
 *  V souboru Homework4.java naimplementujte tøídu DSAHashTable.
 *  Kvùli omezení Javy musíte generické pole alokovat takto:
 *  (Set<Pair<K, V> >[]) new Set<?>[size]. Odevzdejte pouze kód této tøídy.
 */
import java.util.Iterator;
import java.util.*;
import java.util.Set;

// Trida Pair reprezentuje dvojici (klic, hodnota).
// Trida DSAHashTable reprezentuje rozptylovaci tabulku se zøetìzením (první varianta v uèebnici).
class DSAHashTable<K, V> {

    // Vytvori prazdnou instanci DSAHashTable, delka vnitrniho pole je nastavena na size, obsah vnitrniho pole je inicializovan na prazdne mnoziny.
    Set<Pair<K, V>>[] array;
    int size;

    DSAHashTable(int size) {
        this.size = 0;
        array = (Set<Pair<K, V>>[]) new Set<?>[size];

        for (int i = 0; i < size; i++) {
            array[i] = new HashSet<Pair<K, V>>();
        }

    }

    // Ulozi dvojici (key, value) do rozptylovaci tabulky. Pokud uz v tabulce je jina dvojice se stejnym klicem, je smazana. 
    // Klic ani hodnota nesmi byt null. Pokud by pocet dvojic v tabulce po vykonani put mel vzrust nad dvojnasobek delky vnitrniho pole,
    // vnitrni pole zdvojnasobi.
    void put(K key, V value) {
        remove(key);
        array[getIndexOf(key)].add(new Pair<>(key, value));
        size++;

        if (!isBigEnough()) {
            resize(array.length * 2);
        }

    }

    // Vrati hodnotu asociovanou s danym klicem nebo null, pokud dany klic v tabulce neni.
    V get(K key) {

        for (Set<Pair<K, V>> p : array) {
            for (Pair<K, V> k : p) {
                if (k.getKey().equals(key)) {
                    return k.getValue();
                }
            }
        }

        return null;

    }

    // Smaze dvojici s danym klicem. Pokud v tabulce dany klic neni, nedela nic.
    void remove(K key) {
        Set<Pair<K, V>> set = array[getIndexOf(key)];
        Iterator<Pair<K, V>> it = set.iterator();

        while (it.hasNext()) {
            if (it.next().getKey().equals(key)) {
                it.remove();
                size--;
            }
        }
    }

    // Vrati vnitrni pole. Prvky vnitrniho pole mohou byt instance trid v balicku java.util, tzn. nemusite psat vlastni implementaci rozhrani java.util.Set.
    Set<Pair<K, V>>[] getArray() {
        return array;
    }

    // Pro dany klic vrati index v poli. Jako hashovaci funkce se pouzije key.hashCode.
    int getIndexOf(K key) {
        return key.hashCode() % array.length;
    }

    // Pokud je pocet prvku mensi nebo roven dvojnasobku delky vnitrniho pole, vrati true, jinak vrati false.
    boolean isBigEnough() {
        return size <= array.length * 2;
    }

    // Zmeni delku vnitrniho pole, nainicializuje jej prazdnymi mnozinami a zkopiruje do nej vsechny dvojice.
    void resize(int newSize) {
        Iterator<Pair<K, V>> it = iterator();
        
        array = (Set<Pair<K, V>>[]) new Set<?>[newSize];
        for (int i = 0; i < array.length; i++) {
            array[i] = new HashSet<Pair<K, V>>();
        }
        
        
        while (it.hasNext()) {
            Pair<K, V> p = it.next();
            array[getIndexOf(p.getKey())].add(p);
        }
    }

    // Vrati iterator pres vsechny dvojice v tabulce. Iterator nemusi mit implementovanou metodu remove.
    Iterator<Pair<K, V>> iterator() {
        return new Iterator<Pair<K, V>>() {
            Iterator<Pair<K, V>> it;
            int offset = 0;

            Set<Pair<K, V>>[] array = DSAHashTable.this.array;

            public boolean hasNext() {
                for (int i = offset; i < array.length; i++) {
                    
                    if (it == null) {
                        it = array[i].iterator();
                    }
                    if (it.hasNext()) {
                        return true;
                    }
                    
                    it = null;
                    offset++;
                    
                }
                return false;
            }

            public Pair<K, V> next() {
                for (int i = 0; i < array.length; i++) {
                    
                    if (it == null) {
                        it = array[offset].iterator();
                    }
                    if (it.hasNext()) {
                        return it.next();
                    }
                    
                    it = null;
                    offset++;
                    
                }
                throw new NoSuchElementException();
            }

            public void remove() {
            }
        };
    }
}
