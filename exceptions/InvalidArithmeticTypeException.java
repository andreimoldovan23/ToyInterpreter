package exceptions;

public class InvalidArithmeticTypeException extends MyException {
    public InvalidArithmeticTypeException(){
        super();
    }

    public String toString(){
        return "The values provided should be integers!";
    }
}
