package model.exps;

import exceptions.InvalidOperator;
import exceptions.InvalidRelationalType;
import exceptions.MyException;
import model.adts.IHeap;
import model.adts.ISymTable;
import model.adts.ITypeEnv;
import model.types.Bool;
import model.types.Int;
import model.types.Type;
import model.values.BoolValue;
import model.values.Value;

public class RelationalExp implements Exp {

    private final Exp left, right;
    private final String op;

    public RelationalExp(Exp e1, Exp e2, String operator){
        left = e1;
        right = e2;
        op = operator;
    }

    public boolean equals(Object other){
        return other instanceof RelationalExp;
    }

    public String toString(){
        return "(" + left.toString() + " " + op + " " + right.toString() + ")";
    }

    public Value eval(ISymTable<String, Value> table, IHeap<Integer, Value> heap) throws MyException {
        Value v1, v2, result;
        v1 = left.eval(table, heap);
        v2 = right.eval(table, heap);

        if(v1.getType().equals(new Int())){
            if(v2.getType().equals(new Int())){
                Integer i1, i2;
                i1 = (Integer) v1.getValue();
                i2 = (Integer) v2.getValue();
                switch (op){
                    case "<=" -> result = new BoolValue(i1 <= i2);
                    case ">=" -> result = new BoolValue(i1 >= i2);
                    case "==" -> result = new BoolValue(i1.equals(i2));
                    case "<" -> result = new BoolValue(i1 < i2);
                    case ">" -> result = new BoolValue(i1 > i2);
                    case "!=" -> result = new BoolValue(!i1.equals(i2));
                    default -> throw new InvalidOperator();
                }
            }
            else
                throw new InvalidRelationalType();
        }
        else
            throw new InvalidRelationalType();
        return result;
    }

    public Type typeCheck(ITypeEnv<String, Type> typeEnv) throws MyException {
        Type t1 = left.typeCheck(typeEnv);
        Type t2 = right.typeCheck(typeEnv);
        if(!t1.equals(new Int()) || !t2.equals(new Int()))
            throw new InvalidRelationalType();
        return new Bool();
    }

}
