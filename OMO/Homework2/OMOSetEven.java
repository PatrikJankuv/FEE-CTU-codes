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
// třída reprezentující množinu sudých čísel 
public class OMOSetEven implements OMOSetView {
    OMOSetView baseA;

    OMOSetEven(OMOSetView setA) {
        this.baseA = setA;
    }
// metody rozhraní OMOSetView

    @Override
    public boolean contains(int element) {
        return (element % 2 == 0 && baseA.contains(element));
    }

    @Override
    public int[] toArray() {
        OMOSetBase even = new OMOSet();
        
        int[] a = baseA.toArray();

        for (int element : a) {
            if (element % 2 == 0) {
                even.add(element);
            }
        }
        return even.toArray();
    }

    @Override
    public OMOSetView copy() {
        OMOSetEven copy;
        copy = new OMOSetEven(baseA.copy());
        return copy;
    }
}
