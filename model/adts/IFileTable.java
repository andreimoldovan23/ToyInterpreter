package ToyInterpreter.model.adts;

import ToyInterpreter.exceptions.InvalidFilenameException;
import java.util.List;

public interface IFileTable<T, U> {
    void add(T key, U value);
    void remove(T key) throws InvalidFilenameException;
    boolean isDefined(T key);
    U lookup(T key);
    void clear();
    List<U> getValues();
    String toString();
}
