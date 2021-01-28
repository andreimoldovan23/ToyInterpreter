package model.exps;

import exceptions.MyException;
import model.adts.IHeap;
import model.adts.ISymTable;
import model.adts.ITypeEnv;
import model.types.Type;
import model.values.Value;

public interface Exp {
    Value eval(ISymTable<String, Value> table, IHeap<Integer, Value> heap) throws MyException;
    String toString();
    boolean equals(Object other);
    Type typeCheck(ITypeEnv<String, Type> typeEnv) throws MyException;
}
