package ToyInterpreter.model.adts;

import ToyInterpreter.exceptions.InvalidFilenameException;
import java.util.Set;

public interface IFileTable<T, U> {
    void add(T key, U value);
    void remove(T key) throws InvalidFilenameException;
    boolean isDefined(T key);
    U lookup(T key);
    void clear();
    Set<U> getKeys();
    String toString();
}
