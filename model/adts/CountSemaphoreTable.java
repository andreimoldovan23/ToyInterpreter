package model.adts;

import model.Semaphore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@SuppressWarnings("FieldMayBeFinal")
public class CountSemaphoreTable<T extends Integer, U extends Semaphore<Integer, List<Integer>>>
        implements SemaphoreTable<T, U> {

    private static AtomicInteger semaphoreID = new AtomicInteger(0);
    private Map<T, U> map = new HashMap<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    @SuppressWarnings("unchecked")
    public T add(U sem) {
        int idx = semaphoreID.incrementAndGet();
        lock.writeLock().lock();
        map.put((T) Integer.valueOf(idx), sem);
        lock.writeLock().unlock();
        return (T) Integer.valueOf(idx);
    }

    public U lookup(T idx) {
        lock.readLock().lock();
        U sem = map.get(idx);
        lock.readLock().unlock();
        return sem;
    }

    public void clear() {
        map.clear();
    }

    public List<T> getLeft() {
        return new ArrayList<>(map.keySet());
    }

    public List<Integer> getMiddle() {
        List<U> semList = new ArrayList<>(map.values());
        List<Integer> sizes = new ArrayList<>();
        for(var v: semList)
            sizes.add(v.getSize());
        return sizes;
    }

    public List<List<Integer>> getRight() {
        List<U> semList = new ArrayList<>(map.values());
        List<List<Integer>> programs = new ArrayList<>();
        for(var v: semList)
            programs.add(v.getPrograms());
        return programs;
    }

    public String toString() {
        StringBuilder strBuilder = new StringBuilder();
        for (Map.Entry<T, U> entry : map.entrySet()) {
            strBuilder.append(entry.getKey()).append("-->").append(entry.getValue()).append("\n");
        }
        return strBuilder.toString();
    }

}
