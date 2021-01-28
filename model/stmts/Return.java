package model.stmts;

import exceptions.MyException;
import exceptions.ThreadException;
import model.PrgState;
import model.adts.ISymTable;
import model.adts.ITypeEnv;
import model.types.Type;
import model.values.Value;
import java.util.List;

public class Return implements Stmt{

    public String toString() {
        return "return \n";
    }

    public String toStringPrefix(String prefix) {
        return prefix + toString();
    }

    public PrgState exec(PrgState state) throws ThreadException {
        List<ISymTable<String, Value>> symTables = state.getAllTables();
        int idx = symTables.size() - 1;
        symTables.remove(idx);
        return null;
    }

    public ITypeEnv<String, Type> typeCheck(ITypeEnv<String, Type> typeEnv) throws MyException {
        return typeEnv;
    }

}
