package model.adts;

import exceptions.InvalidFilename;
import java.util.List;

public interface IFileTable<T, U> {
    void add(T key, U value);
    void remove(T key) throws InvalidFilename;
    boolean isDefined(T key);
    U lookup(T key);
    void clear();
    String toString();
    List<T> getKeys();
    List<U> getValues();
}
