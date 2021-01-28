package model.stmts;

import exceptions.MyException;
import model.PrgState;
import model.adts.ExeStack;
import model.adts.ISymTable;
import model.adts.ITypeEnv;
import model.types.Type;
import model.values.Value;

import java.util.ArrayList;
import java.util.List;

public class Fork implements Stmt {

    private final Stmt stmt;

    public Fork(Stmt s){
        stmt = s;
    }

    public String toString() {
        return "fork (\n" + stmt.toStringPrefix("\t") + ")\n";
    }

    public String toStringPrefix(String prefix) {
        return prefix + "fork (\n" + stmt.toStringPrefix(prefix + prefix) + prefix + ")\n";
    }

    public PrgState exec(PrgState state) {
        List<ISymTable<String, Value>> newTables = new ArrayList<>();
        List<ISymTable<String, Value>> actualTables = state.getAllTables();
        for(var v : actualTables)
            newTables.add(v.copy());
        return new PrgState(new ExeStack<>(), state.getProcTable(), newTables, state.getOut(), state.getFileTable(),
                state.getHeap(), state.getSemaphoreTable(), state.getCountDownTable(), state.getBarrierTable(),
                state.getLockTable(), stmt);
    }

    public ITypeEnv<String, Type> typeCheck(ITypeEnv<String, Type> typeEnv) throws MyException {
        stmt.typeCheck(typeEnv.copy());
        return typeEnv;
    }

}
