package ToyInterpreter.model.exps;

import ToyInterpreter.exceptions.*;
import ToyInterpreter.model.adts.ISymTable;
import ToyInterpreter.model.types.Int;
import ToyInterpreter.model.values.IntValue;
import ToyInterpreter.model.values.Value;

public class ArithmeticExp implements Exp{

    private final Exp left;
    private final Exp right;
    private final String op;

    public ArithmeticExp(Exp e1, Exp e2, String operand){
        left = e1;
        right = e2;
        op = operand;
    }

    public Value eval(ISymTable<String, Value> table) throws MyException {
        Value v1, v2, finalValue;
        v1 = left.eval(table);
        v2 = right.eval(table);

        if(v1.getType().equals(new Int())){
            if(v2.getType().equals(new Int())){
                Integer i1, i2;
                i1 = (Integer)v1.getValue();
                i2 = (Integer)v2.getValue();
                switch (op){
                    case "+" -> finalValue = new IntValue(i1+i2);
                    case "-" -> finalValue = new IntValue(i1-i2);
                    case "*" -> finalValue = new IntValue(i1*i2);
                    case "/" -> {
                        if(i2 == 0)
                            throw new DivisionByZeroException();
                        finalValue = new IntValue(i1/i2);
                    }
                    default -> throw new InvalidOperator();
                }
            }
            else
                throw new InvalidArithmeticTypeException();
        }
        else
            throw new InvalidArithmeticTypeException();
        return finalValue;
    }

    public String toString(){
        return "(" + left.toString() + " " + op + " " + right.toString() + ")";
    }

    public boolean equals(Object other){
        return other instanceof ArithmeticExp;
    }

}
