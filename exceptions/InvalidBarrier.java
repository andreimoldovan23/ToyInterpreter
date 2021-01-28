package exceptions;

public class InvalidBarrier extends MyException {

    public InvalidBarrier() {
        super();
    }

    public String toString() {
        return "The size of the barrier should be an integer";
    }

}
