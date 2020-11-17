package ToyInterpreter.model.stmts;

import ToyInterpreter.exceptions.InvalidVariable;
import ToyInterpreter.exceptions.IsAlreadyDefined;
import ToyInterpreter.exceptions.IsNotDefinedException;
import ToyInterpreter.exceptions.MyException;
import ToyInterpreter.model.PrgState;
import ToyInterpreter.model.adts.ISymTable;
import ToyInterpreter.model.exps.Exp;
import ToyInterpreter.model.exps.VarExp;
import ToyInterpreter.model.types.*;
import ToyInterpreter.model.values.Value;

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
                case "string" -> table.add(exp.toString(), StringType.defaultValue());
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
