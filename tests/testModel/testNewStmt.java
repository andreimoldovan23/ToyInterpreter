package ToyInterpreter.tests.testModel;

import ToyInterpreter.exceptions.*;
import ToyInterpreter.model.PrgState;
import ToyInterpreter.model.adts.*;
import ToyInterpreter.model.exps.ConstExp;
import ToyInterpreter.model.exps.VarExp;
import ToyInterpreter.model.stmts.NOP;
import ToyInterpreter.model.stmts.NewStmt;
import ToyInterpreter.model.stmts.Stmt;
import ToyInterpreter.model.stmts.VarDecl;
import ToyInterpreter.model.types.Int;
import ToyInterpreter.model.types.Ref;
import ToyInterpreter.model.values.IntValue;
import ToyInterpreter.model.values.StringValue;
import ToyInterpreter.model.values.Value;
import org.junit.*;

import java.io.IOException;

public class testNewStmt {

    private PrgState state;

    @BeforeClass
    public static void printName(){
        System.out.println("Testing NewStmt");
    }

    @AfterClass
    public static void printEnd(){
        System.out.println();
    }

    @Before
    public void setUp() {
        state = new PrgState(new ExeStack<>(), new SymTable<>(), new Out<>(), new FileTable<>(), new Heap<>(),
                new NOP());
    }

    @After
    public void tearDown() throws IOException {
        state.cleanAll();
        state = null;
    }

    @Test
    public void toStringTest() {
        Stmt s = new NewStmt("numberRef", new ConstExp(new IntValue(10)));
        String expected = "new (numberRef, 10)\n";
        Assert.assertEquals("Testing toString method of NewStmt", expected, s.toString());
    }

    @Test
    public void toStringPrefixTest(){
        Stmt s = new NewStmt("numberRef", new ConstExp(new IntValue(10)));
        String expected = "\tnew (numberRef, 10)\n";
        Assert.assertEquals("Testing toStringPrefix method of NewStmt", expected, s.toStringPrefix("\t"));
    }

    @Test
    public void execTest() throws MyException {
        Stmt s1 = new VarDecl(new Ref(new Int()), new VarExp("numberVar"));
        Stmt s2 = new NewStmt("numberVar", new ConstExp(new IntValue(10)));

        s1.exec(state);
        s2.exec(state);

        IHeap<Integer, Value> heap = state.getHeap();
        ISymTable<String, Value> table = state.getTable();
        int addr = (int) table.lookup("numberVar").getValue();
        Assert.assertEquals("Testing exec method of NewStmt", heap.lookup(addr).getValue(), 10);
    }

    @Test(expected = IsNotDefinedException.class)
    public void IsNotDefinedTest() throws MyException{
        Stmt s = new NewStmt("numberVar", new ConstExp(new IntValue(10)));
        Assert.assertNotNull("Testing IsNotDefined exception within exec method of NewStmt",
                s.exec(state));
    }

    @Test(expected = InvalidAllocation.class)
    public void InvalidAllocationTest() throws MyException{
        Stmt s1 = new VarDecl(new Int(), new VarExp("numberVar"));
        Stmt s2 = new NewStmt("numberVar", new ConstExp(new IntValue(10)));
        s1.exec(state);
        Assert.assertNotNull("Testing InvalidAllocation exception within exec method of NewStmt",
                s2.exec(state));
    }

    @Test(expected = InvalidAllocationType.class)
    public void InvalidAllocationTypeTest() throws MyException{
        Stmt s1 = new VarDecl(new Ref(new Int()), new VarExp("numberVar"));
        Stmt s2 = new NewStmt("numberVar", new ConstExp(new StringValue("Hello World")));
        s1.exec(state);
        Assert.assertNotNull("Testing InvalidAllocationType exception within exec method of NewStmt",
                s2.exec(state));
    }

}
