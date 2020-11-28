package ToyInterpreter.model.stmts;

import ToyInterpreter.exceptions.InvalidAssignTypeException;
import ToyInterpreter.exceptions.InvalidVariable;
import ToyInterpreter.exceptions.MyException;
import ToyInterpreter.exceptions.ThreadException;
import ToyInterpreter.model.PrgState;
import ToyInterpreter.model.adts.IHeap;
import ToyInterpreter.model.adts.ISymTable;
import ToyInterpreter.model.adts.ITypeEnv;
import ToyInterpreter.model.exps.Exp;
import ToyInterpreter.model.exps.VarExp;
import ToyInterpreter.model.types.Type;
import ToyInterpreter.model.values.Value;

public class AssignStmt implements Stmt{

    private final Exp left;
    private final Exp right;

    public AssignStmt(Exp e1, Exp e2) throws InvalidVariable {
        if(!e1.equals(new VarExp("default")))
            throw new InvalidVariable();
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
            throw new ThreadException(new InvalidAssignTypeException(), state.getId());
        return null;
    }

    public ITypeEnv<String, Type> typeCheck(ITypeEnv<String, Type> typeEnv) throws MyException {
        Type t1 = left.typeCheck(typeEnv);
        Type t2 = right.typeCheck(typeEnv);
        if(!t1.equals(t2))
            throw new InvalidAssignTypeException();
        return typeEnv;
    }

    public String toString(){
        return left.toString() + "=" + right.toString() + '\n';
    }

    public String toStringPrefix(String prefix){
        return prefix + toString();
    }

}
