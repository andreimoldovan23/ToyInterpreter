package model.stmts;

import exceptions.MyException;
import model.PrgState;
import model.adts.IOut;
import model.adts.ISymTable;
import model.exps.Exp;
import model.values.Value;

public class PrintStmt implements Stmt{

    private final Exp exp;

    public PrintStmt(Exp e){
        exp = e;
    }

    public PrgState exec(PrgState state) throws MyException{
        ISymTable<String, Value> table = state.getTable();
        IOut<String> out = state.getOut();
        Value v = exp.eval(table);
        out.add(v.toString());
        return state;
    }

    public String toString(){
        return "print " + exp.toString() + "\n";
    }

    public String toStringPrefix(String prefix){
        return prefix + toString();
    }

}
