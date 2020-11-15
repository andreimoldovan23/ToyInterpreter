package ToyInterpreter.view;

import ToyInterpreter.controller.Controller;
import ToyInterpreter.exceptions.MyException;
import ToyInterpreter.model.stmts.Stmt;
import java.util.List;

public class DisplayAllCommand extends Command {

    private final Controller controller;

    public DisplayAllCommand(String k, String d, Controller c) {
        super(k, d);
        controller = c;
    }

    public void execute() {
        try{
            List<Stmt> statements = controller.getStatements();
            for(var s : statements) {
                System.out.println("Program\n" + s + "\n");
            }
        }
        catch (MyException e){
            System.out.println(e);
        }
    }

}
