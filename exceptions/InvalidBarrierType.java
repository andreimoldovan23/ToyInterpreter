package exceptions;

public class InvalidBarrierType extends MyException {

    public InvalidBarrierType() {
        super();
    }

    public String toString() {
        return "The identifier of the barrier should be an integer";
    }

}
