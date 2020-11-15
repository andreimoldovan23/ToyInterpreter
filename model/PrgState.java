package model;

import exceptions.MidExecutionException;
import model.adts.IExeStack;
import model.adts.IOut;
import model.adts.ISymTable;
import model.stmts.CompStmt;
import model.stmts.NOP;
import model.stmts.Stmt;
import model.values.Value;

import java.util.Collections;
import java.util.List;

public class PrgState {
    private IExeStack<Stmt> stack;
    private ISymTable<String, Value> table;
    private IOut<String> out;
    private final Stmt initialProgram;

    public PrgState(IExeStack<Stmt> es, ISymTable<String, Value> tbl, IOut<String> out, List<Stmt> s){
        stack = es;
        table = tbl;
        this.out = out;
        initialProgram = assemble(s);
        stack.push(initialProgram);
    }

    public PrgState(IExeStack<Stmt> es, ISymTable<String, Value> tbl, IOut<String> out, Stmt s){
        stack = es;
        table = tbl;
        this.out = out;
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

    public Stmt getInitialProgram(){
        return initialProgram;
    }

    public static Stmt assemble(List<Stmt> statements){
        Collections.reverse(statements);
        return statements.stream().reduce(new NOP(), (a, b) -> new CompStmt(b, a));
    }

    public String toString(){
        return "ExeStack:\n" + stack.toString() + "\nSymTable:\n" + table.toString() +
                "\nOut:\n" + out.toString() + "\n-----------------------------------------------------\n";
    }

    public void reset() throws MidExecutionException{
        if(stack.empty()){
            stack.push(initialProgram);
            out.clear();
            table.clear();
        }
        else{
            throw new MidExecutionException();
        }
    }
}
