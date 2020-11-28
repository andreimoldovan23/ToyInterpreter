package ToyInterpreter.model.adts;

import ToyInterpreter.model.values.Value;
import java.util.List;

public interface ISymTable<T, U extends Value> {
    void add(T key, U value);
    U lookup(T key);
    boolean isDefined(T key);
    void update(T key, U el);
    String toString();
    void clear();
    List<U> getValues();
    ISymTable<T, U> copy();
}
