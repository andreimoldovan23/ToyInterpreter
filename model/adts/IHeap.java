package model.adts;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IHeap<T extends Integer, U> {
    int add(U value);
    boolean isDefined(T key);
    void update(T key, U value);
    U lookup(T key);
    void clear();
    String toString();
    Set<Map.Entry<T, U>> getContent();
    void setContent(Map<T, U> newHeap);
    List<T> getKeys();
    List<U> getValues();
}
