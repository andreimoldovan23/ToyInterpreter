package ToyInterpreter.tests.testModel;

import ToyInterpreter.Main;
import ToyInterpreter.exceptions.*;
import ToyInterpreter.model.PrgState;
import ToyInterpreter.model.adts.*;
import ToyInterpreter.model.exps.*;
import ToyInterpreter.model.stmts.*;
import ToyInterpreter.model.values.*;
import org.junit.*;

import java.io.IOException;

public class testPrintStmt {

    private Exp constExpInt;
    private Exp arithmeticExp;
    private PrgState state;

    @BeforeClass
    public static void printName(){
        System.out.println("Testing PrintStmt");
    }

    @AfterClass
    public static void printEnd(){
        System.out.println();
    }

    @Before
    public void setUp(){
        constExpInt = new ConstExp(new IntValue(10));
        arithmeticExp = new ArithmeticExp(constExpInt, constExpInt, "*");
        state = new PrgState(new ExeStack<>(), new SymTable<>(), new Out<>(), new FileTable<>(), new Heap<>(),
                new NOP());
    }

    @SuppressWarnings("DuplicatedCode")
    @After
    public void tearDown() throws IOException {
        state.getTable().clear();
        Main.clean(state.getOut(), state.getFileTable(), state.getHeap());
        constExpInt = null;
        arithmeticExp = null;
        state = null;
    }

    @Test
    public void execTest() throws MyException {
        Stmt printStmt = new PrintStmt(arithmeticExp);
        String expected = "100\n";
        printStmt.exec(state);
        IOut<String> out = state.getOut();
        Assert.assertEquals("Testing exec method of PrintStmt", expected, out.toString());
    }


    @Test
    public void toStringTest() {
        Stmt printStmt = new PrintStmt(arithmeticExp);
        String printStmtString = "print (10 * 10)\n";
        Assert.assertEquals("Testing toString method of PrintStmt", printStmtString, printStmt.toString());
    }

    @Test
    public void toStringPrefixTest() {
        Stmt printStmt = new PrintStmt(arithmeticExp);
        String expected = "\tprint (10 * 10)\n";
        Assert.assertEquals("Testing toStringPrefix method of PrintStmt", expected,
                printStmt.toStringPrefix("\t"));
    }

}
