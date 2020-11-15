package model.adts;

public interface IOut<T> {
    void add(T exp);
    String toString();
    void clear();
}
