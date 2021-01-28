package exceptions;

public class InvalidLock extends MyException {

    public InvalidLock() {
        super();
    }

    public String toString() {
        return "The id of the lock must be an integer";
    }

}
