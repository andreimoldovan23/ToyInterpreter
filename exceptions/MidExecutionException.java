package exceptions;

public class MidExecutionException extends MyException{

    public MidExecutionException(){
        super();
    }

    public String toString(){
        return "Cannot reset program while it's still executing";
    }
}
