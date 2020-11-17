package ToyInterpreter.view;

import ToyInterpreter.controller.Controller;

import java.io.IOException;

public class QuitCommand extends Command{

    private final Controller controller;
    public QuitCommand(String k, String d, Controller c) {
        super(k, d);
        controller = c;
    }

    public void execute() {
        scanner.close();
        try {
            controller.closeAll();
        }
        catch (IOException ioe){
            ioe.printStackTrace();
        }
        System.exit(0);
    }

}
