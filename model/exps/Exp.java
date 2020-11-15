package model.exps;

import exceptions.MyException;
import model.adts.ISymTable;
import model.values.Value;

public interface Exp {
    Value eval(ISymTable<String, Value> table) throws MyException;
    String toString();
    boolean equals(Object other);
}
