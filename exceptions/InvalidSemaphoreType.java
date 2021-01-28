package exceptions;

public class InvalidSemaphoreType extends MyException{

    public InvalidSemaphoreType() {
        super();
    }

    public String toString() {
        return "The identifier of the semaphore should be an integer";
    }

}
