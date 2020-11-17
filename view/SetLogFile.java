package ToyInterpreter.view;

import ToyInterpreter.controller.Controller;

import java.io.IOException;

public class SetLogFile extends Command{
    private final Controller controller;

    public SetLogFile(String k, String d, Controller c) {
        super(k, d);
        controller = c;
    }

    private String readPath() {
        System.out.print("Input path: ");
        return scanner.nextLine();
    }

    public void execute() {
        String path = readPath();
        try {
            controller.setLogFile(path);
        }
        catch (IOException e){
            System.out.println("Error opening log file");
        }
    }
}
