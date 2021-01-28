package model.stmts;

import exceptions.InvalidIfCondition;
import exceptions.MyException;
import exceptions.ThreadException;
import model.PrgState;
import model.adts.IExeStack;
import model.adts.IHeap;
import model.adts.ISymTable;
import model.adts.ITypeEnv;
import model.exps.Exp;
import model.types.Bool;
import model.types.Type;
import model.values.Value;

public class If implements Stmt{

    private final Exp e;
    private final Stmt s1;
    private final Stmt s2;

    public If(Exp e, Stmt s1, Stmt s2){
        this.e = e;
        this.s1 = s1;
        this.s2 = s2;
    }

    public PrgState exec(PrgState state) throws ThreadException {
        IExeStack<Stmt> stack = state.getStack();
        ISymTable<String, Value> table = state.getTable();
        IHeap<Integer, Value> heap = state.getHeap();
        Value v;

        try{
            v = e.eval(table, heap);
        }
        catch (MyException e){
            throw new ThreadException(e, state.getId());
        }

        if (v.getType().equals(new Bool())) {
            if ((Boolean) v.getValue()) {
                stack.push(s1);
            } else
                stack.push(s2);
        }
        else
            throw new ThreadException(new InvalidIfCondition(), state.getId());
        return null;
    }

    public ITypeEnv<String, Type> typeCheck(ITypeEnv<String, Type> typeEnv) throws MyException {
        Type t = e.typeCheck(typeEnv);
        if(!t.equals(new Bool()))
            throw new InvalidIfCondition();
        s1.typeCheck(typeEnv.copy());
        s2.typeCheck(typeEnv.copy());
        return typeEnv;
    }

    public String toString(){
        return "if " + e.toString() + " then \n" +
                s1.toStringPrefix("\t") + "else \n" + s2.toStringPrefix("\t");
    }

    public String toStringPrefix(String prefix){
        return prefix + "if " + e.toString() + " then \n" +
                s1.toStringPrefix(prefix + "\t") + prefix + "else \n" + s2.toStringPrefix(prefix + "\t");
    }

}
