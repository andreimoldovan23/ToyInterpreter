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

    public static Value defaultValue() {
        return new IntValue(0);
    }

    public Type copy() {
        return new Int();
    }

}
