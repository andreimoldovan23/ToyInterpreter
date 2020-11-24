package ToyInterpreter.model.adts;

import java.util.*;

public class SymTable<T, U> implements ISymTable<T, U> {

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
            strBuilder.append(entry.getKey().toString()).append("=").append(entry.getValue().toString())
                    .append("\n");
        }
        return strBuilder.toString();
    }

    public void clear() {
        map.clear();
    }

    public List<U> getValues(){
        return new ArrayList<>(map.values());
    }

}