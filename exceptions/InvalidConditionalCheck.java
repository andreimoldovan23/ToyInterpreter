package exceptions;

public class InvalidConditionalCheck extends MyException {

    public InvalidConditionalCheck() {
        super();
    }

    public String toString() {
        return "The condition from the conditional assignment must evaluate to a boolean";
    }

}
