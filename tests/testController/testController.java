package ToyInterpreter.tests.testController;

import ToyInterpreter.Main;
import ToyInterpreter.controller.Controller;
import ToyInterpreter.exceptions.InvalidVariable;
import ToyInterpreter.model.PrgState;
import ToyInterpreter.model.adts.*;
import ToyInterpreter.model.exps.ConstExp;
import ToyInterpreter.model.exps.Exp;
import ToyInterpreter.model.exps.ReadHeapExp;
import ToyInterpreter.model.exps.VarExp;
import ToyInterpreter.model.stmts.*;
import ToyInterpreter.model.types.Int;
import ToyInterpreter.model.types.Ref;
import ToyInterpreter.model.types.StringType;
import ToyInterpreter.model.values.IntValue;
import ToyInterpreter.model.values.StringValue;
import ToyInterpreter.model.values.Value;
import ToyInterpreter.repository.IRepo;
import ToyInterpreter.repository.Repo;
import org.junit.*;
import org.junit.rules.TemporaryFolder;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class testController {

    private IOut<String> out;
    private IFileTable<StringValue, MyBufferedReader> fileTable;
    private IHeap<Integer, Value> heap;
    private IRepo<PrgState> repo;
    private PrgState state;
    private Controller controller;

    @Rule
    public TemporaryFolder myFolder = new TemporaryFolder();

    @BeforeClass
    public static void printName() {
        System.out.println("Testing controller");
    }

    @AfterClass
    public static void printEnd() {
        System.out.println();
    }

    @Before
    public void setUp() throws InvalidVariable, IOException {
        out = new Out<>();
        fileTable = new FileTable<>();
        heap = new Heap<>();

        File myFile = myFolder.newFile("testController.in");
        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(myFile, true)), true);
        writer.println(50);
        writer.close();

        String path = myFile.getAbsolutePath();
        File logFile = myFolder.newFile("logFile.txt");
        String logPath = logFile.getAbsolutePath();

        Exp var = new VarExp("a");
        Exp refVar = new VarExp("refVar");
        Exp refRef = new VarExp("refRefVar");
        //correspondence:
        //s19, s5, __
        //s20, s6, __
        //s21, s7, __
        //s22, s8, __
        //s23, s9, __
        //s24, s10, __
        //s25, s14, __
        //s26, s15, s11
        //s27, s16, s12
        //__, s17, s13

        //main thread
        Stmt s1 = new VarDecl(new Int(), var);
        Stmt s2 = new AssignStmt(var, new ConstExp(new IntValue(8)));
        Stmt s3 = new PrintStmt(var);
        Stmt s4 = new VarDecl(new Ref(new Int()), refVar);

        //second thread
        Stmt s5 = new VarDecl(new Ref(new Ref(new Int())), refRef);
        Stmt s7 = new NewStmt("refRefVar", refVar);
        Stmt s6 = new NewStmt("refVar", new ConstExp(new IntValue(10)));
        Stmt s8 = new VarDecl(new StringType(), new VarExp("inFile"));
        Stmt s9 = new AssignStmt(new VarExp("inFile"), new ConstExp(new StringValue(path)));
        Stmt s10 = new OpenFileStmt(new VarExp("inFile"));

        //third thread
        Stmt s11 = new VarDecl(new Int(), new VarExp("fileNumber"));
        Stmt s12 = new ReadFileStmt(new VarExp("inFile"), "fileNumber");
        Stmt s13 = new PrintStmt(new VarExp("fileNumber"));

        //second thread
        Stmt s14 = new ForkStmt(Main.assemble(new ArrayList<>(List.of(s11, s12, s13))));
        Stmt s15 = new PrintStmt(new ReadHeapExp(refVar));
        Stmt s16 = new CloseFileStmt(new VarExp("inFile"));
        Stmt s17 = new NewStmt("refVar", new ConstExp(new IntValue(15)));

        //main thread
        Stmt s18 = new ForkStmt(Main.assemble(new ArrayList<>(List.of(s5, s6, s7, s8, s9, s10, s14, s15, s16, s17))));
        Stmt s19 = new NewStmt("refVar", new ConstExp(new IntValue(100)));
        Stmt s20 = new NOP();
        Stmt s21 = new NOP();
        Stmt s22 = new NOP();
        Stmt s23 = new NOP();
        Stmt s24 = new ReadFileStmt(new ConstExp(new StringValue(path)), "a");
        Stmt s25 = new NOP();
        Stmt s26 = new PrintStmt(new ReadHeapExp(refVar));
        Stmt s27 = new OpenFileStmt(new ConstExp(new StringValue(path)));

        state = new PrgState(new ExeStack<>(), new SymTable<>(), out, fileTable, heap,
                Main.assemble(
                        new ArrayList<>(List.of(s1, s2, s3, s4, s18, s19, s20, s21, s22, s23, s24, s25, s26, s27))));

        repo = new Repo<>(state, logPath);
        controller = new Controller(repo);
        controller.setDisplayFlag(false);
    }

    @After
    public void tearDown() throws IOException {
        state.getTable().clear();
        Main.clean(out, fileTable, heap);
        state = null;
        repo = null;
        controller = null;
    }

    @Test
    public void allStepTest() throws InterruptedException {
        //Possible out final values
        //8, 10, 100, 50
        //8, 10, 100, 0
        //8, 100, 10, 50
        //8, 100, 10, 0

        //Possible heap final values
        //2-->10, 3-->(2 -> int), 4-->15
        //1-->10, 3-->(1 -> int), 4-->15

        String expectedOut = "8\n" + "100\n" + "10\n" + "0\n";
        String expectedHeap = "2-->10\n" + "3-->(2 -> int)\n" + "4-->15\n";
        controller.allStep();

        Assert.assertEquals(expectedOut, out.toString());
        Assert.assertEquals("", fileTable.toString());
        Assert.assertEquals(expectedHeap, heap.toString());
    }

}
