package ToyInterpreter.exceptions;

public class NotAllocated extends MyException{

    public NotAllocated(){
        super();
    }

    public String toString(){
        return "No memory has yet been allocated for that variable";
    }

}
