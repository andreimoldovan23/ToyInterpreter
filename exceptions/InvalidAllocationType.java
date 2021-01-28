package exceptions;

public class InvalidAllocationType extends MyException{

    public InvalidAllocationType(){
        super();
    }

    public String toString(){
        return "Incompatibility between referenced and declared types";
    }

}
