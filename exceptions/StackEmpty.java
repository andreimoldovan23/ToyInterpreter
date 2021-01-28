package exceptions;

public class StackEmpty extends MyException{
    public StackEmpty(){
        super();
    }

    public String toString(){
        return "The call stack is empty!!";
    }
}
