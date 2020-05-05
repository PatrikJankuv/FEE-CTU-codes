 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.cvut.k36.omo.hw.hw01;

/**
 *
 * @author patrik
 */
public class Homework1 {
    private int callh;
    private static int calli;

    public Homework1() {
        callh = 0;
    }

    
    public boolean f(){
            return true;
        }

    public static boolean g(){
        return false;
    }
    
    int h(){
        return ++this.callh;
    }
   
    int i(){
        return ++Homework1.calli;
    }


}
