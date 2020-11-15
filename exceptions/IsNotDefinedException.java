package exceptions;

public class IsNotDefinedException extends MyException{
    public IsNotDefinedException(){
        super();
    }

    public String toString(){
        return "Variable is not defined";
    }
}
