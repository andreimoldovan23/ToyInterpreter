package model.stmts;

import exceptions.*;
import model.PrgState;
import model.adts.IHeap;
import model.adts.ISymTable;
import model.adts.ITypeEnv;
import model.exps.Exp;
import model.types.Ref;
import model.types.Type;
import model.values.RefValue;
import model.values.Value;

public class New implements Stmt{

    private final String varName;
    private final Exp exp;

    public New(String v, Exp e){
        varName = v;
        exp = e;
    }

    public String toString(){
        return "new (" + varName + ", " + exp.toString() + ") \n";
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
            throw new ThreadException(new IsNotDefined(), state.getId());
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
            throw new IsNotDefined();

        Type varType = typeEnv.lookup(varName);
        if(!(varType instanceof Ref))
            throw new InvalidAllocation();

        Type expected = exp.typeCheck(typeEnv);
        if(!varType.equals(new Ref(expected)))
            throw new InvalidAllocationType();

        return typeEnv;
    }

}
