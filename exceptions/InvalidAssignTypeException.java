package ToyInterpreter.exceptions;

public class InvalidAssignTypeException extends MyException{
    public InvalidAssignTypeException(){
        super();
    }

    public String toString(){
        return "Mismatch between types in assignment statement";
    }
}
