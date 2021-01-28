package exceptions;

public class ThreadException extends MyException {

    private final MyException exception;
    private final int id;

    public ThreadException(MyException e, int Id){
        super();
        exception = e;
        id = Id;
    }

    public String toString() {
        return "Program: " + id + ", " + exception.toString();
    }

}
