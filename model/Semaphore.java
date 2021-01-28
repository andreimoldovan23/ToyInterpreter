package model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@SuppressWarnings("FieldMayBeFinal")
public class Semaphore<K extends Integer, V extends List<Integer>> {

    private final K size;
    private V elements;
    private final Lock lock = new ReentrantLock();

    public Semaphore(K s, V l) {
        size = s;
        elements = l;
    }

    public boolean acquire(int id) {
        boolean hasBeenAcquired = false;
        lock.lock();
        int length = elements.size();
        if((Integer)size > length) {
            if(!elements.contains(id)) {
                elements.add(id);
            }
            hasBeenAcquired = true;
        }
        lock.unlock();
        return hasBeenAcquired;
    }

    public void release(int id) {
        lock.lock();
        int index = elements.indexOf(id);
        if(index != -1) {
            elements.remove(index);
        }
        lock.unlock();
    }

    public String toString() {
        return "(" + size + "-->" + elements + ") \n";
    }

    public K getSize() {
        return size;
    }

    @SuppressWarnings("unchecked")
    public V getPrograms() {
        return (V) new ArrayList<Integer>(elements);
    }

}
