package ToyInterpreter;

import ToyInterpreter.controller.Controller;
import ToyInterpreter.exceptions.MyException;
import ToyInterpreter.model.PrgState;
import ToyInterpreter.model.adts.*;
import ToyInterpreter.model.exps.*;
import ToyInterpreter.model.stmts.*;
import ToyInterpreter.model.types.Int;
import ToyInterpreter.model.types.Ref;
import ToyInterpreter.model.types.StringType;
import ToyInterpreter.model.values.IntValue;
import ToyInterpreter.model.values.StringValue;
import ToyInterpreter.repository.IRepo;
import ToyInterpreter.repository.Repo;
import ToyInterpreter.view.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {

    public static Stmt assemble(List<Stmt> statements){
        Collections.reverse(statements);
        return statements.stream().reduce(new NOP(), (a, b) -> new CompStmt(b, a));
    }

    private static Stmt generateProgram() throws MyException{
        Exp var = new VarExp("varf");
        Stmt s1 = new VarDecl(new StringType(), var);
        Stmt s2 = new AssignStmt(var, new ConstExp(new StringValue("test.in")));
        Stmt s3 = new OpenFileStmt(var);
        Stmt s4 = new VarDecl(new Int(), new VarExp("varc"));
        Stmt s5 = new ReadFileStmt(var, "varc");
        Stmt s6 = new PrintStmt(new VarExp("varc"));
        Stmt s7 = new CloseFileStmt(var);
        Exp const1 = new ConstExp(new IntValue(35));
        Exp arithmetic1 = new ArithmeticExp(const1, const1, "+");
        Exp arithmetic2 = new ArithmeticExp(const1, const1, "-");

        Stmt declareRef = new VarDecl(new Ref(new Int()), new VarExp("myRef"));
        Stmt declareRefCollected = new VarDecl(new Ref(new StringType()), new VarExp("myRefWillBeCollected"));
        Stmt declareCompRef = new VarDecl(new Ref(new Ref(new Int())), new VarExp("myCompRef"));

        Stmt newRefCollected = new NewStmt("myRefWillBeCollected", new ConstExp(new StringValue("Hello world")));
        Stmt newRefCollected2 = new NewStmt("myRefWillBeCollected", new ConstExp(new StringValue("Hi guys")));
        Stmt newSimple = new NewStmt("myRef", new ConstExp(new IntValue(10)));
        Stmt newComp = new NewStmt("myCompRef", new VarExp("myRef"));

        Stmt write = new WriteHeapStmt("myRef", arithmetic1);
        Stmt print1 = new PrintStmt(new ReadHeapExp(new VarExp("myRef")));
        Stmt print2 = new PrintStmt(new ReadHeapExp(new VarExp("myCompRef")));
        Stmt new2 = new NewStmt("myRef", new ConstExp(new IntValue(100)));

        Stmt s8 = new IfStmt(new RelationalExp(arithmetic1, arithmetic2, ">="),
                assemble(new ArrayList<>(List.of(declareRef, declareCompRef, declareRefCollected,
                        newSimple, newComp, newRefCollected, write, new2, newRefCollected2))),
                new NOP());

        Exp a = new VarExp("a");
        Exp relational = new RelationalExp(a, new ConstExp(new IntValue(0)), ">");
        Stmt declA = new VarDecl(new Int(), a);
        Stmt assignA = new AssignStmt(a, new ConstExp(new IntValue(3)));
        Stmt printA = new PrintStmt(a);
        Stmt decrementA = new AssignStmt(a, new ArithmeticExp(a, new ConstExp(new IntValue(1)), "-"));
        Stmt whileStmt = new WhileStmt(relational, new CompStmt(printA, decrementA));
        return assemble(new ArrayList<>(List.of(s1, s2, s3, s4, s5, s6, s5, s6, s7, s8,
                assemble(new ArrayList<>(List.of(print1, print2))),
                assemble(new ArrayList<>(List.of(declA, assignA, whileStmt))))));
    }

    public static void main(String[] args) {
        try {
            Stmt stmt = generateProgram();
            PrgState p1 = new PrgState(new ExeStack<>(), new SymTable<>(), new Out<>(), new FileTable<>(),
                    new Heap<>(), stmt);

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
            Command setLogFile = new SetLogFile("setLog", "Sets the file used for logging program state", c);

            menu.addCommand(displayCurrent);
            menu.addCommand(displayAll);
            menu.addCommand(setLogFile);
            menu.addCommand(oneStep);
            menu.addCommand(allStep);
            menu.addCommand(setDisplay);

            setLogFile.execute();
            menu.run();
        }
        catch (MyException e) {
            System.out.println(e);
        }
    }

}
