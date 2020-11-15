package model.adts;

public interface ISymTable<T, U> {
    void add(T key, U value);
    U lookup(T key);
    boolean isDefined(T key);
    void update(T key, U el);
    String toString();
    void clear();
}
