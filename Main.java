package ToyInterpreter;

import ToyInterpreter.controller.Controller;
import ToyInterpreter.exceptions.InvalidVariable;
import ToyInterpreter.exceptions.MyException;
import ToyInterpreter.model.PrgState;
import ToyInterpreter.model.adts.*;
import ToyInterpreter.model.exps.*;
import ToyInterpreter.model.stmts.*;
import ToyInterpreter.model.types.Int;
import ToyInterpreter.model.types.Ref;
import ToyInterpreter.model.types.StringType;
import ToyInterpreter.model.types.Type;
import ToyInterpreter.model.values.IntValue;
import ToyInterpreter.model.values.StringValue;
import ToyInterpreter.model.values.Value;
import ToyInterpreter.repository.IRepo;
import ToyInterpreter.repository.Repo;
import ToyInterpreter.view.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {

    private static IOut<String> out = new Out<>();
    private static IFileTable<StringValue, MyBufferedReader> fileTable = new FileTable<>();
    private static IHeap<Integer, Value> heap = new Heap<>();
    private static ITypeEnv<String, Type> typeEnv = new TypeEnv<>();

    private static boolean typeChecker(Stmt s) {
        try{
            typeEnv = s.typeCheck(typeEnv);
            return true;
        }
        catch (MyException e){
            System.out.println(e);
            return false;
        }
    }

    public static Stmt assemble(List<Stmt> statements){
        Collections.reverse(statements);
        return statements.stream().reduce(new NOP(), (a, b) -> new CompStmt(b, a));
    }

    public static void clean(IOut<String> out, IFileTable<StringValue, MyBufferedReader> fileTable,
                             IHeap<Integer, Value> heap) throws IOException {
        out.clear();
        heap.clear();
        List<MyBufferedReader> readers = fileTable.getValues();
        for(var r : readers)
            r.close();
        fileTable.clear();
        typeEnv.clear();
    }

    public static void clean() throws IOException {
        clean(out, fileTable, heap);
    }

    private static Stmt generateProgram() throws MyException{
        Exp number = new VarExp("number");
        Exp refNumber = new VarExp("refNumber");
        Exp refRef = new VarExp("refRef");
        Exp testFile = new ConstExp(new StringValue("testFile.txt"));
        Exp myFile = new ConstExp(new StringValue("myFile.txt"));
        Stmt nop = new NOP();

        //main thread
        Stmt s1 = new VarDecl(new Int(), number);
        Stmt s2 = new VarDecl(new Ref(new Int()), refNumber);
        Stmt s3 = new VarDecl(new Ref(new Ref(new Int())), refRef);

        Stmt s4 = new AssignStmt(number, new ConstExp(new IntValue(15)));
        Stmt s5 = new NewStmt(refNumber.toString(), new ConstExp(new IntValue(15)));
        Stmt s6 = new OpenFileStmt(testFile);

        //second thread
        Stmt s7 = new NewStmt(refRef.toString(), refNumber);
        Stmt s8 = new NewStmt(refNumber.toString(), new ConstExp(new IntValue(20)));
        Stmt s9 = new OpenFileStmt(myFile);
        Stmt s10 = new ReadFileStmt(myFile, number.toString());

        //third thread
        Stmt s11 = new WriteHeapStmt(refNumber.toString(), new ConstExp(new IntValue(100)));
        Stmt s12 = new OpenFileStmt(myFile);
        Stmt s13 = new ReadFileStmt(testFile, number.toString());

        //fourth thread
        Stmt read = new ReadFileStmt(testFile, number.toString());
        Stmt s14 = new PrintStmt(number);
        Stmt s15 = new CloseFileStmt(testFile);

        //third thread
        Stmt s16 = new ForkStmt(assemble(new ArrayList<>(List.of(read, s14, s15))));
        Stmt thirdPrint = new PrintStmt(number);

        //second thread
        Stmt s17 = new ForkStmt(assemble(new ArrayList<>(List.of(s11, s12, s13, s16, thirdPrint, nop, nop))));
        Stmt s18 = new WriteHeapStmt(refNumber.toString(), new ConstExp(new IntValue(200)));
        Stmt s19 = new CloseFileStmt(myFile);
        Stmt s20 = new PrintStmt(number);
        Stmt secondRead = new ReadFileStmt(testFile, number.toString());
        Stmt secondPrint = new PrintStmt(number);

        //main thread
        Stmt s21 = new ForkStmt(assemble(new ArrayList<>(List.of(s7, s8, s9, s10, s17, s18, s19, s20, secondRead,
                secondPrint, nop, nop))));
        Stmt s22 = new NewStmt(refNumber.toString(), new ConstExp(new IntValue(100)));
        Stmt s23 = new NewStmt(refRef.toString(), refNumber);
        Stmt s24 = new OpenFileStmt(myFile);
        Stmt s25 = new ReadFileStmt(myFile, number.toString());
        Stmt s26 = new PrintStmt(number);

        return assemble(new ArrayList<>(List.of(s1, s2, s3, s4, s5, s6, s21, s22, s23, s24, s25, s26,
                nop, nop, nop, nop, nop, nop, nop)));
    }

    private static Stmt generateWrongProgram() throws InvalidVariable {
        Exp var = new VarExp("number");
        Exp ref = new VarExp("refNumber");
        Stmt s1 = new VarDecl(new StringType(), var);
        Stmt s2 = new VarDecl(new Ref(new Int()), ref);
        Stmt s3 = new NewStmt(ref.toString(), new ConstExp(new IntValue(100)));
        Stmt s4 = new AssignStmt(var, new ReadHeapExp(ref));
        Stmt s5 = new PrintStmt(var);
        return assemble(new ArrayList<>(List.of(s1, s2, s3, s4, s5)));
    }

    public static void main(String[] args) throws MyException {
        Stmt stmt = generateProgram();
        //Stmt stmt = generateWrongProgram();
        if(stmt == null || !typeChecker(stmt)){
            System.out.println("Compilation errors");
            Command quit = new QuitCommand("quit", "Exit program");
            quit.execute();
        }

        PrgState program = new PrgState(new ExeStack<>(), new SymTable<>(), out,
                fileTable, heap, stmt);
        IRepo<PrgState> repo = new Repo<>(program, "");
        Controller c = new Controller(repo);

        TextMenu menu = new TextMenu();
        Command allStep = new AllStepsCommand("allStep", "Complete execution of current program", c);
        Command setDisplay = new SetDisplayFlagCommand("setFlag", "Set display flag for program", c);
        Command displayCurrent = new DisplayInitialCommand("displayInitial",
                "Display main thread of current program", c);
        Command setLogFile = new SetLogFile("setLog", "Sets the file used for logging program state", c);

        menu.addCommand(displayCurrent);
        menu.addCommand(setLogFile);
        menu.addCommand(allStep);
        menu.addCommand(setDisplay);

        setLogFile.execute();
        menu.run();
    }

}
