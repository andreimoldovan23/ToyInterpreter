package exceptions;

public class DivisionByZeroException extends MyException{

    public DivisionByZeroException(){
        super();
    }

    public String toString(){
        return "You cannot divide a number by 0!";
    }
}
