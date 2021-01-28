package model.adts;

import model.types.Type;
import java.util.HashMap;
import java.util.Map;

public class TypeEnv<T, U extends Type> implements ITypeEnv<T, U> {

    private final Map<T, U> map = new HashMap<>();

    public void add(T key, U val) {
        map.put(key, val);
    }

    public boolean isDefined(T key) {
        return lookup(key) != null;
    }

    public U lookup(T key) {
        return map.get(key);
    }

    @SuppressWarnings("unchecked")
    public ITypeEnv<T, U> copy() {
        ITypeEnv<T, U> newEnv = new TypeEnv<>();
        for(Map.Entry<T, U> entry : map.entrySet())
            newEnv.add(entry.getKey(), (U) entry.getValue().copy());
        return newEnv;
    }

    public void clear() {
        map.clear();
    }
}
