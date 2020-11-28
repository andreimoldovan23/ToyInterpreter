package ToyInterpreter.exceptions;

public class InvalidVariable extends MyException{

    public InvalidVariable(){
        super();
    }

    public String toString() {
        return "Invalid variable declaration";
    }

}
