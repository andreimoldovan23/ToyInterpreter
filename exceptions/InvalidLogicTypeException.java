package exceptions;

public class InvalidLogicTypeException extends MyException {
    public InvalidLogicTypeException() {
        super();
    }
    public String toString(){
        return "The values provided should be booleans!";
    }


}
