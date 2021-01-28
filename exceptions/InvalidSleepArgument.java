package exceptions;

public class InvalidSleepArgument extends MyException {

    public InvalidSleepArgument() {
        super();
    }

    public String toString() {
        return "The argument provided to sleep should be an integer";
    }

}
