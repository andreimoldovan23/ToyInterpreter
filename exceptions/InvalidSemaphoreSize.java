package exceptions;

public class InvalidSemaphoreSize extends MyException {

    public InvalidSemaphoreSize() {
        super();
    }

    public String toString() {
        return "The semaphore size should be an integer";
    }

}
