package exceptions;

public class InvalidIfCondition extends MyException {
    public InvalidIfCondition(){
        super();
    }

    public String toString(){
        return "The condition in the if statement does not return a boolean";
    }
}
