package ToyInterpreter.exceptions;

public class InvalidFilenameException extends MyException{

    public InvalidFilenameException(){
        super();
    }

    public String toString() {
        return "The provided filename does not correspond to any previously opened file";
    }

}
