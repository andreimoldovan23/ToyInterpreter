package ToyInterpreter.model.adts;

import ToyInterpreter.exceptions.InvalidFilenameException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileTable<T, U> implements IFileTable<T, U> {

    private final Map<T, U> map = new HashMap<>();

    public void add(T key, U value) {
        map.put(key, value);
    }

    public void remove(T key) throws InvalidFilenameException {
        U element = map.remove(key);
        if(element == null)
            throw new InvalidFilenameException();
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
            strBuilder.append(entry.getKey().toString()).append("=").append(entry.getValue().toString())
                    .append("\n");
        }
        return strBuilder.toString();
    }

    public List<U> getValues(){
        return new ArrayList<>(map.values());
    }

}
