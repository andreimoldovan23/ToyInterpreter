package model.types;

import model.values.IntValue;
import model.values.Value;

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
