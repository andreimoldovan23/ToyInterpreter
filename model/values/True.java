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

}
