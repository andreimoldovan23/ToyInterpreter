package ToyInterpreter.model.exps;

import ToyInterpreter.exceptions.MyException;
import ToyInterpreter.model.adts.IHeap;
import ToyInterpreter.model.adts.ISymTable;
import ToyInterpreter.model.adts.ITypeEnv;
import ToyInterpreter.model.types.Type;
import ToyInterpreter.model.values.Value;

public interface Exp {
    Value eval(ISymTable<String, Value> table, IHeap<Integer, Value> heap) throws MyException;
    String toString();
    boolean equals(Object other);
    Type typeCheck(ITypeEnv<String, Type> typeEnv) throws MyException;
}
