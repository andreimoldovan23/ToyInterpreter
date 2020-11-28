package ToyInterpreter.model.stmts;

import ToyInterpreter.exceptions.MyException;
import ToyInterpreter.model.PrgState;
import ToyInterpreter.model.adts.ExeStack;
import ToyInterpreter.model.adts.ITypeEnv;
import ToyInterpreter.model.types.Type;

public class ForkStmt implements Stmt {

    private final Stmt stmt;

    public ForkStmt(Stmt s){
        stmt = s;
    }

    public String toString() {
        return "fork (" +
                stmt.toString()
                        .replaceAll("( \n)+\t+"," ")
                        .replaceAll("(\n$)", "")
                        .replaceAll("[\n]+", " | ") +
                ")\n";
    }

    public String toStringPrefix(String prefix) {
        return prefix + toString();
    }

    public PrgState exec(PrgState state) {
        return new PrgState(new ExeStack<>(), state.getTable().copy(), state.getOut(), state.getFileTable(),
                state.getHeap(), stmt);
    }

    public ITypeEnv<String, Type> typeCheck(ITypeEnv<String, Type> typeEnv) throws MyException {
        stmt.typeCheck(typeEnv.copy());
        return typeEnv;
    }

}
