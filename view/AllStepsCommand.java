package ToyInterpreter.view;

import ToyInterpreter.controller.Controller;
import ToyInterpreter.exceptions.MyException;
import java.io.IOException;

public class AllStepsCommand extends Command {

    private final Controller controller;

    public AllStepsCommand(String k, String d, Controller c) {
        super(k, d);
        controller = c;
    }

    public void execute() {
        try{
            controller.allStep();
        }
        catch (MyException e){
            System.out.println(e);
        }
        catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

}
