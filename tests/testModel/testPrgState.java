package ToyInterpreter.tests.testModel;

import ToyInterpreter.Main;
import ToyInterpreter.exceptions.MyException;
import ToyInterpreter.model.PrgState;
import ToyInterpreter.model.adts.*;
import ToyInterpreter.model.exps.ConstExp;
import ToyInterpreter.model.exps.Exp;
import ToyInterpreter.model.exps.VarExp;
import ToyInterpreter.model.stmts.*;
import ToyInterpreter.model.types.Int;
import ToyInterpreter.model.types.Ref;
import ToyInterpreter.model.values.IntValue;
import ToyInterpreter.model.values.StringValue;
import ToyInterpreter.model.values.Value;
import org.junit.*;
import org.junit.rules.TemporaryFolder;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class testPrgState {

    @Rule
    public TemporaryFolder myFolder = new TemporaryFolder();

    private PrgState state;
    private Exp var;

    @BeforeClass
    public static void printName(){
        System.out.println("Testing PrgState class");
    }

    @AfterClass
    public static void printEnd(){
        System.out.println();
    }

    @Before
    public void setUp() throws MyException, IOException {
        File myFile = myFolder.newFile("myFile.txt");
        String path = myFile.getAbsolutePath();

        var = new VarExp("a");
        Stmt s1 = new VarDecl(new Int(), var);
        Stmt s2 = new VarDecl(new Ref(new Int()), new VarExp("refVar"));
        Stmt s3 = new AssignStmt(var, new ConstExp(new IntValue(10)));
        Stmt s4 = new PrintStmt(var);
        Stmt s5 = new OpenFileStmt(new ConstExp(new StringValue(path)));
        Stmt s6 = new NewStmt("refVar", new ConstExp(new IntValue(10)));

        state = new PrgState(new ExeStack<>(), new SymTable<>(), new Out<>(), new FileTable<>(),
                new Heap<>(), Main.assemble(new ArrayList<>(List.of(s1, s2, s3, s4, s5, s6))));
        IExeStack<Stmt> stack = state.getStack();
        while(!stack.empty()) {
            Stmt s = stack.pop();
            s.exec(state);
            stack = state.getStack();
        }
    }

    @After
    public void tearDown() throws IOException {
        state.getTable().clear();
        Main.clean(state.getOut(), state.getFileTable(), state.getHeap());
        var = null;
        state = null;
    }

    @Test
    public void assembleTest() {
        ISymTable<String, Value> table = state.getTable();
        IOut<String> out = state.getOut();
        Assert.assertEquals("Testing assemble method of PrgState", 10,
                table.lookup(var.toString()).getValue());
        Assert.assertEquals("10\n", out.toString());
    }

    @Test
    public void cleanAllTest() throws IOException {
        state.getTable().clear();
        Main.clean(state.getOut(), state.getFileTable(), state.getHeap());
        ISymTable<String, Value> table = state.getTable();
        IOut<String> out = state.getOut();
        IFileTable<StringValue, MyBufferedReader> fileTable = state.getFileTable();
        IHeap<Integer, Value> heap = state.getHeap();
        Assert.assertEquals("Testing cleanAll method of PrgState", "", table.toString());
        Assert.assertEquals("", out.toString());
        Assert.assertEquals("", fileTable.toString());
        Assert.assertEquals("", heap.toString());
    }

}
