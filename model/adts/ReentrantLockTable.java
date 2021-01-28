package model.adts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@SuppressWarnings("FieldMayBeFinal")
public class ReentrantLockTable<K extends Integer, V extends Integer> implements LockTable<K, V> {

    private final Map<K, V> map = new HashMap<>();
    private static AtomicInteger id = new AtomicInteger(0);
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    @SuppressWarnings("unchecked")
    public K add() {
        int index = id.incrementAndGet();
        lock.writeLock().lock();
        map.put((K)Integer.valueOf(index), (V)Integer.valueOf(-1));
        lock.writeLock().unlock();
        return (K)Integer.valueOf(index);
    }

    public V lookup(K lockID) {
        lock.readLock().lock();
        V lockedPrg = map.get(lockID);
        lock.readLock().unlock();
        return lockedPrg;
    }

    private boolean getLock(K lockId, V prgId) {
        boolean hasLocked = false;
        lock.writeLock().lock();
        V val = map.get(lockId);
        if(val.equals(-1)) {
            map.replace(lockId, prgId);
            hasLocked = true;
        }
        lock.writeLock().unlock();
        return hasLocked;
    }

    @SuppressWarnings("unchecked")
    private boolean releaseLock(K lockId, V prgId) {
        boolean hasReleased = false;
        lock.writeLock().lock();
        V val = map.get(lockId);
        if(val.equals(prgId)) {
            map.replace(lockId, (V)Integer.valueOf(-1));
            hasReleased = true;
        }
        lock.writeLock().unlock();
        return hasReleased;
    }

    public boolean update(K lockId, V prgId, boolean flag) {
        if(!flag)
            return releaseLock(lockId, prgId);
        return getLock(lockId, prgId);
    }

    public List<K> getLeft() {
        return new ArrayList<>(map.keySet());
    }

    public List<V> getRight() {
        return new ArrayList<>(map.values());
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for(Map.Entry<K, V> e : map.entrySet())
            stringBuilder.append(e.getKey()).append("-->").append(e.getValue()).append("\n");
        return stringBuilder.toString();
    }

    public void clear() {
        map.clear();
    }

}
