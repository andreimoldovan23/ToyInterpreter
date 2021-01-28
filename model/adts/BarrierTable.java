package model.adts;

import model.Barrier;

import java.util.List;

public interface BarrierTable<T extends Integer, U extends Barrier<Integer, List<Integer>>> {
    T add(U barrier);
    U lookup(T id);
    void clear();
    String toString();
    List<T> getLeft();
    List<Integer> getMiddle();
    List<List<Integer>> getRight();
}
