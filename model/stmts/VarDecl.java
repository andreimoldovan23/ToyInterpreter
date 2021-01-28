package model.stmts;

import exceptions.*;
import model.PrgState;
import model.adts.IHeap;
import model.adts.ISymTable;
import model.adts.ITypeEnv;
import model.exps.Exp;
import model.exps.VarExp;
import model.types.*;
import model.values.Value;

public class VarDecl implements Stmt {

    private final Exp exp;
    private final Type type;

    public VarDecl(Type t, Exp e) {
        exp = e;
        type = t;
    }

    public PrgState exec(PrgState state) throws ThreadException {
        ISymTable<String, Value> table = state.getTable();
        IHeap<Integer, Value> heap = state.getHeap();
        try {
            exp.eval(table, heap);
            throw new ThreadException(new IsAlreadyDefined(), state.getId());
        }
        catch (IsNotDefined e) {
            switch (type.toString()) {
                case "int" -> table.add(exp.toString(), Int.defaultValue());
                case "boolean" -> table.add(exp.toString(), Bool.defaultValue());
                case "string" -> table.add(exp.toString(), StringType.defaultValue());
            }
            if(type.toString().contains("ref"))
                table.add(exp.toString(),
                        Ref.defaultValue(((Ref)type).getInner()));
            return null;
        }
        catch (MyException me){
            throw (ThreadException)me;
        }
    }

    public String toString() {
        return type.toString() + " " + exp.toString() + " \n";
    }

    public String toStringPrefix(String prefix){
        return prefix + toString();
    }

    public ITypeEnv<String, Type> typeCheck(ITypeEnv<String, Type> typeEnv) throws InvalidVariable {
        if(!exp.equals(new VarExp("default")))
            throw new InvalidVariable();
        typeEnv.add(exp.toString(), type);
        return typeEnv;
    }

}
