package ToyInterpreter.view;

import ToyInterpreter.Main;

import java.io.IOException;

public class QuitCommand extends Command{

    public QuitCommand(String k, String d) {
        super(k, d);
    }

    public void execute() {
        scanner.close();
        try {
            Main.clean();
        }
        catch (IOException ioe){
            System.out.println("Something went wrong");
            ioe.printStackTrace();
        }
        System.out.println("Exiting...");
        System.exit(0);
    }

}
