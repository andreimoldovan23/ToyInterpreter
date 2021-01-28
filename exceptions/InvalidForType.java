package exceptions;

public class InvalidForType extends MyException {

    public InvalidForType() {
        super();
    }

    public String toString() {
        return "The condition of the for statement should contain only numbers";
    }

}
