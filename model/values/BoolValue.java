package ToyInterpreter.model.values;

import ToyInterpreter.model.types.Type;
import ToyInterpreter.model.types.Bool;

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

}
