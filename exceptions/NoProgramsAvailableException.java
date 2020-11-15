package ToyInterpreter.exceptions;

public class NoProgramsAvailableException extends MyException{
    public NoProgramsAvailableException(){
        super();
    }

    public String toString(){
        return "There are no programs loaded";
    }
}
