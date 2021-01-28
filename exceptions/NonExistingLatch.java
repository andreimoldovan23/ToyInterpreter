package exceptions;

public class NonExistingLatch extends MyException {

    public NonExistingLatch() {
        super();
    }

    public String toString() {
        return "This CountDownLatch does not exist";
    }

}
