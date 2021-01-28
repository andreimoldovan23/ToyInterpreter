package model.adts;

import model.Barrier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@SuppressWarnings("FieldMayBeFinal")
public class CyclicBarrierTable<T extends Integer, U extends Barrier<Integer, List<Integer>>>
    implements BarrierTable<T, U>{

    private static AtomicInteger id = new AtomicInteger(0);
    private Map<T, U> map = new HashMap<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    @SuppressWarnings("unchecked")
    public T add(U barrier) {
        int index = id.incrementAndGet();
        lock.writeLock().lock();
        map.put((T)Integer.valueOf(index), barrier);
        lock.writeLock().unlock();
        return (T)Integer.valueOf(index);
    }

    public U lookup(T id) {
        lock.readLock().lock();
        U barrier = map.get(id);
        lock.readLock().unlock();
        return barrier;
    }

    public void clear() {
        map.clear();
    }

    public List<T> getLeft() {
        return new ArrayList<>(map.keySet());
    }

    public List<Integer> getMiddle() {
        List<Integer> sizes = new ArrayList<>();
        List<U> barriers = new ArrayList<>(map.values());
        for(var b : barriers)
            sizes.add(b.getSize());
        return sizes;
    }

    public List<List<Integer>> getRight() {
        List<List<Integer>> programs = new ArrayList<>();
        List<U> barriers = new ArrayList<>(map.values());
        for(var b : barriers)
            programs.add(b.getElements());
        return programs;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for(Map.Entry<T, U> e : map.entrySet())
            stringBuilder.append(e.getKey()).append("-->").append(e.getValue()).append("\n");
        return stringBuilder.toString();
    }

}
