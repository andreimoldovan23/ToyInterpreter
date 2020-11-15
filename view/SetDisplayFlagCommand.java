package ToyInterpreter.view;

import ToyInterpreter.controller.Controller;

import java.util.InputMismatchException;

public class SetDisplayFlagCommand extends Command {

    private final Controller controller;

    public SetDisplayFlagCommand(String k, String d, Controller c) {
        super(k, d);
        controller = c;
    }

    private boolean readFlag(){
        while(true){
            try {
                System.out.print("Input flag: ");
                boolean flag = scanner.nextBoolean();
                scanner.nextLine();
                return flag;
                }
            catch (InputMismatchException e){
                System.out.println("You should only input a boolean value(true or false)");
                scanner.next();
            }
        }
    }

    public void execute() {
        boolean flag = readFlag();
        controller.setDisplayFlag(flag);
    }

}
