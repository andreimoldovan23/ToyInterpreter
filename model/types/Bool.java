package ToyInterpreter.model.types;

import ToyInterpreter.model.values.False;
import ToyInterpreter.model.values.Value;

public class Bool implements Type{

    public boolean equals(Object another){
        return another instanceof Bool;
    }

    public String toString(){
        return "boolean";
    }

    public static Value defaultValue() {
        return new False();
    }

    public Type copy() {
        return new Bool();
    }

}
