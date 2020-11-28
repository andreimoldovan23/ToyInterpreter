package ToyInterpreter.model.types;

import ToyInterpreter.model.values.RefValue;
import ToyInterpreter.model.values.Value;

public class Ref implements Type {

    private final Type innerType;

    public Ref(Type t){
        innerType = t;
    }

    public boolean equals(Object other){
        return other instanceof Ref && innerType.equals(((Ref) other).getInner());
    }

    public Type getInner(){
        return innerType;
    }

    public String toString(){
        return "ref (" + innerType.toString() + ")";
    }

    public static Value defaultValue(Type t){
        return new RefValue(0, t);
    }

    public Type copy() {
        return new Ref(innerType.copy());
    }

}
