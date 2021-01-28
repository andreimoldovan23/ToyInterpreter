package model.adts;

import model.values.Value;
import java.util.*;

public class SymTable<T, U extends Value> implements ISymTable<T, U> {

    private final Map<T, U> map;

    public SymTable() {
        map = new HashMap<>();
    }

    public void add(T key, U value) {
        map.put(key, value);
    }

    public U lookup(T key){
        return map.get(key);
    }

    public boolean isDefined(T key) {
        return lookup(key) != null;
    }

    public void update(T key, U val){
        map.replace(key, val);
    }

    public String toString() {
        StringBuilder strBuilder = new StringBuilder();
        for (Map.Entry<T, U> entry : map.entrySet()) {
            strBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("\n");
        }
        return strBuilder.toString();
    }

    public void clear() {
        map.clear();
    }

    public List<U> getValues(){
        return new ArrayList<>(map.values());
    }

    public List<T> getKeys() {
        return new ArrayList<>(map.keySet());
    }

    @SuppressWarnings("unchecked")
    public ISymTable<T, U> copy() {
        ISymTable<T, U> newTable = new SymTable<>();
        for (Map.Entry<T, U> entry : map.entrySet()){
            newTable.add(entry.getKey(), (U)entry.getValue().copy());
        }
        return newTable;
    }

}