package exceptions;

public class InvalidArithmeticType extends MyException {
    public InvalidArithmeticType(){
        super();
    }

    public String toString(){
        return "The values provided should be integers!";
    }
}
