package ToyInterpreter.exceptions;

public class InvalidAllocation extends MyException {

    public InvalidAllocation(){
        super();
    }

    public String toString(){
        return "Cannot allocate memory for non-reference variables";
    }

}
