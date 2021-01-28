package model.adts;

import exceptions.InvalidFilename;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FileTable<T, U> implements IFileTable<T, U> {

    private final Map<T, U> map = new ConcurrentHashMap<>();

    public void add(T key, U value) {
        map.putIfAbsent(key, value);
    }

    public void remove(T key) throws InvalidFilename {
        U element = map.remove(key);
        if(element == null)
            throw new InvalidFilename();
    }

    public boolean isDefined(T key) {
        return lookup(key) != null;
    }

    public U lookup(T key) {
        return map.get(key);
    }

    public void clear() {
        map.clear();
    }

    public String toString(){
        StringBuilder strBuilder = new StringBuilder();
        for (Map.Entry<T, U> entry : map.entrySet()) {
            strBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("\n");
        }
        return strBuilder.toString();
    }

    public List<T> getKeys() {
        return new ArrayList<>(map.keySet());
    }

    public List<U> getValues(){
        return new ArrayList<>(map.values());
    }

}
