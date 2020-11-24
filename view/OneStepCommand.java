package ToyInterpreter.view;

import ToyInterpreter.controller.Controller;
import ToyInterpreter.exceptions.MyException;

public class OneStepCommand extends Command{

    private final Controller controller;
    private final Command quit;

    public OneStepCommand(String k, String d, Controller c) {
        super(k, d);
        controller = c;
        quit = new QuitCommand("quit", "Exit program", controller);
    }

    public void execute() {
        try {
            controller.oneStep();
        }
        catch (MyException e){
            System.out.println(e);
            quit.execute();
        }
    }

}
