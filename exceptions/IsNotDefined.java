package exceptions;

public class IsNotDefined extends MyException{
    public IsNotDefined(){
        super();
    }

    public String toString(){
        return "Variable is not defined";
    }
}
