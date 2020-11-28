package ToyInterpreter.model.stmts;

import ToyInterpreter.exceptions.*;
import ToyInterpreter.model.PrgState;
import ToyInterpreter.model.adts.IHeap;
import ToyInterpreter.model.adts.ISymTable;
import ToyInterpreter.model.adts.ITypeEnv;
import ToyInterpreter.model.exps.Exp;
import ToyInterpreter.model.types.Ref;
import ToyInterpreter.model.types.Type;
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
    public PrgState exec(PrgState state) throws ThreadException {
        ISymTable<String, Value> table = state.getTable();
        IHeap<Integer, Value> heap = state.getHeap();

        if(!table.isDefined(varName))
            throw new ThreadException(new IsNotDefinedException(), state.getId());
        Value val = table.lookup(varName);
        if(!(val.getType() instanceof Ref))
            throw new ThreadException(new InvalidAllocation(), state.getId());

        Value expVal;
        try{
            expVal = exp.eval(table, heap);
        }
        catch (MyException e){
            throw new ThreadException(e, state.getId());
        }

        if(!((Ref)val.getType()).getInner().equals(expVal.getType()))
            throw new ThreadException(new InvalidAllocationType(), state.getId());

        int addr = (int) val.getValue();
        if(!heap.isDefined(addr))
            throw new ThreadException(new NotAllocated(), state.getId());

        heap.update(addr, expVal);
        return null;
    }

    public ITypeEnv<String, Type> typeCheck(ITypeEnv<String, Type> typeEnv) throws MyException {
        if(!typeEnv.isDefined(varName))
            throw new IsNotDefinedException();

        Type expType = exp.typeCheck(typeEnv);
        Type varType = typeEnv.lookup(varName);

        if(!(varType instanceof Ref))
            throw new InvalidAllocation();

        if(!((Ref) varType).getInner().equals(expType))
            throw new InvalidAllocationType();

        return typeEnv;
    }

}
