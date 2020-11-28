package ToyInterpreter.model.exps;

import ToyInterpreter.exceptions.IsNotDefinedException;
import ToyInterpreter.exceptions.MyException;
import ToyInterpreter.model.adts.IHeap;
import ToyInterpreter.model.adts.ISymTable;
import ToyInterpreter.model.adts.ITypeEnv;
import ToyInterpreter.model.types.Type;
import ToyInterpreter.model.values.Value;

public class VarExp implements Exp{

    private final String varName;

    public VarExp(String s){
        varName = s;
    }

    public Value eval(ISymTable<String, Value> table, IHeap<Integer, Value> heap) throws IsNotDefinedException {
        if(table.isDefined(varName))
            return table.lookup(varName);
        throw new IsNotDefinedException();
    }

    public String toString(){
        return varName;
    }

    public boolean equals(Object other){
        return other instanceof VarExp;
    }

    public Type typeCheck(ITypeEnv<String, Type> typeEnv) throws MyException {
        if(!typeEnv.isDefined(varName))
            throw new IsNotDefinedException();
        return typeEnv.lookup(varName);
    }

}
