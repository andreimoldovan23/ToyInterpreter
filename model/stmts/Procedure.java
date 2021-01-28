package model.stmts;

import exceptions.MyException;
import exceptions.ThreadException;
import javafx.util.Pair;
import model.PrgState;
import model.adts.ITypeEnv;
import model.types.Type;
import java.util.List;

public class Procedure implements Stmt{

    private final String procName;
    private final Stmt stmt;
    private final List<String> arguments;

    public Procedure(String n, List<String> l, Stmt sm) {
        procName = n;
        arguments = l;
        stmt = sm;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for(var a: arguments) stringBuilder.append(a).append(", ");
        stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
        return "procedure " + procName + " (" + stringBuilder.toString() + ") {\n" + stmt.toStringPrefix("\t") + "} \n";
    }

    public String toStringPrefix(String prefix) {
        StringBuilder stringBuilder = new StringBuilder();
        for(var a: arguments) stringBuilder.append(a).append(", ");
        stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
        return prefix + "procedure " + procName + " (" + stringBuilder.toString() +
                ") {\n" + stmt.toStringPrefix(prefix + "\t") + "} \n";
    }

    public PrgState exec(PrgState state) throws ThreadException {
        state.getProcTable().add(procName, new Pair<>(arguments, stmt));
        return null;
    }

    public ITypeEnv<String, Type> typeCheck(ITypeEnv<String, Type> typeEnv) throws MyException {
        return typeEnv;
    }

}
