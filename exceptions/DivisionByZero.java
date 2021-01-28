package exceptions;

public class DivisionByZero extends MyException{

    public DivisionByZero(){
        super();
    }

    public String toString(){
        return "You cannot divide a number by 0!";
    }
}
