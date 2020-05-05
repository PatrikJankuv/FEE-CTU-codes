/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.pjv;

/**
 *
 * @author patrik
 */
public class NodeImpl implements Node{
    private int value;
    private NodeImpl left;
    private NodeImpl right;

  
    NodeImpl(int value) {
        this.value = value;
    }

    public void setLeft(NodeImpl left){
        this.left = left;
    }
    
    public void setRight(NodeImpl right){
        this.right = right;
    }
    
    @Override
    public Node getLeft() {
        return left;
    }

    @Override
    public Node getRight() {
        return right;
    }

    @Override
    public int getValue() {
        return value;
    }

   
    
    public void toPrint(int index, StringBuffer print){
        for (int i = 0; i < index; i++) {
            print.append(' ');
        }
        
        print.append("- ").append(value).append("\n");

        if (left != null) {
            left.toPrint(index + 1, print);
        }

        if (right != null) {
            right.toPrint(index + 1, print);
        }       
    }
    
}
