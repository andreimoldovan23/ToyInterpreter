package ToyInterpreter.exceptions;

public class FileAlreadyOpen extends MyException{

    public FileAlreadyOpen(){
        super();
    }

    public String toString(){
        return "The file is open in the current program";
    }

}
