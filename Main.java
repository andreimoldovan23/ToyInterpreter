package ToyInterpreter;

import ToyInterpreter.controller.Controller;
import ToyInterpreter.exceptions.MyException;
import ToyInterpreter.model.PrgState;
import ToyInterpreter.model.adts.ExeStack;
import ToyInterpreter.model.adts.FileTable;
import ToyInterpreter.model.adts.Out;
import ToyInterpreter.model.adts.SymTable;
import ToyInterpreter.model.exps.*;
import ToyInterpreter.model.stmts.*;
import ToyInterpreter.model.types.Int;
import ToyInterpreter.model.types.StringType;
import ToyInterpreter.model.values.IntValue;
import ToyInterpreter.model.values.StringValue;
import ToyInterpreter.repository.IRepo;
import ToyInterpreter.repository.Repo;
import ToyInterpreter.view.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static List<Stmt> generateProgram() throws MyException{
        Exp var = new VarExp("varf");
        Stmt s1 = new VarDecl(new StringType(), var);
        Stmt s2 = new AssignStmt(var, new ConstExp(new StringValue("test.in")));
        Stmt s3 = new OpenFileStmt(var);
        Stmt s4 = new VarDecl(new Int(), new VarExp("varc"));
        Stmt s5 = new ReadFileStmt(var, "varc");
        Stmt s6 = new PrintStmt(new VarExp("varc"));
        Stmt s7 = new CloseFileStmt(var);
        Exp const1 = new ConstExp(new IntValue(35));
        Stmt s8 = new IfStmt(new RelationalExp(const1, new VarExp("varc"), "<="),
                new PrintStmt(new ConstExp(new StringValue("varc greater than 35"))),
                new PrintStmt(new ConstExp(new StringValue("Hello world"))));
        return new ArrayList<>(List.of(s1, s2, s3, s4, s5, s6, s5, s6, s7, s8));
    }

    public static void main(String[] args) {
        try {
            List<Stmt> stmts = generateProgram();
            PrgState p1 = new PrgState(new ExeStack<>(), new SymTable<>(), new Out<>(), new FileTable<>(), stmts);

            IRepo<PrgState> repo = new Repo<>(p1, "");
            Controller c = new Controller(repo);

            TextMenu menu = new TextMenu();
            Command oneStep = new OneStepCommand("oneStep", "One step execution of current program", c);
            Command allStep = new AllStepsCommand("allStep", "Complete execution of current program", c);
            Command setDisplay = new SetDisplayFlagCommand("setFlag", "Set display flag for program", c);
            Command displayCurrent = new DisplayInitialCommand("displayInitial",
                    "Display main thread of current program", c);
            Command displayAll = new DisplayAllCommand("displayAll",
                    "Display all threads of current program", c);
            Command quit = new QuitCommand("quit", "Exit current session", c);
            Command setLogFile = new SetLogFile("setLog", "Sets the file used for logging program state", c);

            menu.addCommand(displayCurrent);
            menu.addCommand(displayAll);
            menu.addCommand(setLogFile);
            menu.addCommand(oneStep);
            menu.addCommand(allStep);
            menu.addCommand(setDisplay);
            menu.addCommand(quit);

            setLogFile.execute();
            menu.run();
        }
        catch (MyException e) {
            System.out.println(e);
        }
    }

}
