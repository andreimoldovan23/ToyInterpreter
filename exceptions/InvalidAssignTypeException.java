package exceptions;

public class InvalidAssignTypeException extends MyException{
    public InvalidAssignTypeException(){
        super();
    }

    public String toString(){
        return "You cannot assign bool value to int variable or vice versa";
    }
}
