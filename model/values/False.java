package ToyInterpreter.model.values;

import ToyInterpreter.model.types.Bool;
import ToyInterpreter.model.types.Type;

public class False implements Value {

    public String toString(){
        return "false";
    }

    public Type getType(){
        return new Bool();
    }

    public Boolean getValue(){
        return false;
    }

    public boolean equals(Object other){
        return other instanceof False;
    }

    public Value copy() {
        return new False();
    }

}
