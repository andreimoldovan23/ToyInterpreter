package view;

import controller.Controller;
import exceptions.MyException;
import model.stmts.Stmt;

public class DisplayCurrentCommand extends Command{

    private final Controller controller;

    public DisplayCurrentCommand(String k, String d, Controller c) {
        super(k, d);
        controller = c;
    }

    public void execute() {
        try{
            Stmt stmt = controller.getCurrentStatement();
            System.out.println("Current program\n" + stmt + "\n");
        }
        catch (MyException e){
            System.out.println(e);
        }
    }

}
