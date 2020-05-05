package cz.cvut.fel.pjv;

import java.util.ArrayList;

/**
 *
 * @author patrik
 */
public class TreeImpl implements Tree{
    private NodeImpl root;
   
    public TreeImpl() {
    }
    
   
    @Override
    public void setTree(int[] values) {
        if(isEmty(values)){
            root = null;
        }
        else
            root = setNodes(0, values.length, values);   
    }
    
    private NodeImpl setNodes(int first, int last, int[] values){
        if(isEnd(last, first))
            return null;
        
        int middle = (last + first) / 2;
        
        NodeImpl node = new NodeImpl(values[middle]);
        node.setLeft(setNodes(first, middle, values));
        node.setRight(setNodes(middle+1, last, values));
        
        return node;
    }
    

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public String toString() {
        String output;
        StringBuffer print = new StringBuffer();
                
        if (root == null){
            output = print.toString();
            return output;
        }
        else {   
            root.toPrint(0, print);
            output = print.toString();
            return output;
        }
    }
    
    
    
    public boolean isEmty(int[] values){
        if(values.length == 0){
            root = null;
            return true;
        }
        return false;
    }
    
    public boolean isEnd(int x, int y){
        if(x - y > 0)
            return false;
        else
            return true;
    }
}