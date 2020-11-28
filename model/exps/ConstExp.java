package ToyInterpreter.model.exps;

import ToyInterpreter.model.adts.IHeap;
import ToyInterpreter.model.adts.ISymTable;
import ToyInterpreter.model.adts.ITypeEnv;
import ToyInterpreter.model.types.Type;
import ToyInterpreter.model.values.Value;

public class ConstExp implements Exp{

    private final Value val;

    public ConstExp(Value v){
        val = v;
    }

    public Value eval(ISymTable<String, Value> table, IHeap<Integer, Value> heap){
        return val;
    }

    public String toString(){
        return val.toString();
    }

    public boolean equals(Object other) {
        return other instanceof ConstExp;
    }

    public Type typeCheck(ITypeEnv<String, Type> typeEnv) {
        return val.getType();
    }

}
