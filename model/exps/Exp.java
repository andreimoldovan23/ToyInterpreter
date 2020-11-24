package ToyInterpreter.model.exps;

import ToyInterpreter.exceptions.MyException;
import ToyInterpreter.model.adts.IHeap;
import ToyInterpreter.model.adts.ISymTable;
import ToyInterpreter.model.values.Value;

public interface Exp {
    Value eval(ISymTable<String, Value> table, IHeap<Integer, Value> heap) throws MyException;
    String toString();
    boolean equals(Object other);
}
