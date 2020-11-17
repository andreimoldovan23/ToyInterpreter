package ToyInterpreter.model.values;

import ToyInterpreter.model.types.StringType;
import ToyInterpreter.model.types.Type;

public class StringValue implements Value{

    private final String val;

    public StringValue(String s){
        val = s;
    }

    public String toString(){
        return val;
    }

    public Type getType() {
        return new StringType();
    }

    public Object getValue() {
        return val;
    }

    public boolean equals(Object other){
        return other instanceof StringValue;
    }

    public int hashCode(){
        int sum = 0;
        for(int i = 0; i < val.length(); i++){
            int charVal = val.charAt(i);
            sum += charVal;
        }
        return sum;
    }

}
