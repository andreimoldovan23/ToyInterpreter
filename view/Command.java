package ToyInterpreter.view;

import java.util.Scanner;

public abstract class Command {

    private final String key;
    private final String description;
    protected static final Scanner scanner = new Scanner(System.in);

    public Command(String k, String d){
        key = k;
        description = d;
    }

    public String getKey(){
        return key;
    }

    public String getDescription(){
        return description;
    }

    public static Scanner getScanner(){
        return scanner;
    }

    public abstract void execute();

}
