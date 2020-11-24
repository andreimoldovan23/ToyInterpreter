package ToyInterpreter.exceptions;

public class InvalidAddress extends MyException{

    public InvalidAddress(){
        super();
    }

    public String toString(){
        return "Invalid address";
    }

}
