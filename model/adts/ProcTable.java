package model.adts;

import javafx.util.Pair;
import model.stmts.Stmt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ProcTable<K extends String, V extends Pair<List<String>, Stmt>> implements IProcTable<K, V> {

    private final Map<K, V> map = new HashMap<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public void add(K name, V def) {
        lock.writeLock().lock();
        map.put(name, def);
        lock.writeLock().unlock();
    }

    public V lookup(K name) {
        lock.readLock().lock();
        V def = map.get(name);
        lock.readLock().unlock();
        return def;
    }

    public void clear() {
        map.clear();
    }

    public List<String> getSignature() {
        List<String> signatures = new ArrayList<>();
        List<String> names = new ArrayList<>(map.keySet());
        List<Pair<List<String>, Stmt>> definitions = new ArrayList<>(map.values());
        int length = names.size();
        for(int i = 0; i < length; i++) {
            List<String> arguments = definitions.get(i).getKey();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(names.get(i)).append(" (");

            for (String argument : arguments) stringBuilder.append(argument).append(", ");

            stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
            stringBuilder.append(")");
            signatures.add(stringBuilder.toString());
        }
        return signatures;
    }

    public List<String> getDefinition() {
        List<String> definitions = new ArrayList<>();
        for(var v: map.values())
            definitions.add(v.getValue().toString());
        return definitions;
    }

    public String toString() {
        List<String> signatures = getSignature();
        List<String> stmts = getDefinition();
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < signatures.size(); i++)
            stringBuilder.append(signatures.get(i)).append(" {\n").append(stmts.get(i));
        stringBuilder.append("}\n");
        return stringBuilder.toString();
    }

}
