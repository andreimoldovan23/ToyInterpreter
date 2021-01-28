package model.adts;

import javafx.util.Pair;
import model.stmts.Stmt;
import java.util.List;

public interface IProcTable<K extends String, V extends Pair<List<String>, Stmt>> {
    void add(K name, V def);
    V lookup(K name);
    String toString();
    void clear();
    List<String> getSignature();
    List<String> getDefinition();
}
