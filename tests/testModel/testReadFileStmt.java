package ToyInterpreter.tests.testModel;

import ToyInterpreter.exceptions.*;
import ToyInterpreter.model.PrgState;
import ToyInterpreter.model.adts.*;
import ToyInterpreter.model.exps.*;
import ToyInterpreter.model.stmts.*;
import ToyInterpreter.model.types.*;
import ToyInterpreter.model.values.*;
import org.junit.*;
import org.junit.rules.TemporaryFolder;
import java.io.File;
import java.io.IOException;

public class testReadFileStmt {

    @Rule
    public TemporaryFolder myFolder = new TemporaryFolder();

    private Exp varExpInt, varExpBool, varExpString;
    private Exp constExpInt;
    private Type intType, boolType;
    private PrgState state;

    @BeforeClass
    public static void printName(){
        System.out.println("Testing ReadFileStmt");
    }

    @AfterClass
    public static void printEnd(){
        System.out.println();
    }

    @Before
    public void setUp(){
        intType = new Int();
        boolType = new Bool();
        constExpInt = new ConstExp(new IntValue(10));
        varExpInt = new VarExp("number");
        varExpBool = new VarExp("boolean");
        varExpString = new ConstExp(new StringValue("myTestFile.txt"));
        state = new PrgState(new ExeStack<>(), new SymTable<>(), new Out<>(), new FileTable<>(), new Heap<>(),
                new NOP());
    }

    @SuppressWarnings("DuplicatedCode")
    @After
    public void tearDown() throws IOException {
        state.cleanAll();
        intType = null;
        boolType = null;
        constExpInt = null;
        varExpBool = null;
        varExpInt = null;
        varExpString = null;
        state = null;
    }

    @Test
    public void toStringTest() {
        Stmt readFileStmt = new ReadFileStmt(varExpString, "number");
        Assert.assertEquals("Testing toString method of ReadFileStmt", readFileStmt.toString(),
                "readFile (myTestFile.txt, number)\n");
    }

    @Test
    public void toStringPrefixTest() {
        Stmt readFileStmt = new ReadFileStmt(varExpString, "number");
        String expected = "\treadFile (myTestFile.txt, number)\n";
        Assert.assertEquals("Testing toStringPrefix method of ReadFileStmt",
                expected, readFileStmt.toStringPrefix("\t"));
    }

    @Test(expected = InvalidFilenameException.class)
    public void InvalidFilenameTest() throws MyException{
        Stmt readFileStmt = new ReadFileStmt(new ConstExp(new IntValue(7)), "hello");
        Assert.assertNotNull("Testing InvalidFilename exception of exec method of ReadFileStmt",
                readFileStmt.exec(state));
    }

    @Test(expected = FileNotOpen.class)
    public void FileNotOpenTest() throws MyException{
        Stmt varDecl = new VarDecl(intType, varExpInt);
        varDecl.exec(state);
        Stmt readFileStmt = new ReadFileStmt(new ConstExp(new StringValue("hello")), varExpInt.toString());
        Assert.assertNotNull("Testing FileNotOpen exception of exec method of ReadFileStmt",
                readFileStmt.exec(state));
    }

    @Test(expected = IsNotDefinedException.class)
    public void IsNotDefinedTest() throws MyException{
        Stmt readFileStmt = new ReadFileStmt(new ConstExp(new StringValue("hello")), varExpInt.toString());
        Assert.assertNotNull("Testing IsNotDefined exception of exec method of ReadFileStmt",
                readFileStmt.exec(state));
    }

    @Test(expected = InvalidFileReadType.class)
    public void InvalidFileReadTypeTest() throws MyException{
        Stmt varDecl = new VarDecl(boolType, varExpBool);
        varDecl.exec(state);
        Stmt readFileStmt = new ReadFileStmt(new ConstExp(new StringValue("hello")), varExpBool.toString());
        Assert.assertNotNull("Testing InvalidFileReadType exception of exec method of ReadFileStmt",
                readFileStmt.exec(state));
    }

    @Test
    public void execTest() throws MyException, IOException {
        Stmt varDecl = new VarDecl(intType, varExpInt);
        Stmt assign = new AssignStmt(varExpInt, constExpInt);
        varDecl.exec(state);
        assign.exec(state);

        File myFile = myFolder.newFile(varExpString.toString());
        StringValue path = new StringValue(myFile.getAbsolutePath());
        Stmt open = new OpenFileStmt(new ConstExp(path));
        Stmt read = new ReadFileStmt(new ConstExp(path), varExpInt.toString());

        open.exec(state);
        read.exec(state);

        Assert.assertEquals("Testing exec method of ReadFileStmt", 0,
                state.getTable().lookup(varExpInt.toString()).getValue());
    }

}
