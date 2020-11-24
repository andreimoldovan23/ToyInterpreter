package ToyInterpreter.tests.testController;

import ToyInterpreter.Main;
import ToyInterpreter.controller.Controller;
import ToyInterpreter.exceptions.MyException;
import ToyInterpreter.exceptions.StackEmptyException;
import ToyInterpreter.model.PrgState;
import ToyInterpreter.model.adts.*;
import ToyInterpreter.model.exps.ConstExp;
import ToyInterpreter.model.exps.Exp;
import ToyInterpreter.model.exps.VarExp;
import ToyInterpreter.model.stmts.*;
import ToyInterpreter.model.types.*;
import ToyInterpreter.model.values.IntValue;
import org.junit.*;
import ToyInterpreter.repository.*;
import org.junit.rules.TemporaryFolder;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class testController {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private Controller controller;
    private IRepo<PrgState> repo;
    private PrgState prg;
    private String path;

    @BeforeClass
    public static void printName(){
        System.out.println("Testing Controller class");
    }

    @AfterClass
    public static void printEnd(){
        System.out.println();
    }

    @Before
    public void setUp() throws MyException, IOException {
        File myTestFile = tempFolder.newFile("testFile.in");
        path = myTestFile.getAbsolutePath();
        Exp var = new VarExp("a");
        Exp refVar = new VarExp("refVar");
        Stmt s1 = new VarDecl(new Int(), var);
        Stmt s2 = new AssignStmt(var, new ConstExp(new IntValue(8)));
        Stmt s3 = new PrintStmt(var);
        Stmt s4 = new VarDecl(new Ref(new Int()), refVar);
        Stmt s5 = new NewStmt("refVar", new ConstExp(new IntValue(10)));
        Stmt s6 = new NewStmt("refVar", new ConstExp(new IntValue(100)));
        Stmt s7 = new PrintStmt(refVar);
        prg = new PrgState(new ExeStack<>(), new SymTable<>(), new Out<>(),
                new FileTable<>(), new Heap<>(), Main.assemble(new ArrayList<>(
                        List.of(s1, s2, s3, s4, s5, s6, s7))));
        repo = new Repo<>(prg, path);
    }

    @After
    public void tearDown() throws IOException {
        controller.closeAll();
        prg = null;
        repo = null;
        controller = null;
    }

    @Test
    public void getStatementsTest() {
        controller = new Controller(repo);
        List<Stmt> controllerStatements = controller.getStatements();
        for(var s : controllerStatements)
            Assert.assertEquals("Testing getStatements method of Controller",
                    s.toString(), prg.getInitialProgram().toString());
    }

    @Test
    public void getCurrentStatementTest() {
        controller = new Controller(repo);
        Stmt s = controller.getInitialProgram();
        Assert.assertEquals("Testing getCurrentStatement method of Controller",
                prg.getInitialProgram().toString(), s.toString());
    }

    @Test
    public void allStepTest() throws MyException, IOException {
        controller = new Controller(repo);
        controller.setDisplayFlag(false);
        try {
            controller.allStep();
        }
        catch (StackEmptyException se){
            Stream<String> stream = Files.lines(Path.of(path), StandardCharsets.UTF_8);
            long count = stream.count();
            Assert.assertEquals("Testing allStep method of Controller", 319, count);

            Assert.assertFalse("Testing garbage collector", prg.getHeap().isDefined(1));
        }
    }

}
