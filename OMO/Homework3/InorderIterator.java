/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.k36.omo.hw.hw03;

/**
 *
 * @author patrik
 */
public class InorderIterator extends Iterator {

    public InorderIterator(Node nodeRoot) {
        super(nodeRoot);
        setRootToCurrent();
        putThatBitchIntoFIFO();
    }

    private void putThatBitchIntoFIFO() {
        boolean isThereSomething = true;
        
        while(isThereSome(currentNode)){
            if(!order.contains(currentNode) && currentNode.getLeft() != null && !order.contains(currentNode.getLeft())){
                currentNode = currentNode.getLeft();
            }
            else if(!order.contains(currentNode) && currentNode.getLeft() == null){
                order.add(currentNode);
                if(currentNode.getRight() != null){
                    currentNode = currentNode.getRight();
                }
                else if( currentNode.getParent() != null)
                    currentNode = currentNode.getParent();
            }
            else if(!order.contains(currentNode) && currentNode.getLeft() != null && order.contains(currentNode.getLeft())){
                order.add(currentNode);
            }
            else if(order.contains(currentNode) &&  currentNode.getRight() != null && !order.contains(currentNode.getRight())){
                currentNode = currentNode.getRight();
            }  
            else if(currentNode.getRight() == null || (currentNode.getRight() != null && order.contains(currentNode.getRight()))){
               if( currentNode.getParent() != null)
                currentNode = currentNode.getParent();
            }            
        }
    }
    
    private boolean isThereSome(Node node){
        
        if(nodeRoot != node){
            return true;
        }
        else{
            if(currentNode.getLeft() == null && currentNode.getRight() == null){
                if(!order.contains(currentNode))
                    order.add(node);
                return false;
            }
            else if(currentNode.getLeft() != null && currentNode.getRight() == null){
                return !(order.contains(currentNode.getLeft()) && order.contains(currentNode));
            }
            else if(currentNode.getLeft() == null && currentNode.getRight() != null){
                return !order.contains(node.getRight());
            }
            else if(currentNode.getLeft() != null && currentNode.getRight() != null){
                return !order.contains(node.getRight());
            }
        }
        return false;
    }
}
 