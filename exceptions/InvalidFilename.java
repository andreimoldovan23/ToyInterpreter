package exceptions;

public class InvalidFilename extends MyException{

    public InvalidFilename(){
        super();
    }

    public String toString() {
        return "The provided filename does not correspond to any previously opened file";
    }

}
