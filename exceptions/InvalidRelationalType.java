package exceptions;

public class InvalidRelationalType extends MyException{

    public InvalidRelationalType(){
        super();
    }

    public String toString(){
        return "You can only compare integers";
    }

}
