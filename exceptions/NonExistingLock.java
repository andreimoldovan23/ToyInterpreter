package exceptions;

public class NonExistingLock extends MyException {

    public NonExistingLock() {
        super();
    }

    public String toString() {
        return "This lock does not exist";
    }

}
