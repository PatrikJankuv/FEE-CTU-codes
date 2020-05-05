/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.k36.omo.hw.hw03;

import java.util.LinkedList;

/**
 *
 * @author patrik
 */
public class Iterator implements CustomIterator{
    Node nodeRoot, currentNode;
    LinkedList<Node> order = new LinkedList<>();
    

    public Iterator(Node nodeRoot) {
        this.nodeRoot = nodeRoot;
    }
    
    protected final void setRootToCurrent() {
        currentNode = nodeRoot;
    }
    
    protected final void setRootNode(){
        
    }

    @Override
    public boolean hasNext() {
        return (!order.isEmpty());
    }

    @Override
    public int next() {
        return order.removeFirst().getContents();
    }
    
    
}
