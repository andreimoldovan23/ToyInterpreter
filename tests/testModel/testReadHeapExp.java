package ToyInterpreter.tests.testModel;

import ToyInterpreter.exceptions.InvalidAddress;
import ToyInterpreter.exceptions.InvalidReadHeapType;
import ToyInterpreter.exceptions.InvalidVariable;
import ToyInterpreter.exceptions.MyException;
import ToyInterpreter.model.PrgState;
import ToyInterpreter.model.adts.*;
import ToyInterpreter.model.exps.ConstExp;
import ToyInterpreter.model.exps.Exp;
import ToyInterpreter.model.exps.ReadHeapExp;
import ToyInterpreter.model.exps.VarExp;
import ToyInterpreter.model.stmts.NOP;
import ToyInterpreter.model.stmts.NewStmt;
import ToyInterpreter.model.stmts.Stmt;
import ToyInterpreter.model.stmts.VarDecl;
import ToyInterpreter.model.types.Int;
import ToyInterpreter.model.types.Ref;
import ToyInterpreter.model.values.IntValue;
import org.junit.*;
import java.io.IOException;

public class testReadHeapExp {

    private PrgState state;
    private Exp varExp, constExp, readHeap;
    private Stmt declStmt, newStmt;

    @BeforeClass
    public static void printName(){
        System.out.println("Testing ReadHeapExp");
    }

    @AfterClass
    public static void printEnd(){
        System.out.println();
    }

    @Before
    public void setUp() throws InvalidVariable {
        varExp = new VarExp("myVar");
        constExp = new ConstExp(new IntValue(10));
        declStmt = new VarDecl(new Ref(new Int()), varExp);
        newStmt = new NewStmt(varExp.toString(), constExp);
        state = new PrgState(new ExeStack<>(), new SymTable<>(), new Out<>(), new FileTable<>(),
                new Heap<>(), new NOP());
    }

    @After
    public void tearDown() throws IOException {
        state.cleanAll();
        newStmt = null;
        declStmt = null;
        constExp = null;
        varExp = null;
        state = null;
        readHeap = null;
    }

    @Test
    public void evalTest() throws MyException {
        declStmt.exec(state);
        newStmt.exec(state);
        readHeap = new ReadHeapExp(varExp);
        Assert.assertEquals("Testing eval method of ReadHeapExp",
                readHeap.eval(state.getTable(), state.getHeap()).getValue(), 10);
    }

    @Test(expected = InvalidReadHeapType.class)
    public void InvalidReadHeapTypeTest() throws MyException {
        Stmt s = new VarDecl(new Int(), new VarExp("myNewVar"));
        s.exec(state);
        readHeap = new ReadHeapExp(new VarExp("myNewVar"));
        Assert.assertNotNull("Testing InvalidReadHeapType exception of eval method of ReadHeapExp",
                readHeap.eval(state.getTable(), state.getHeap()));
    }

    @Test(expected = InvalidAddress.class)
    public void InvalidAddressTest() throws MyException {
        declStmt.exec(state);
        readHeap = new ReadHeapExp(varExp);
        Assert.assertNotNull("Testing InvalidAddress exception of eval method of ReadHeapExp",
                readHeap.eval(state.getTable(), state.getHeap()));
    }

    @Test
    public void toStringTest() {
        String expected = "readHeap (myVar)";
        readHeap = new ReadHeapExp(varExp);
        Assert.assertEquals("Testing toString method ReadHeapExp", readHeap.toString(), expected);
    }

    @Test
    public void equalsTest() {
        readHeap = new ReadHeapExp(varExp);
        Assert.assertEquals("Testing equals method of ReadHeapExp",
                readHeap, new ReadHeapExp(new ConstExp(new IntValue(10))));
    }

}
