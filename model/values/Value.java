package model.values;

import model.types.Type;

public interface Value{
    String toString();
    Type getType();
    Object getValue();
}
