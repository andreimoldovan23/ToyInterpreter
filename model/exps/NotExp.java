package model.exps;

import exceptions.InvalidWhileType;
import exceptions.MyException;
import model.adts.IHeap;
import model.adts.ISymTable;
import model.adts.ITypeEnv;
import model.types.Bool;
import model.types.Type;
import model.values.BoolValue;
import model.values.Value;

public class NotExp implements Exp {

    private final Exp exp;

    public NotExp(Exp e) {
        exp = e;
    }

    public boolean equals(Object other) {
        return other instanceof NotExp;
    }

    public String toString() {
        return "!(" + exp.toString() + ")";
    }

    public Value eval(ISymTable<String, Value> table, IHeap<Integer, Value> heap) throws MyException {
        Value val = exp.eval(table, heap);
        if(!(val.getType() instanceof Bool))
            throw new InvalidWhileType();
        return new BoolValue(!(Boolean)val.getValue());
    }

    public Type typeCheck(ITypeEnv<String, Type> typeEnv) throws MyException {
        if(!(exp.typeCheck(typeEnv) instanceof Bool))
            throw new InvalidWhileType();
        return new Bool();
    }

}
