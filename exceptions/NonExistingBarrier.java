package exceptions;

public class NonExistingBarrier extends MyException {

    public NonExistingBarrier() {
        super();
    }

    public String toString() {
        return "This barrier does not exist";
    }

}
