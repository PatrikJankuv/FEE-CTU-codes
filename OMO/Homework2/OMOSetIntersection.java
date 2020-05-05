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
// třída reprezentující průnik dvou množin: A průnik B
public class OMOSetIntersection implements OMOSetView {
    OMOSetView baseA;
    OMOSetView baseB;
 
   OMOSetIntersection(OMOSetView setA, OMOSetView setB) {
       this.baseA = setA;
       this.baseB = setB;
       
   }
   
   

// metody rozhraní OMOSetView

@Override
    public boolean contains(int element) {
        return (baseA.contains(element) && baseB.contains(element));
                
    }

    @Override
    public int[] toArray() {
        OMOSetBase intersection = new OMOSet();
        
        int[] a = baseA.toArray();
        
        for (int element : a){
            if(baseB.contains(element) && !intersection.contains(element)){
                intersection.add(element);
            }
        }
        return intersection.toArray();
    }

    @Override
    public OMOSetView copy() {
        OMOSetIntersection deepCopy;
        deepCopy = new OMOSetIntersection(baseA.copy(), baseB.copy());
        
        return deepCopy;
    }
}
