package model.stmts;

import exceptions.InvalidProcCall;
import exceptions.MyException;
import exceptions.NonExistingProc;
import exceptions.ThreadException;
import javafx.util.Pair;
import model.PrgState;
import model.adts.ISymTable;
import model.adts.ITypeEnv;
import model.adts.SymTable;
import model.exps.Exp;
import model.types.Type;
import model.values.Value;

import java.util.List;

public class CallProc implements Stmt{

    private final String procName;
    private final List<Exp> exps;

    public CallProc(String p, List<Exp> e) {
        procName = p;
        exps = e;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for(var e: exps)
            stringBuilder.append(e).append(", ");
        stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
        return "call " + procName + " (" + stringBuilder.toString() + ") \n";
    }

    public String toStringPrefix(String prefix) {
        return prefix + toString();
    }

    public PrgState exec(PrgState state) throws ThreadException {
        Pair<List<String>, Stmt> procDef = state.getProcTable().lookup(procName);
        if(procDef == null)
            throw new ThreadException(new NonExistingProc(), state.getId());

        List<String> arguments = procDef.getKey();
        if(arguments.size() != exps.size())
            throw new ThreadException(new InvalidProcCall(), state.getId());

        ISymTable<String, Value> newTable = new SymTable<>();
        int size = arguments.size();
        for(int i = 0; i < size; i++) {
            try{
                newTable.add(arguments.get(i), exps.get(i).eval(state.getTable(), state.getHeap()));
            }
            catch (MyException me) {
                throw new ThreadException(me, state.getId());
            }
        }

        state.getAllTables().add(newTable);
        Stmt stmt = state.getProcTable().lookup(procName).getValue();
        state.getStack().push(new Return());
        state.getStack().push(stmt);
        return null;
    }

    public ITypeEnv<String, Type> typeCheck(ITypeEnv<String, Type> typeEnv) throws MyException {
        for(var e: exps)
            e.typeCheck(typeEnv);
        return typeEnv;
    }

}
