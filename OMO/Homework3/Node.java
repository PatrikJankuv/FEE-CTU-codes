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
public class Node {

    private final int contents;
    private final Node left, right;
    private Node parent;

    public Node(int contents, Node left, Node right) {
        this.contents = contents;
        this.left = left;
        if (left != null) {
            left.parent = this;
        }
        this.right = right;
        if (right != null) {
            right.parent = this;
        }
    }

    public int getContents() {
        return contents;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    public Node getParent() {
        return parent;
    }
    

    public CustomIterator preorderIterator() {
        return new PreorderIterator(this);
    }

    public CustomIterator inorderIterator() {
        return new InorderIterator(this);
    }

    public CustomIterator postorderIterator() {
        return new PostorderIterator(this);
    }



}
