package exceptions;

public class InvalidFileReadType extends MyException {

    public InvalidFileReadType(){
        super();
    }

    public String toString(){
        return "Values from the file can only be read into variables of type int";
    }

}
