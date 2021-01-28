package model.values;

import model.types.Int;
import model.types.Type;

public class IntValue implements Value{

    private final int val;

    public IntValue(int val){
        this.val = val;
    }

    public Integer getValue(){
        return val;
    }

    public Type getType(){
        return new Int();
    }

    public String toString(){
        return Integer.toString(val);
    }

    public boolean equals(Object other){
        return other instanceof IntValue;
    }

    public Value copy() {
        return new IntValue(val);
    }

}
