package model.adts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@SuppressWarnings("FieldMayBeFinal")
public class CountDownLatch<T extends Integer, U extends Integer> implements CountDownTable<T, U>{

    private static AtomicInteger id = new AtomicInteger(0);
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private Map<T, U> map = new HashMap<>();

    @SuppressWarnings("unchecked")
    public T add(U size) {
        int idx = id.incrementAndGet();
        lock.writeLock().lock();
        map.put((T)Integer.valueOf(idx), size);
        lock.writeLock().unlock();
        return (T)Integer.valueOf(idx);
    }

    public U lookup(T id) {
        lock.readLock().lock();
        U val = map.get(id);
        lock.readLock().unlock();
        return val;
    }

    public void update(T latchId, U size) {
        lock.writeLock().lock();
        map.replace(latchId, size);
        lock.writeLock().unlock();
    }

    public void clear() {
        map.clear();
    }

    public List<T> getLeft() {
        return new ArrayList<>(map.keySet());
    }

    public List<U> getRight() {
        return new ArrayList<>(map.values());
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for(Map.Entry<T, U> entry : map.entrySet()) {
            stringBuilder.append(entry.getKey()).append("-->").append(entry.getValue()).append("\n");
        }
        return stringBuilder.toString();
    }

}
