package ToyInterpreter.view;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TextMenu {

    private Map<String, Command> commands = new HashMap<>();

    public void addCommand(Command c){
        commands.put(c.getKey(), c);
    }

    public void printMenu(){
        for(var c : commands.values()){
            System.out.println(c.getKey() + ": " + c.getDescription());
        }
    }

    @SuppressWarnings("all")
    public void run(){
        Scanner scanner = Command.getScanner();
        while(true){
            System.out.println();
            printMenu();
            System.out.print("\n" + "Input command: ");
            String cmd = scanner.nextLine();
            Command c = commands.get(cmd);
            if(c == null){
                System.out.println("Invalid command!");
            }
            else {
                c.execute();
            }
        }
    }
}
