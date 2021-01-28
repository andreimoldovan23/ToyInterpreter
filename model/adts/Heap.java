package model.adts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("FieldMayBeFinal")
public class Heap<T extends Integer, U> implements IHeap<T, U>{

    private static AtomicInteger nextFree = new AtomicInteger(0);
    private Map<T, U> map = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public int add(U value) {
        int addr = nextFree.incrementAndGet();
        map.put((T)Integer.valueOf(addr), value);
        return addr;
    }

    public void update(T key, U value){
        map.replace(key, value);
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
        StringBuilder builder = new StringBuilder();
        for(Map.Entry<T, U> e : map.entrySet()){
            builder.append(e.getKey()).append("-->").append(e.getValue()).append("\n");
        }
        return builder.toString();
    }

    public Set<Map.Entry<T, U>> getContent(){
        return map.entrySet();
    }

    public void setContent(Map<T, U> newHeap){
        map = newHeap;
    }

    public List<T> getKeys() {
        return new ArrayList<>(map.keySet());
    }

    public List<U> getValues() {
        return new ArrayList<>(map.values());
    }

}
