package model.types;

import model.values.False;
import model.values.Value;

public class Bool implements Type{

    public boolean equals(Object another){
        return another instanceof Bool;
    }

    public String toString(){
        return "boolean";
    }

    static public Value defaultValue() {
        return new False();
    }

}
