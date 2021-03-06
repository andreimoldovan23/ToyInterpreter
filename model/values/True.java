package model.values;

import model.types.Bool;
import model.types.Type;

public class True implements Value {

    public String toString(){
        return "true";
    }

    public Type getType(){
        return new Bool();
    }

    public Boolean getValue(){
        return true;
    }

    public boolean equals(Object other){
        return other instanceof True;
    }

    public Value copy() {
        return new True();
    }

}
