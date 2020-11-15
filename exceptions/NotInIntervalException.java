package exceptions;

public class NotInIntervalException extends MyException{
    public NotInIntervalException(){
        super();
    }

    public String toString(){
        return "You should input a valid program index";
    }
}
