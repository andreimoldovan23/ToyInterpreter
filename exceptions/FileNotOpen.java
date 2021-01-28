package exceptions;

public class FileNotOpen extends MyException {

    public FileNotOpen(){
        super();
    }

    public String toString(){
        return "No file with the given name is currently opened by the program";
    }

}
