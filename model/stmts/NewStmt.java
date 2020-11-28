package ToyInterpreter.model.stmts;

import ToyInterpreter.exceptions.*;
import ToyInterpreter.model.PrgState;
import ToyInterpreter.model.adts.IHeap;
import ToyInterpreter.model.adts.ISymTable;
import ToyInterpreter.model.adts.ITypeEnv;
import ToyInterpreter.model.exps.Exp;
import ToyInterpreter.model.types.Ref;
import ToyInterpreter.model.types.Type;
import ToyInterpreter.model.values.RefValue;
import ToyInterpreter.model.values.Value;

public class NewStmt implements Stmt{

    private final String varName;
    private final Exp exp;

    public NewStmt(String v, Exp e){
        varName = v;
        exp = e;
    }

    public String toString(){
        return "new (" + varName + ", " + exp.toString() + ")\n";
    }

    public String toStringPrefix(String prefix) {
        return prefix + toString();
    }

    @SuppressWarnings("DuplicatedCode")
    public PrgState exec(PrgState state) throws ThreadException {
        ISymTable<String, Value> table = state.getTable();
        IHeap<Integer, Value> heap = state.getHeap();
        Value val, expVal;

        if(!table.isDefined(varName))
            throw new ThreadException(new IsNotDefinedException(), state.getId());
        val = table.lookup(varName);
        if(!(val.getType() instanceof Ref))
            throw new ThreadException(new InvalidAllocation(), state.getId());

        try{
            expVal = exp.eval(table, heap);
        }
        catch (MyException e){
            throw new ThreadException(e, state.getId());
        }

        if(!((Ref)val.getType()).getInner().equals(expVal.getType()))
            throw new ThreadException(new InvalidAllocationType(), state.getId());

        int addr = heap.add(expVal);
        table.update(varName, new RefValue(addr, expVal.getType()));
        return null;
    }

    public ITypeEnv<String, Type> typeCheck(ITypeEnv<String, Type> typeEnv) throws MyException {
        if(!typeEnv.isDefined(varName))
            throw new IsNotDefinedException();

        Type varType = typeEnv.lookup(varName);
        if(!(varType instanceof Ref))
            throw new InvalidAllocation();

        Type expected = exp.typeCheck(typeEnv);
        if(!varType.equals(new Ref(expected)))
            throw new InvalidAllocationType();

        return typeEnv;
    }

}
