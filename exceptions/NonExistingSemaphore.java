package exceptions;

public class NonExistingSemaphore extends MyException {

    public NonExistingSemaphore() {
        super();
    }

    public String toString() {
        return "This semaphore does not exist";
    }

}
