package model.stmts;

import exceptions.InvalidAssignType;
import exceptions.InvalidVariable;
import exceptions.MyException;
import exceptions.ThreadException;
import model.PrgState;
import model.adts.IHeap;
import model.adts.ISymTable;
import model.adts.ITypeEnv;
import model.exps.Exp;
import model.exps.VarExp;
import model.types.Type;
import model.values.Value;

public class Assign implements Stmt{

    private final Exp left;
    private final Exp right;

    public Assign(Exp e1, Exp e2) {
        left = e1;
        right = e2;
    }

    public PrgState exec(PrgState state) throws ThreadException {
        ISymTable<String, Value> table = state.getTable();
        IHeap<Integer, Value> heap = state.getHeap();
        Value v1, v2;
        try {
            v1 = left.eval(table, heap);
            v2 = right.eval(table, heap);
        }
        catch (MyException e){
            throw new ThreadException(e, state.getId());
        }

        if(v1.getType().equals(v2.getType()))
            table.update(left.toString(), v2);
        else
            throw new ThreadException(new InvalidAssignType(), state.getId());
        return null;
    }

    public ITypeEnv<String, Type> typeCheck(ITypeEnv<String, Type> typeEnv) throws MyException {
        if(!left.equals(new VarExp("default")))
            throw new InvalidVariable();
        Type t1 = left.typeCheck(typeEnv);
        Type t2 = right.typeCheck(typeEnv);
        if(!t1.equals(t2))
            throw new InvalidAssignType();
        return typeEnv;
    }

    public String toString(){
        return left.toString() + "=" + right.toString() + " \n";
    }

    public String toStringPrefix(String prefix){
        return prefix + toString();
    }

}
