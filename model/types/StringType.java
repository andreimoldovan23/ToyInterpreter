package ToyInterpreter.model.types;

import ToyInterpreter.model.values.StringValue;
import ToyInterpreter.model.values.Value;

public class StringType implements Type{

    public String toString(){
        return "string";
    }

    public boolean equals(Object other){
        return other instanceof StringType;
    }

    public static Value defaultValue(){
        return new StringValue("");
    }

    public Type copy() {
        return new StringType();
    }
}
