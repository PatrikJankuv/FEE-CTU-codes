/**
 *
 * @author pjank
 */
import java.util.ArrayList;
import java.util.Iterator;

// Trida Tree reprezentuje binarni vyhledavaci strom, ve kterem pro kazdy uzel n plati
// n.left == null || n.left.contents.less(n.contents) a
// n.right == null || n.right.contents.greater(n.contents).
class Tree<E extends DSAComparable<E>> {

    Node<E> root;
    ArrayList<E> orderThree = new ArrayList<>();

    public Tree() {
    }

    // Vrati minimum z tohoto stromu nebo null, pokud je strom prazdny.
    E minimum() {
        Node<E> x = root;

        while (x.left != null) {
            x = x.left;
        }

        try {
            return x.contents;
        } catch (NullPointerException ex) {
            return null;
        }
    }

    // Vrati minimum ze zadaneho podstromu nebo null, pokud je podstrom prazdny.
    E subtreeMin(Node<E> n) {
        Node<E> x = n;

        while (x.left != null) {
            x = x.left;
        }

        try {
            return x.contents;
        } catch (NullPointerException ex) {
            return null;
        }
    }

    // Vrati maximum z tohoto podstromu nebo null, pokud je podstrom prazdny.
    E maximum() {
        Node<E> x = root;

        while (x.right != null) {
            x = x.right;
        }

        try {
            return x.contents;
        } catch (NullPointerException ex) {
            return null;
        }
    }

    // Vrati maximum ze zadaneho podstromu nebo null, pokud je podstrom prazdny.
    E subtreeMax(Node<E> n) {
        Node<E> x = n;

        while (x == null) {
            x = x.right;
        }

        return x.contents;
    }

    // Vlozi prvek do stromu (duplicity jsou zakazane)
    void insert(E elem) {
        if (contains(elem)) {
            return;
        }

        Node<E> y = null;
        Node<E> x = root;
//        System.out.println(elem);

        while (x != null) {
            y = x;
//            System.out.println(x.contents + " left " + x.left + " right " + x.right);
            if (elem.less(x.contents)) {
                x = x.left;
            } else {
                x = x.right;
            }
        }

        if (y == null) {
            root = new Node<>(elem, null);
        } else if (elem.less(y.contents)) {
            y.left = new Node<>(elem, y);
        } else {
            y.right = new Node<>(elem, y);
        }
    }

    // Projde strom a vrati: // - uzel s hodnotou elem, pokud existuje, // - null pokud uzel s hodnotou elem existuje
    Node<E> find(E elem) {
        Node<E> x = root;

        if (x == null) {
            return null;
        }

        while (!elem.equal(x.contents)) {
            if (x == null) {
                return null;
            }
            if (elem.less(x.contents)) {
                x = x.left;
            } else {
                x = x.right;
            }

            if (x == null) {
                return null;
            }
        }

        return x;
    }

    // Vrati true, pokud tento strom obsahuje prvek elem.
    boolean contains(E elem) {
        Node<E> x = find(elem);

        if (x == null || x.contents == null) {
            return false;
        } else if (elem.equal(x.contents)) {
            return true;
        } else {
            System.out.println(x.contents);
            return false;
        }
    }

    // Odstrani vyskyt prvku elem z tohoto stromu.
    void remove(E elem) {
//        if (contains(elem)) {
//            size--;
//        }
//
        root = removeNode(root, elem);
//root = remove(elem, root);
    }

    Node<E> removeNode(Node<E> root, E elem) {

        if (root == null) {
            return root;
        }

        if (root.contents.equal(elem)) {
            if (root.left == null && root.right == null) {
                return null;
            }

            if (root.left == null) {
                return root.right;
            }

            if (root.right == null) {
                return root.left;
            }

            root.contents = subtreeMin(root.right);
            root.right = removeNode(root.right, root.contents);
        }

        if (elem.less(root.contents)) {
            root.left = removeNode(root.left, elem);
        } 
        if (elem.greater(root.contents)) {
            root.right = removeNode(root.right, elem);
        }
        return root;
    }

    boolean order() {
        orderThree.clear();
        inorder(root);
        return true;
    }

    void inorder(Node<E> x) {
        if (x != null) {
            inorder(x.left);
            orderThree.add(x.contents);
            inorder(x.right);
        }
    }

    // Vrati iterator pres cely strom (od nejmensiho po nejvetsi). Metoda remove() nemusí být implementována
    Iterator<E> iterator() {
        Iterator<E> iterator = new Iterator<E>() {
            int currIndex = 0;
            boolean ordered = order();

            @Override
            public boolean hasNext() {
                boolean ordered = order();
                if (currIndex < orderThree.size()) {
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public E next() {
                currIndex++;
                return orderThree.get(currIndex - 1);
            }
        };

        return iterator;
    }

// Vrati korenovy uzel tohoto stromu.
    Node<E> getRootNode() {
        return root;
    }

}
