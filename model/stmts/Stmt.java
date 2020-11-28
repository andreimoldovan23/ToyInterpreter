package ToyInterpreter.model.stmts;

import ToyInterpreter.exceptions.MyException;
import ToyInterpreter.exceptions.ThreadException;
import ToyInterpreter.model.PrgState;
import ToyInterpreter.model.adts.ITypeEnv;
import ToyInterpreter.model.types.Type;

public interface Stmt {
    String toString();
    String toStringPrefix(String prefix);
    PrgState exec(PrgState state) throws ThreadException;
    ITypeEnv<String, Type> typeCheck(ITypeEnv<String, Type> typeEnv) throws MyException;
}
