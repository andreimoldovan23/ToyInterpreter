package model.stmts;

import exceptions.InvalidIfCondition;
import exceptions.MyException;
import model.PrgState;
import model.adts.IExeStack;
import model.adts.ISymTable;
import model.exps.Exp;
import model.types.Bool;
import model.values.Value;

public class IfStmt implements Stmt{

    private final Exp e;
    private final Stmt s1;
    private final Stmt s2;

    public IfStmt(Exp e, Stmt s1, Stmt s2){
        this.e = e;
        this.s1 = s1;
        this.s2 = s2;
    }

    public PrgState exec(PrgState state) throws MyException{
        IExeStack<Stmt> stack = state.getStack();
        ISymTable<String, Value> table = state.getTable();
        Value v = e.eval(table);

        if (v.getType().equals(new Bool())) {
            if ((Boolean) v.getValue()) {
                stack.push(s1);
            } else
                stack.push(s2);
        }
        else
            throw new InvalidIfCondition();
        return state;
    }

    public String toString(){
        return "if " + e.toString() + " then \n" +
                s1.toStringPrefix("\t") + "else \n" + s2.toStringPrefix("\t");
    }

    public String toStringPrefix(String prefix){
        String actual = prefix + "\t";
        return prefix + "if " + e.toString() + " then \n" +
                s1.toStringPrefix(actual) + prefix + "else \n" + s2.toStringPrefix(actual);
    }

}
