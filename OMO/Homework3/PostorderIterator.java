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
public class PostorderIterator extends Iterator{
    
    public PostorderIterator(Node nodeRoot) {
        super(nodeRoot);
        setRootToCurrent();
        putIntoOrder();
    }
    
    private void putIntoOrder(){
        
        while(isThereSomething()){
            if(!order.contains(currentNode) && currentNode.getLeft() != null && !order.contains(currentNode.getLeft())){
                currentNode = currentNode.getLeft();
            }
            else if(!order.contains(currentNode) && currentNode.getLeft() == null){
                if(currentNode.getRight() != null && !order.contains(currentNode.getRight())){
                    currentNode = currentNode.getRight();
                }
                else if(currentNode.getRight() == null){
                    order.add(currentNode);
                }
                else{
                order.add(currentNode);
                if( currentNode.getParent() != null)
                    currentNode = currentNode.getParent();
                }
            }
            else if(!order.contains(currentNode) && currentNode.getLeft() != null && order.contains(currentNode.getLeft())){
                if(currentNode.getRight() != null && !order.contains(currentNode.getRight())){
                    currentNode = currentNode.getRight();
                }
                else{
                    order.add(currentNode);
                    if( currentNode.getParent() != null){
                        currentNode = currentNode.getParent();
                    }
                }
            }
            else if(order.contains(currentNode) &&  currentNode.getParent() != null){
                    currentNode = currentNode.getParent();
            }  
//            else if(currentNode.getRight() == null || (currentNode.getRight() != null && order.contains(currentNode.getRight()))){
//               if( currentNode.getParent() != null)
//                currentNode = currentNode.getParent();
//            }            
        }
    }
    
    private boolean isThereSomething(){
        return !order.contains(nodeRoot);
    }
    
}
