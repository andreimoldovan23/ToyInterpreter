package ToyInterpreter.exceptions;

public class InvalidWhileTypeException extends MyException {

    public InvalidWhileTypeException(){
        super();
    }

    public String toString(){
        return "The while condition is not a boolean";
    }

}
