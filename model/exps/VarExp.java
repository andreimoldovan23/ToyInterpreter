package model.exps;

import exceptions.IsNotDefined;
import exceptions.MyException;
import model.adts.IHeap;
import model.adts.ISymTable;
import model.adts.ITypeEnv;
import model.types.Type;
import model.values.Value;

public class VarExp implements Exp{

    private final String varName;

    public VarExp(String s){
        varName = s;
    }

    public Value eval(ISymTable<String, Value> table, IHeap<Integer, Value> heap) throws IsNotDefined {
        if(table.isDefined(varName))
            return table.lookup(varName);
        throw new IsNotDefined();
    }

    public String toString(){
        return varName;
    }

    public boolean equals(Object other){
        return other instanceof VarExp;
    }

    public Type typeCheck(ITypeEnv<String, Type> typeEnv) throws MyException {
        if(!typeEnv.isDefined(varName))
            throw new IsNotDefined();
        return typeEnv.lookup(varName);
    }

}
