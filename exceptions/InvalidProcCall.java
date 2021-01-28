package exceptions;

public class InvalidProcCall extends MyException {

    public InvalidProcCall() {
        super();
    }

    public String toString() {
        return "The provided arguments do not match the signature";
    }

}
