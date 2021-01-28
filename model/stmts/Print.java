package model.stmts;

import exceptions.MyException;
import exceptions.ThreadException;
import model.PrgState;
import model.adts.IHeap;
import model.adts.IOut;
import model.adts.ISymTable;
import model.adts.ITypeEnv;
import model.exps.Exp;
import model.types.Type;
import model.values.Value;

public class Print implements Stmt{

    private final Exp exp;

    public Print(Exp e){
        exp = e;
    }

    public PrgState exec(PrgState state) throws ThreadException {
        ISymTable<String, Value> table = state.getTable();
        IHeap<Integer, Value> heap = state.getHeap();
        IOut<String> out = state.getOut();
        Value v;
        try{
            v = exp.eval(table, heap);
        }
        catch (MyException e){
            throw new ThreadException(e, state.getId());
        }

        out.add(v.toString());
        return null;
    }

    public ITypeEnv<String, Type> typeCheck(ITypeEnv<String, Type> typeEnv) throws MyException {
        exp.typeCheck(typeEnv);
        return typeEnv;
    }

    public String toString(){
        return "print " + exp.toString() + " \n";
    }

    public String toStringPrefix(String prefix){
        return prefix + toString();
    }

}
