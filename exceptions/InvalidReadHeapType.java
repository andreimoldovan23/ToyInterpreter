package ToyInterpreter.exceptions;

public class InvalidReadHeapType extends MyException {

    public InvalidReadHeapType(){
        super();
    }

    public String toString(){
        return "You can not read a non reference value from the heap";
    }

}
