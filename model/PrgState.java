package model;

import exceptions.MyException;
import javafx.util.Pair;
import model.adts.*;
import model.stmts.Stmt;
import model.values.StringValue;
import model.values.Value;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("FieldMayBeFinal")
public class PrgState {

    private static AtomicInteger id = new AtomicInteger(0);
    private final int thisStateId;
    private final IExeStack<Stmt> stack;
    private final List<ISymTable<String, Value>> tables;
    private final IOut<String> out;
    private final IFileTable<StringValue, MyBufferedReader> fileTable;
    private final IHeap<Integer, Value> heap;
    private final SemaphoreTable<Integer, Semaphore<Integer, List<Integer>>> semaphoreTable;
    private final BarrierTable<Integer, Barrier<Integer, List<Integer>>> barrierTable;
    private final CountDownTable<Integer, Integer> countDownTable;
    private final LockTable<Integer, Integer> lockTable;
    private final IProcTable<String, Pair<List<String>, Stmt>> procTable;

    public PrgState(IExeStack<Stmt> es, IProcTable<String, Pair<List<String>, Stmt>> pt,
                    List<ISymTable<String, Value>> t, IOut<String> o,
                    IFileTable<StringValue, MyBufferedReader> ft, IHeap<Integer, Value> h,
                    SemaphoreTable<Integer, Semaphore<Integer, List<Integer>>> semT,
                    CountDownTable<Integer, Integer> countT,
                    BarrierTable<Integer, Barrier<Integer, List<Integer>>> b,
                    LockTable<Integer, Integer> lt,
                    Stmt s){
        stack = es;
        procTable = pt;
        tables = t;
        out = o;
        fileTable = ft;
        heap = h;
        semaphoreTable = semT;
        countDownTable = countT;
        barrierTable = b;
        lockTable = lt;
        stack.push(s);
        thisStateId = id.incrementAndGet();
    }

    public IExeStack<Stmt> getStack(){
        return stack;
    }

    public ISymTable<String, Value> getTable(){
        return tables.get(tables.size() - 1);
    }

    public List<ISymTable<String, Value>> getAllTables() {
        return tables;
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

    public SemaphoreTable<Integer, Semaphore<Integer, List<Integer>>> getSemaphoreTable() {
        return semaphoreTable;
    }

    public CountDownTable<Integer, Integer> getCountDownTable() {
        return countDownTable;
    }

    public BarrierTable<Integer, Barrier<Integer, List<Integer>>> getBarrierTable() {
        return barrierTable;
    }

    public LockTable<Integer, Integer> getLockTable() {
        return lockTable;
    }

    public IProcTable<String, Pair<List<String>, Stmt>> getProcTable() {
        return procTable;
    }

    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        for(var v: tables)
            stringBuilder.append("\nSymTable:\n").append(v);

        return "Program: " + thisStateId + "\n" +
                "ExeStack:\n" + stack +
                "ProcTable:\n" + procTable + stringBuilder +
                "\nOut:\n" + out + "\nFileTable:\n" + fileTable +
                "\nHeap:\n" + heap +
                "\nSemaphoreTable:\n" + semaphoreTable +
                "\nCountDownTable:\n" + countDownTable +
                "\nBarrierTable:\n" + barrierTable +
                "\nLockTable:\n" + lockTable +
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
