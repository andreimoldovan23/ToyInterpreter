package ToyInterpreter.model.exps;

import ToyInterpreter.exceptions.MyException;
import ToyInterpreter.model.adts.ISymTable;
import ToyInterpreter.model.values.Value;

public interface Exp {
    Value eval(ISymTable<String, Value> table) throws MyException;
    String toString();
    boolean equals(Object other);
}
