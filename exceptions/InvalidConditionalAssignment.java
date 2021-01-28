package exceptions;

public class InvalidConditionalAssignment extends MyException {

    public InvalidConditionalAssignment() {
        super();
    }

    public String toString() {
        return "The assigment cannot take place because of type incompatibility";
    }

}
