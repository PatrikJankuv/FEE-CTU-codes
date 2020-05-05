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
// třída reprezentující A\B: doplněk množiny B vzhledem k množině A:  A\B = { x | x?A ? x?B }
public class OMOSetComplement implements OMOSetView {

    OMOSetView baseA;
    OMOSetView baseB;
    

    OMOSetComplement(OMOSetView setA, OMOSetView setB) {
        this.baseA = setA;
        this.baseB = setB;

    }

    @Override
    public boolean contains(int element) {
        return (baseA.contains(element) && !baseB.contains(element));
    }

    @Override
    public int[] toArray() {
        OMOSetBase complement = new OMOSet();

        int[] a = baseA.toArray();

        for (int element : a) {
            if (!baseB.contains(element) && !complement.contains(element)) {
                complement.add(element);
            }
        }
        return complement.toArray();
    }

    @Override
    public OMOSetView copy() {
        OMOSetComplement deepCopy;
        deepCopy = new OMOSetComplement(baseA.copy(), baseB.copy());
        return deepCopy;
    }
}
