package exceptions;

public class InvalidOperator extends MyException{
    public String toString(){
        return "Invalid operator";
    }

    public InvalidOperator(){
        super();
    }
}
