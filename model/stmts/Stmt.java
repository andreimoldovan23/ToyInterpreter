package model.stmts;

import exceptions.MyException;
import model.PrgState;

public interface Stmt {
    String toString();
    String toStringPrefix(String prefix);
    PrgState exec(PrgState state) throws MyException;
}
