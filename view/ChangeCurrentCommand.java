package view;

import controller.Controller;
import exceptions.MyException;

public class ChangeCurrentCommand extends Command{

    private final Controller controller;

    public ChangeCurrentCommand(String k, String d, Controller c) {
        super(k, d);
        controller = c;
    }

    private int readInt(){
        while (true){
            try{
                System.out.print("Input number: ");
                return Integer.parseInt(scanner.nextLine());
            }
            catch (NumberFormatException e){
                System.out.println("You should only input numbers");
            }
        }
    }

    public void execute() {
        int number = readInt();
        try{
            controller.changeCurrentProgram(number - 1);
        }
        catch (MyException e) {
            System.out.println(e);
        }
    }
}
