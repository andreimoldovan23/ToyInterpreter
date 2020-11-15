package ToyInterpreter.view;

public class QuitCommand extends Command{

    public QuitCommand(String k, String d) {
        super(k, d);
    }

    public void execute() {
        System.exit(0);
    }

}
