package ToyInterpreter.model.types;

import ToyInterpreter.model.values.IntValue;
import ToyInterpreter.model.values.Value;

public class Int implements Type{

    public boolean equals(Object another){
        return another instanceof Int;
    }

    public String toString(){
        return "int";
    }

    static public Value defaultValue() {
        return new IntValue(0);
    }

}
