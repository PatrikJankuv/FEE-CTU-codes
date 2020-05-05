/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.k36.omo.hw.hw02;

/**
 *
 * @author patrik
 */
// třída reprezentující sjednocení dvou množin: A sjednoceno B
public class OMOSetUnion implements OMOSetView {
    OMOSetView baseA;
    OMOSetView baseB;

    OMOSetUnion(OMOSetView setA, OMOSetView setB) {
        this.baseA = setA;
        this.baseB = setB;

    }

// metody rozhraní OMOSetView
    @Override
    public boolean contains(int element) {
        return (baseA.contains(element) || baseB.contains(element));
    }

    @Override
    public int[] toArray() {
        OMOSetBase union = new OMOSet();

        int[] a = baseA.toArray();
        for (int element : a) {
            union.add(element);
        }

        int[] b = baseB.toArray();
        for (int element : b) {
            if(!union.contains(element))
                union.add(element);
        }

        return union.toArray();
    }

    @Override
    public OMOSetView copy() {
        OMOSetUnion copy;
        copy = new OMOSetUnion(baseA.copy(), baseB.copy());
        return copy;
    }
}
