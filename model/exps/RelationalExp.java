package ToyInterpreter.model.exps;

import ToyInterpreter.exceptions.InvalidOperator;
import ToyInterpreter.exceptions.InvalidRelationalType;
import ToyInterpreter.exceptions.MyException;
import ToyInterpreter.model.adts.IHeap;
import ToyInterpreter.model.adts.ISymTable;
import ToyInterpreter.model.types.Int;
import ToyInterpreter.model.values.BoolValue;
import ToyInterpreter.model.values.Value;

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

}
