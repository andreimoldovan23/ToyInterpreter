package ToyInterpreter.view;

import ToyInterpreter.controller.Controller;
import ToyInterpreter.exceptions.MyException;

public class AllStepsCommand extends Command {

    private final Controller controller;
    private final Command quit;

    public AllStepsCommand(String k, String d, Controller c) {
        super(k, d);
        controller = c;
        quit = new QuitCommand("quit", "Exit program", controller);
    }

    public void execute() {
        try{
            controller.allStep();
        }
        catch (MyException e){
            quit.execute();
        }
    }

}
