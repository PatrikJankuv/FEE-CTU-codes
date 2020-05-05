/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.k36.omo.hw.hw02;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author patrik
 */
// třída reprezentující obecnou množinu
public class OMOSet extends OMOSetBase implements OMOSetView {

    private Set<Integer> base;

    public OMOSet() {
        this.base = new HashSet<>();

    }

    public OMOSet(int[] base) {
        this.base = new HashSet<>();
        
        if (base != null && base.length > 0) {
            for (int i = 0, max = base.length; i < max; i++) {
                this.base.add(base[i]);
            }
        }
    }

    // OVERRIDE OMOSetBase abstract methods:
    @Override
    public void add(int element) {
        base.add(element);
    }

    @Override
    public void remove(int element) {
        base.remove(element);
    }

    // IMPLEMENT OMOSetView interface:
    @Override
    public boolean contains(int element) {
        return base.contains(element);
    }

    @Override
    public int[] toArray() {
        int[] ret = base.stream().mapToInt(Integer::intValue).toArray();
        return ret;
    }

    /**
     * @return
     */
    @Override
    public OMOSetView copy() {
        return new OMOSet(this.toArray());

    }
}
