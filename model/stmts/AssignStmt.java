package ToyInterpreter.model.stmts;

import ToyInterpreter.exceptions.InvalidAssignTypeException;
import ToyInterpreter.exceptions.InvalidVariable;
import ToyInterpreter.exceptions.MyException;
import ToyInterpreter.model.PrgState;
import ToyInterpreter.model.adts.IHeap;
import ToyInterpreter.model.adts.ISymTable;
import ToyInterpreter.model.exps.Exp;
import ToyInterpreter.model.exps.VarExp;
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

    public PrgState exec(PrgState state) throws MyException{
        ISymTable<String, Value> table = state.getTable();
        IHeap<Integer, Value> heap = state.getHeap();
        Value v1 = left.eval(table, heap);
        Value v2 = right.eval(table, heap);

        if(v1.getType().equals(v2.getType()))
            table.update(left.toString(), v2);
        else
            throw new InvalidAssignTypeException();
        return null;
    }

    public String toString(){
        return left.toString() + "=" + right.toString() + '\n';
    }

    public String toStringPrefix(String prefix){
        return prefix + toString();
    }

}
