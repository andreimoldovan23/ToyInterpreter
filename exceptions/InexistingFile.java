package exceptions;

public class InexistingFile extends MyException {

    public InexistingFile(){
        super();
    }

    public String toString(){
        return "The file does not exist";
    }

}
