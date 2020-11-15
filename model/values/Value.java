package ToyInterpreter.model.values;

import ToyInterpreter.model.types.Type;

public interface Value{
    String toString();
    Type getType();
    Object getValue();
}
