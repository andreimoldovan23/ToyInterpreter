package model.stmts;

import exceptions.MyException;
import exceptions.ThreadException;
import model.PrgState;
import model.adts.ITypeEnv;
import model.types.Type;

public interface Stmt {
    String toString();
    String toStringPrefix(String prefix);
    PrgState exec(PrgState state) throws ThreadException;
    ITypeEnv<String, Type> typeCheck(ITypeEnv<String, Type> typeEnv) throws MyException;
}
