package exceptions;

public class InvalidLatchType extends MyException {

    public InvalidLatchType() {
        super();
    }

    public String toString() {
        return "The size of the latch should be an integer";
    }

}
