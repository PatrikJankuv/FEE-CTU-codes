package cz.cvut.fel.pjv;

/**
 * Implementation of the {@link Queue} backed by fixed size array.
 */
public class CircularArrayQueue implements Queue {
        
    String[] queue;
    int first = 0;
    int last = 0;
    int size;
    int capacity;
    /**
     * Creates the queue with capacity set to the value of 5.
     */
    public CircularArrayQueue() {
           this.queue = new String[5];
           this.capacity = 5;
    }


    /**
     * Creates the queue with given {@code capacity}. The capacity represents maximal number of elements that the
     * queue is able to store.
     * @param capacity of the queue
     */
    public CircularArrayQueue(int capacity) {
           this.queue = new String[capacity];
           this.capacity = capacity;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public boolean isFull() {
        return this.size == capacity;
    }

    @Override
    public boolean enqueue(String obj) {
        if(isFull())
            return false;
        
        if(obj == null)
            return false;
        
        queue[last]  = obj;
        last++;
        if (last == capacity){
            last = 0;
        }
        size++;
        return true;
    }

    @Override
    public String dequeue() {
        if (isEmpty())
            return null;
        
        String tmp = queue[first];
        first++;
        if (first == capacity){
            first = 0;
        }
        
        size--;
        return tmp;
    }

    @Override
    public void printAllElements() {
        for (String element : queue){
            if (null != element)
                System.out.println(element);
        }

    }
}
