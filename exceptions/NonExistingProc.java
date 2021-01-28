package exceptions;

public class NonExistingProc extends MyException {

    public NonExistingProc() {
        super();
    }

    public String toString() {
        return "This name does not correspond to any procedure";
    }

}
