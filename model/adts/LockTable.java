package model.adts;

import java.util.List;

public interface LockTable<K extends Integer, V extends Integer> {
    K add();
    V lookup(K lockID);
    boolean update(K lockId, V prgId, boolean flag);
    String toString();
    List<K> getLeft();
    List<V> getRight();
    void clear();
}
