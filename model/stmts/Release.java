package model.stmts;

import exceptions.*;
import model.PrgState;
import model.Semaphore;
import model.adts.ISymTable;
import model.adts.ITypeEnv;
import model.adts.SemaphoreTable;
import model.types.Int;
import model.types.Type;
import model.values.Value;

import java.util.List;

public class Release implements Stmt {

    private final String varName;

    public Release(String v) {
        varName = v;
    }

    public String toString() {
        return "release (" + varName + ") \n";
    }

    public String toStringPrefix(String prefix) {
        return prefix + toString();
    }

    @SuppressWarnings("DuplicatedCode")
    public PrgState exec(PrgState state) throws ThreadException {
        ISymTable<String, Value> symTable = state.getTable();
        SemaphoreTable<Integer, Semaphore<Integer, List<Integer>>> semTable = state.getSemaphoreTable();

        if(!symTable.isDefined(varName))
            throw new ThreadException(new IsNotDefined(), state.getId());
        Value val = symTable.lookup(varName);
        if(!(val.getType() instanceof Int))
            throw new ThreadException(new InvalidSemaphoreType(), state.getId());

        Semaphore<Integer, List<Integer>> semValue = semTable.lookup((Integer)val.getValue());
        if(semValue == null)
            throw new ThreadException(new NonExistingSemaphore(), state.getId());

        semValue.release(state.getId());
        return null;
    }

    public ITypeEnv<String, Type> typeCheck(ITypeEnv<String, Type> typeEnv) throws MyException {
        if(!(typeEnv.lookup(varName) instanceof Int))
            throw new InvalidSemaphoreType();
        return typeEnv;
    }

}
