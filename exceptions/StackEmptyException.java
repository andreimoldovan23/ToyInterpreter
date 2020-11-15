package exceptions;

public class StackEmptyException extends MyException{
    public StackEmptyException(){
        super();
    }

    public String toString(){
        return "The call stack is empty!!";
    }
}
