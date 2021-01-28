package exceptions;

public class InvalidAssignType extends MyException{
    public InvalidAssignType(){
        super();
    }

    public String toString(){
        return "Mismatch between types in assignment statement";
    }
}
