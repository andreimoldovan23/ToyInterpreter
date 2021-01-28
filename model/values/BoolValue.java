package model.values;

import model.types.Type;
import model.types.Bool;

public class BoolValue implements Value {

    private final boolean val;

    public BoolValue(boolean b){
        val = b;
    }

    public Boolean getValue(){
        return val;
    }

    public Type getType(){
        return new Bool();
    }

    public String toString(){
        return Boolean.toString(val);
    }

    public boolean equals(Object other){
        return other instanceof BoolValue;
    }

    public Value copy() {
        return new BoolValue(val);
    }

}
