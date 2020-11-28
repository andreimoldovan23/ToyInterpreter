package ToyInterpreter.model;

import ToyInterpreter.exceptions.MyException;
import ToyInterpreter.model.adts.*;
import ToyInterpreter.model.stmts.Stmt;
import ToyInterpreter.model.values.StringValue;
import ToyInterpreter.model.values.Value;
import java.util.concurrent.atomic.AtomicInteger;

public class PrgState {

    private static AtomicInteger id = new AtomicInteger(0);
    private final int thisStateId;
    private final IExeStack<Stmt> stack;
    private final ISymTable<String, Value> table;
    private final IOut<String> out;
    private final IFileTable<StringValue, MyBufferedReader> fileTable;
    private final IHeap<Integer, Value> heap;
    private final Stmt initialProgram;

    public PrgState(IExeStack<Stmt> es, ISymTable<String, Value> tbl, IOut<String> o,
                    IFileTable<StringValue, MyBufferedReader> ft, IHeap<Integer, Value> h,
                    Stmt s){
        stack = es;
        table = tbl;
        out = o;
        fileTable = ft;
        heap = h;
        initialProgram = s;
        stack.push(initialProgram);
        thisStateId = id.incrementAndGet();
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

    public IHeap<Integer, Value> getHeap(){
        return heap;
    }

    public Stmt getInitialProgram(){
        return initialProgram;
    }

    public String toString(){
        return "Program: " + thisStateId + "\n" +
                "ExeStack:\n" + stack.toString() + "\nSymTable:\n" + table.toString() +
                "\nOut:\n" + out.toString() + "\nFileTable:\n" + fileTable.toString() +
                "\nHeap:\n" + heap.toString() +
                "\n-----------------------------------------------------\n";
    }

    public boolean isNotCompleted() {
        return !stack.empty();
    }

    public PrgState oneStep() throws MyException {
        Stmt top;
        top = stack.pop();
        return top.exec(this);
    }

    public int getId() {
        return thisStateId;
    }

}
