package ToyInterpreter.tests.testModel;

import ToyInterpreter.Main;
import ToyInterpreter.exceptions.*;
import ToyInterpreter.model.PrgState;
import ToyInterpreter.model.adts.*;
import ToyInterpreter.model.exps.*;
import ToyInterpreter.model.stmts.*;
import ToyInterpreter.model.values.*;
import org.junit.*;
import org.junit.rules.TemporaryFolder;
import java.io.File;
import java.io.IOException;

public class testOpenFileStmt {

    @Rule
    public TemporaryFolder myFolder = new TemporaryFolder();

    private Exp varExpString;
    private PrgState state;

    @BeforeClass
    public static void printName(){
        System.out.println("Testing OpenFileStmt");
    }

    @AfterClass
    public static void printEnd(){
        System.out.println();
    }

    @Before
    public void setUp(){
        varExpString = new ConstExp(new StringValue("myTestFile.txt"));
        state = new PrgState(new ExeStack<>(), new SymTable<>(), new Out<>(), new FileTable<>(), new Heap<>(),
                new NOP());
    }

    @SuppressWarnings("DuplicatedCode")
    @After
    public void tearDown() throws IOException {
        state.getTable().clear();
        Main.clean(state.getOut(), state.getFileTable(), state.getHeap());
        varExpString = null;
        state = null;
    }

    @Test
    public void toStringTest() {
        Stmt openStmt = new OpenFileStmt(varExpString);
        Assert.assertEquals("Testing toString method of OpenFileStmt", openStmt.toString(),
                "openFile (myTestFile.txt)\n");
    }

    @Test
    public void toStringPrefixTest() {
        Stmt openStmt = new OpenFileStmt(varExpString);
        String expected = "\topenFile (myTestFile.txt)\n";
        Assert.assertEquals("Testing toStringPrefix method of OpenFileStmt",
                expected, openStmt.toStringPrefix("\t"));
    }

    @Test
    public void execTest() throws MyException, IOException {
        File myFile = myFolder.newFile(varExpString.toString());
        StringValue stringValue = new StringValue(myFile.getAbsolutePath());
        Stmt openStmt = new OpenFileStmt(new ConstExp(stringValue));
        openStmt.exec(state);
        IFileTable<StringValue, MyBufferedReader> fileTable = state.getFileTable();
        Assert.assertTrue("Testing exec method of OpenFileStmt", fileTable.isDefined(stringValue));
    }

    @Test(expected = InvalidFilenameException.class)
    public void InvalidFilenameTest() throws MyException {
        Stmt openStmt = new OpenFileStmt(new ConstExp(new IntValue(7)));
        try{
            openStmt.exec(state);
        }
        catch (ThreadException te){
            throw te.getException();
        }
    }

    @Test(expected = FileAlreadyOpen.class)
    public void FileAlreadyOpenTest() throws MyException, IOException {
        File myFile = myFolder.newFile(varExpString.toString());
        StringValue stringValue = new StringValue(myFile.getAbsolutePath());
        Stmt openStmt1 = new OpenFileStmt(new ConstExp(stringValue));
        Stmt openStmt2 = new OpenFileStmt(new ConstExp(stringValue));
        openStmt1.exec(state);
        try{
            openStmt2.exec(state);
        }
        catch (ThreadException te){
            throw te.getException();
        }
    }

    @Test(expected = InexistingFile.class)
    public void InexistingFileTest() throws MyException{
        Stmt openStmt = new OpenFileStmt(new ConstExp(new StringValue("helloWorld")));
        try{
            openStmt.exec(state);
        }
        catch (ThreadException te){
            throw te.getException();
        }
    }

}
