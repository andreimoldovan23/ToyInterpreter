package model.adts;

import java.util.List;

public interface IOut<T> {
    void add(T exp);
    String toString();
    void clear();
    List<T> getValues();
}
