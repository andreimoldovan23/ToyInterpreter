package ToyInterpreter.model.adts;

import ToyInterpreter.exceptions.InvalidAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Heap<T extends Integer, U> implements IHeap<T, U>{

    private int nextFree = 0;
    private int lastOccupiedAddress = 0;
    private Map<T, U> map = new HashMap<>();

    @SuppressWarnings("unchecked")
    private void findFirstFree(){
        for(int i = 1; i < lastOccupiedAddress; i++)
            if(!isDefined((T)Integer.valueOf(i))){
                nextFree = i;
                return;
            }
        nextFree = lastOccupiedAddress + 1;
        lastOccupiedAddress++;
    }

    @SuppressWarnings("unchecked")
    public int add(U value) {
        findFirstFree();
        map.put((T)Integer.valueOf(nextFree), value);
        return nextFree;
    }

    public void update(T key, U value){
        map.replace(key, value);
    }

    public boolean isDefined(T key) {
        return lookup(key) != null;
    }

    public void remove(T key) throws InvalidAddress {
        U val = map.remove(key);
        if (val == null)
            throw new InvalidAddress();
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
            builder.append(e.getKey().toString()).append("=").append(e.getValue().toString())
                    .append("\n");
        }
        return builder.toString();
    }

    public Set<Map.Entry<T, U>> getContent(){
        return map.entrySet();
    }

    public void setContent(Map<T, U> newHeap){
        map = newHeap;
    }

}
