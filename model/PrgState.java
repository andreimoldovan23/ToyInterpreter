package ToyInterpreter.model;

import ToyInterpreter.model.adts.*;
import ToyInterpreter.model.stmts.CompStmt;
import ToyInterpreter.model.stmts.NOP;
import ToyInterpreter.model.stmts.Stmt;
import ToyInterpreter.model.values.StringValue;
import ToyInterpreter.model.values.Value;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class PrgState {

    private final IExeStack<Stmt> stack;
    private final ISymTable<String, Value> table;
    private final IOut<String> out;
    private final IFileTable<StringValue, MyBufferedReader> fileTable;
    private final Stmt initialProgram;

    public PrgState(IExeStack<Stmt> es, ISymTable<String, Value> tbl, IOut<String> out,
                    IFileTable<StringValue, MyBufferedReader> ft, List<Stmt> s){
        stack = es;
        table = tbl;
        this.out = out;
        fileTable = ft;
        initialProgram = assemble(s);
        stack.push(initialProgram);
    }

    public PrgState(IExeStack<Stmt> es, ISymTable<String, Value> tbl, IOut<String> out,
                    IFileTable<StringValue, MyBufferedReader> ft, Stmt s){
        stack = es;
        table = tbl;
        this.out = out;
        fileTable = ft;
        initialProgram = s;
        stack.push(initialProgram);
    }

    public IExeStack<Stmt> getStack(){
        return stack;
    }

    public ISymTable<String, Value> getTable(){
        return table;
    }

    public IOut<String> getOut(){
        return out;
    }

    public IFileTable<StringValue, MyBufferedReader> getFileTable(){
        return fileTable;
    }

    public Stmt getInitialProgram(){
        return initialProgram;
    }

    public static Stmt assemble(List<Stmt> statements){
        Collections.reverse(statements);
        return statements.stream().reduce(new NOP(), (a, b) -> new CompStmt(b, a));
    }

    public String toString(){
        return "ExeStack:\n" + stack.toString() + "\nSymTable:\n" + table.toString() +
                "\nOut:\n" + out.toString() + "\nFileTable:\n" + fileTable.toString() +
                "\n-----------------------------------------------------\n";
    }

    public void reset() throws IOException {
        stack.push(initialProgram);
        out.clear();
        table.clear();
        Set<MyBufferedReader> readers = fileTable.getKeys();
        for(var r : readers)
            r.close();
        fileTable.clear();
        }

}
