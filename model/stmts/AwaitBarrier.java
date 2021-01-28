package model.stmts;

import exceptions.*;
import model.Barrier;
import model.PrgState;
import model.adts.BarrierTable;
import model.adts.ISymTable;
import model.adts.ITypeEnv;
import model.types.Int;
import model.types.Type;
import model.values.Value;

import java.util.List;

public class AwaitBarrier implements Stmt {

    private final String varName;

    public AwaitBarrier(String s) {
        varName = s;
    }

    public String toString() {
        return "awaitBarrier(" + varName + ") \n";
    }

    public String toStringPrefix(String prefix) {
        return prefix + toString();
    }

    public PrgState exec(PrgState state) throws ThreadException {
        ISymTable<String, Value> table = state.getTable();
        BarrierTable<Integer, Barrier<Integer, List<Integer>>> barrierTable = state.getBarrierTable();

        if(!table.isDefined(varName))
            throw new ThreadException(new IsNotDefined(), state.getId());
        Value val = table.lookup(varName);
        if(!(val.getType() instanceof Int))
            throw new ThreadException(new InvalidBarrierType(), state.getId());

        Barrier<Integer, List<Integer>> barrierVal = barrierTable.lookup((Integer)val.getValue());
        if(barrierVal == null)
            throw new ThreadException(new NonExistingBarrier(), state.getId());

        boolean isWaiting = barrierVal.awaitBarrier(state.getId());
        if(isWaiting) {
            state.getStack().push(this);
        }
        return null;
    }

    public ITypeEnv<String, Type> typeCheck(ITypeEnv<String, Type> typeEnv) throws MyException {
        if(!(typeEnv.lookup(varName) instanceof Int))
            throw new InvalidBarrierType();
        return typeEnv;
    }
}
