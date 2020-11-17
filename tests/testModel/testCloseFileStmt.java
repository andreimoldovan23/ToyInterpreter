package ToyInterpreter.tests.testModel;

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

public class testCloseFileStmt {

    @Rule
    public TemporaryFolder myFolder = new TemporaryFolder();

    private Exp varExpString;
    private PrgState state;

    @BeforeClass
    public static void printName(){
        System.out.println("Testing CloseFileStmt");
    }

    @AfterClass
    public static void printEnd(){
        System.out.println();
    }

    @Before
    public void setUp(){
        varExpString = new ConstExp(new StringValue("myTestFile.txt"));
        state = new PrgState(new ExeStack<>(), new SymTable<>(), new Out<>(), new FileTable<>(), new NOP());
    }

    @SuppressWarnings("DuplicatedCode")
    @After
    public void tearDown(){
        varExpString = null;
        state = null;
    }

    @Test
    public void toStringTest() {
        Stmt closeStmt = new CloseFileStmt(varExpString);
        Assert.assertEquals("Testing toString method of CloseFileStmt", closeStmt.toString(),
                "closeFile (myTestFile.txt)\n");
        }

    @Test
    public void toStringPrefixTest() {
        Stmt closeStmt = new CloseFileStmt(varExpString);
        String expected = "\tcloseFile (myTestFile.txt)\n";
        Assert.assertEquals("Testing toStringPrefix method of CloseFileStmt", expected,
                closeStmt.toStringPrefix("\t"));
    }

    @Test
    public void execTest() throws IOException, MyException {
        File myFile = myFolder.newFile(varExpString.toString());
        StringValue stringValue = new StringValue(myFile.getAbsolutePath());
        Stmt openStmt = new OpenFileStmt(new ConstExp(stringValue));
        Stmt closeStmt = new CloseFileStmt(new ConstExp(stringValue));
        state = openStmt.exec(state);
        closeStmt.exec(state);
        IFileTable<StringValue, MyBufferedReader> fileTable = state.getFileTable();
        Assert.assertFalse("Testing exec method of CloseFileStmt", fileTable.isDefined(stringValue));
    }

    @Test(expected = InvalidFilenameException.class)
    public void InvalidFilenameCloseTest() throws MyException {
        Stmt closeStmt = new CloseFileStmt(new ConstExp(new IntValue(7)));
        Assert.assertNotNull("Testing InvalidFilename exception of exec method of CloseFileStmt",
                closeStmt.exec(state));
    }

    @Test(expected = FileNotOpen.class)
    public void FileNotOpenTest() throws MyException{
        Stmt closeStmt = new CloseFileStmt(new ConstExp(new StringValue("hello")));
        Assert.assertNotNull("Testing FileNotOpen exception of exec method of CloseFileStmt",
                closeStmt.exec(state));
    }

}
