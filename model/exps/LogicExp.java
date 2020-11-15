package ToyInterpreter.model.exps;

import ToyInterpreter.exceptions.InvalidLogicTypeException;
import ToyInterpreter.exceptions.InvalidOperator;
import ToyInterpreter.exceptions.MyException;
import ToyInterpreter.model.adts.ISymTable;
import ToyInterpreter.model.types.Bool;
import ToyInterpreter.model.values.BoolValue;
import ToyInterpreter.model.values.Value;

public class LogicExp implements Exp {

    private final Exp left;
    private final Exp right;
    private final String op;

    public LogicExp(Exp e1, Exp e2, String o){
        left = e1;
        right = e2;
        op = o;
    }

    public Value eval(ISymTable<String, Value> table) throws MyException{
        Value v1, v2, finalValue;
        v1 = left.eval(table);
        v2 = right.eval(table);

        if(v1.getType().equals(new Bool())){
            if(v2.getType().equals(new Bool())){
                Boolean b1, b2;
                b1 = (Boolean)v1.getValue();
                b2 = (Boolean)v2.getValue();
                switch (op){
                    case "&" -> finalValue = new BoolValue(b1 & b2);
                    case "|" -> finalValue = new BoolValue(b1 | b2);
                    default -> throw new InvalidOperator();
                }
            }
            else
                throw new InvalidLogicTypeException();
        }
        else
            throw new InvalidLogicTypeException();
        return finalValue;
    }

    public String toString(){
        return "(" + left.toString() + " " + op + " " + right.toString() + ")";
    }

    public boolean equals(Object other){
        return other instanceof LogicExp;
    }

}
