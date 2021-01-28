package model.adts;

import java.util.List;

public interface CountDownTable<T extends Integer, U extends Integer> {
    T add(U size);
    U lookup(T id);
    void update(T id, U size);
    void clear();
    List<T> getLeft();
    List<U> getRight();
    String toString();
}
