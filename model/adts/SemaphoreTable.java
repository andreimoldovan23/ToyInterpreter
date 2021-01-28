package model.adts;

import model.Semaphore;
import java.util.List;

public interface SemaphoreTable<T extends Integer, U extends Semaphore<Integer, List<Integer>>> {
    T add(U sem);
    U lookup(T idx);
    void clear();
    String toString();
    List<T> getLeft();
    List<Integer> getMiddle();
    List<List<Integer>> getRight();
}
