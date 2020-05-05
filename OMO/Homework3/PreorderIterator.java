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
public final class PreorderIterator extends Iterator {

    public PreorderIterator(Node nodeRoot) {
        super(nodeRoot);
        setRootToCurrent();
        putThatBitchIntoFIFO();
    }

    private void putThatBitchIntoFIFO() {
        order.add(nodeRoot);
        boolean isThereSomething = true;

        while (isThereSome(currentNode)) {
            if (currentNode.getLeft() != null && !order.contains(currentNode.getLeft())) {
                currentNode = currentNode.getLeft();
                order.add(currentNode);

            } else if (currentNode.getRight() != null && !order.contains(currentNode.getRight())) {
                currentNode = currentNode.getRight();
                order.add(currentNode);

            } else {
                if (currentNode.getParent() != null) {
                    currentNode = currentNode.getParent();
                } else {
                    isThereSomething = false;
                }

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
