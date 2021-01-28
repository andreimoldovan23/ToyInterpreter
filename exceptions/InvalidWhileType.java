package exceptions;

public class InvalidWhileType extends MyException {

    public InvalidWhileType(){
        super();
    }

    public String toString(){
        return "The while condition is not a boolean";
    }

}
