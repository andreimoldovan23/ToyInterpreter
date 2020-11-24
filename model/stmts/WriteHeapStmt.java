package ToyInterpreter.model.stmts;

import ToyInterpreter.exceptions.*;
import ToyInterpreter.model.PrgState;
import ToyInterpreter.model.adts.IHeap;
import ToyInterpreter.model.adts.ISymTable;
import ToyInterpreter.model.exps.Exp;
import ToyInterpreter.model.types.Ref;
import ToyInterpreter.model.values.Value;

public class WriteHeapStmt implements Stmt{

    private final String varName;
    private final Exp exp;

    public WriteHeapStmt(String v, Exp e){
        varName = v;
        exp = e;
    }

    public String toString(){
        return "writeHeap (" + varName + ", " + exp.toString() + ")\n";
    }

    public String toStringPrefix(String prefix) {
        return prefix + toString();
    }

    @SuppressWarnings("DuplicatedCode")
    public PrgState exec(PrgState state) throws MyException {
        ISymTable<String, Value> table = state.getTable();
        IHeap<Integer, Value> heap = state.getHeap();

        if(!table.isDefined(varName))
            throw new IsNotDefinedException();
        Value val = table.lookup(varName);
        if(!(val.getType() instanceof Ref))
            throw new InvalidAllocation();

        Value expVal = exp.eval(table, heap);
        if(!((Ref)val.getType()).getInner().equals(expVal.getType()))
            throw new InvalidAllocationType();

        int addr = (int) val.getValue();
        if(!heap.isDefined(addr))
            throw new NotAllocated();

        heap.update(addr, expVal);
        return null;
    }

}
