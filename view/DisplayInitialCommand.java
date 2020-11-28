package ToyInterpreter.view;

import ToyInterpreter.controller.Controller;
import ToyInterpreter.model.stmts.Stmt;

public class DisplayInitialCommand extends Command{

    private final Controller controller;

    public DisplayInitialCommand(String k, String d, Controller c) {
        super(k, d);
        controller = c;
    }

    public void execute() {
        Stmt stmt = controller.getInitialProgram();
        System.out.println("Main program\n" + stmt + "\n");
    }

}
