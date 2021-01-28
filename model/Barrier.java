package model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Barrier<T extends Integer, U extends List<Integer>> {

    private final T size;
    private final U elements;
    private final Lock lock = new ReentrantLock();

    public Barrier(T s, U e) {
        size = s;
        elements = e;
    }

    public boolean awaitBarrier(int id) {
        lock.lock();
        boolean isWaiting = false;
        if((Integer)size > elements.size()) {
            if(!elements.contains(id))
                elements.add(id);
            isWaiting = true;
        }
        lock.unlock();
        return isWaiting;
    }

    public T getSize() {
        return size;
    }

    @SuppressWarnings("unchecked")
    public U getElements() {
        return (U) new ArrayList<>(elements);
    }

    public String toString() {
        return "(" + size + "-->" + elements + ") \n";
    }

}
