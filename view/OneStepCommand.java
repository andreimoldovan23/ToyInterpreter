package ToyInterpreter.view;

import ToyInterpreter.controller.Controller;
import ToyInterpreter.exceptions.MyException;
import java.io.IOException;

public class OneStepCommand extends Command{

    private final Controller controller;

    public OneStepCommand(String k, String d, Controller c) {
        super(k, d);
        controller = c;
    }

    public void execute() {
        try {
            controller.oneStep();
        }
        catch (MyException e){
            System.out.println(e);
        }
        catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

}
