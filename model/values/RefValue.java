package ToyInterpreter.model.values;

import ToyInterpreter.model.types.Ref;
import ToyInterpreter.model.types.Type;

public class RefValue implements Value {

    private final int address;
    private final Type locationType;

    public RefValue(int a, Type t){
        address = a;
        locationType = t;
    }

    public Type getType() {
        return new Ref(locationType);
    }

    public Object getValue() {
        return address;
    }

    public boolean equals(Object other){
        return other instanceof RefValue;
    }

    public String toString(){
        return "(" + address + " -> " + locationType.toString() + ")";
    }

    public Value copy() {
        return new RefValue(address, locationType.copy());
    }

}
