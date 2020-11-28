package ToyInterpreter.model.adts;

import ToyInterpreter.model.types.Type;

public interface ITypeEnv<T, U extends Type> {
    void add(T key, U val);
    boolean isDefined(T key);
    U lookup(T key);
    ITypeEnv<T, U> copy();
    void clear();
}
