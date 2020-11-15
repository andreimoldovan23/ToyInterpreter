package model.stmts;

import exceptions.InvalidAssignTypeException;
import exceptions.InvalidVariable;
import exceptions.MyException;
import model.PrgState;
import model.adts.ISymTable;
import model.exps.Exp;
import model.exps.VarExp;
import model.values.Value;

public class AssignStmt implements Stmt{

    private final Exp left;
    private final Exp right;

    public AssignStmt(Exp e1, Exp e2) throws InvalidVariable {
        if(!e1.equals(new VarExp("default")))
            throw new InvalidVariable();
        left = e1;
        right = e2;
    }

    public PrgState exec(PrgState state) throws MyException{
        ISymTable<String, Value> table = state.getTable();
        Value v1 = left.eval(table);
        Value v2 = right.eval(table);

        if(v1.getType().equals(v2.getType()))
            table.update(left.toString(), v2);
        else
            throw new InvalidAssignTypeException();
        return state;
    }

    public String toString(){
        return left.toString() + "=" + right.toString() + '\n';
    }

    public String toStringPrefix(String prefix){
        return prefix + toString();
    }

}
