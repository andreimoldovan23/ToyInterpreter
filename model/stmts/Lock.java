package model.stmts;

import exceptions.*;
import model.PrgState;
import model.adts.ISymTable;
import model.adts.ITypeEnv;
import model.adts.LockTable;
import model.types.Int;
import model.types.Type;
import model.values.Value;

public class Lock implements Stmt {

    private final String varName;

    public Lock(String s) {
        varName = s;
    }

    public String toString() {
        return "lock(" + varName + ") \n";
    }

    public String toStringPrefix(String prefix) {
        return prefix + toString();
    }

    @SuppressWarnings("DuplicatedCode")
    public PrgState exec(PrgState state) throws ThreadException {
        ISymTable<String, Value> table = state.getTable();
        LockTable<Integer, Integer> lockTable = state.getLockTable();

        if(!table.isDefined(varName))
            throw new ThreadException(new IsNotDefined(), state.getId());
        Value val = table.lookup(varName);
        if(!(val.getType() instanceof Int))
            throw new ThreadException(new InvalidLock(), state.getId());
        if(lockTable.lookup((Integer)val.getValue()) == null)
            throw new ThreadException(new NonExistingLock(), state.getId());

        boolean hasLocked = lockTable.update((Integer)val.getValue(), state.getId(), true);
        if(!hasLocked)
            state.getStack().push(this);
        return null;
    }

    public ITypeEnv<String, Type> typeCheck(ITypeEnv<String, Type> typeEnv) throws MyException {
        if(!(typeEnv.lookup(varName) instanceof Int))
            throw new InvalidLock();
        return typeEnv;
    }

}
