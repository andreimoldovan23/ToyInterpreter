package ToyInterpreter.exceptions;

public class IsAlreadyDefined extends MyException{
    public IsAlreadyDefined(){
        super();
    }

    public String toString(){
        return "The variable is already defined";
    }
}
