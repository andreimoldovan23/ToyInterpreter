package ToyInterpreter.view;

import ToyInterpreter.controller.Controller;

public class AllStepsCommand extends Command {

    private final Controller controller;
    private final Command quit;

    public AllStepsCommand(String k, String d, Controller c) {
        super(k, d);
        controller = c;
        quit = new QuitCommand("quit", "Exit program");
    }

    public void execute() {
        try{
            controller.allStep();
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        quit.execute();
    }

}
