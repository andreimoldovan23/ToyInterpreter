package exceptions;

public class InvalidLogicType extends MyException {
    public InvalidLogicType() {
        super();
    }
    public String toString(){
        return "The values provided should be booleans!";
    }


}
