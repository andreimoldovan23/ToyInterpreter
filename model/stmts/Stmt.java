package ToyInterpreter.model.stmts;

import ToyInterpreter.exceptions.MyException;
import ToyInterpreter.model.PrgState;

public interface Stmt {
    String toString();
    String toStringPrefix(String prefix);
    PrgState exec(PrgState state) throws MyException;
}
