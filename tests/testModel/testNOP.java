package ToyInterpreter.tests.testModel;

import ToyInterpreter.exceptions.*;
import ToyInterpreter.model.PrgState;
import ToyInterpreter.model.adts.*;
import ToyInterpreter.model.stmts.*;
import ToyInterpreter.model.values.*;
import org.junit.*;

import java.io.IOException;

public class testNOP {

    private PrgState state;

    @BeforeClass
    public static void printName(){
        System.out.println("Testing NOPStmt");
    }

    @AfterClass
    public static void printEnd(){
        System.out.println();
    }

    @Before
    public void setUp(){
        state = new PrgState(new ExeStack<>(), new SymTable<>(), new Out<>(), new FileTable<>(), new Heap<>(),
                new NOP());
    }

    @SuppressWarnings("DuplicatedCode")
    @After
    public void tearDown() throws IOException {
        state.cleanAll();
        state = null;
    }

    @Test
    public void execNOPTest() throws MyException {
        Stmt nop = new NOP();
        nop.exec(state);
        IExeStack<Stmt> stack = state.getStack();
        ISymTable<String, Value> table = state.getTable();
        IOut<String> out = state.getOut();
        Assert.assertEquals("Testing exec method of NOP", "[No operation]\n", stack.toString());
        Assert.assertEquals("", table.toString());
        Assert.assertEquals("", out.toString());
    }

    @Test
    public void toStringTest() {
        Stmt nop = new NOP();
        Assert.assertEquals("Testing toString method of NOP", nop.toString(), "No operation\n");
    }

    @Test
    public void toStringPrefixTest() {
        Stmt nop = new NOP();
        String expected = "\tNo operation\n";
        Assert.assertEquals("Testing toStringPrefix method of NOP",
                expected, nop.toStringPrefix("\t"));
    }

}
