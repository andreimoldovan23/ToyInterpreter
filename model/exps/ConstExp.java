package model.exps;

import model.adts.ISymTable;
import model.values.Value;

public class ConstExp implements Exp{

    private final Value val;

    public ConstExp(Value v){
        val = v;
    }

    public Value eval(ISymTable<String, Value> table){
        return val;
    }

    public String toString(){
        return val.toString();
    }

    public boolean equals(Object other) {
        return other instanceof ConstExp;
    }

}
