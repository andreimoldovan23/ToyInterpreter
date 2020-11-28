package ToyInterpreter.model.types;

import ToyInterpreter.model.values.Value;

public interface Type {

    boolean equals(Object another);

    String toString();

    static Value defaultValue(){
        return null;
    }

    Type copy();

}
