package model.stmts;

import exceptions.InvalidVariable;
import exceptions.IsAlreadyDefined;
import exceptions.IsNotDefinedException;
import exceptions.MyException;
import model.PrgState;
import model.adts.ISymTable;
import model.exps.Exp;
import model.exps.VarExp;
import model.types.*;
import model.values.Value;

public class VarDecl implements Stmt {

    private final Exp exp;
    private final String type;

    public VarDecl(Type t, Exp e) throws InvalidVariable {
        if(!e.equals(new VarExp("default")))
            throw new InvalidVariable();
        exp = e;
        type = t.toString();
    }

    public PrgState exec(PrgState state) throws MyException{
        ISymTable<String, Value> table = state.getTable();
        try {
            exp.eval(table);
            throw new IsAlreadyDefined();
        }
        catch (IsNotDefinedException e) {
            switch (type) {
                case "int" -> table.add(exp.toString(), Int.defaultValue());
                case "boolean" -> table.add(exp.toString(), Bool.defaultValue());
            }
            return state;
        }
    }

    public String toString() {
        return type + " " + exp.toString() + "\n";
    }

    public String toStringPrefix(String prefix){
        return prefix + toString();
    }

}
