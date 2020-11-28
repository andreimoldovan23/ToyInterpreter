package ToyInterpreter.tests.testModel;

import ToyInterpreter.Main;
import ToyInterpreter.exceptions.MyException;
import ToyInterpreter.exceptions.NotAllocated;
import ToyInterpreter.exceptions.ThreadException;
import ToyInterpreter.model.PrgState;
import ToyInterpreter.model.adts.*;
import ToyInterpreter.model.exps.ConstExp;
import ToyInterpreter.model.exps.VarExp;
import ToyInterpreter.model.stmts.*;
import ToyInterpreter.model.types.Ref;
import ToyInterpreter.model.types.StringType;
import ToyInterpreter.model.values.IntValue;
import ToyInterpreter.model.values.StringValue;
import ToyInterpreter.model.values.Value;
import org.junit.*;

import java.io.IOException;

public class testWriteHeapStmt {

    private PrgState state;

    @BeforeClass
    public static void printName(){
        System.out.println("Testing WriteHeapStmt");
    }

    @AfterClass
    public static void printEnd(){
        System.out.println();
    }

    @Before
    public void setUp(){
        state = new PrgState(new ExeStack<>(), new SymTable<>(), new Out<>(), new FileTable<>(),
                new Heap<>(), new NOP());
    }

    @After
    public void tearDown() throws IOException {
        state.getTable().clear();
        Main.clean(state.getOut(), state.getFileTable(), state.getHeap());
        state = null;
    }

    @Test
    public void toStringTest(){
        Stmt s = new WriteHeapStmt("varName", new ConstExp(new IntValue(10)));
        String expected = "writeHeap (varName, 10)\n";
        Assert.assertEquals("Testing toString method of WriteHeapTest", expected, s.toString());
    }

    @Test
    public void toStringPrefixTest(){
        Stmt s = new WriteHeapStmt("varName", new ConstExp(new IntValue(10)));
        String expected = "\twriteHeap (varName, 10)\n";
        Assert.assertEquals("Testing toString method of WriteHeapTest", expected, s.toStringPrefix("\t"));
    }

    @Test
    public void execTest() throws MyException {
        Stmt s1 = new VarDecl(new Ref(new StringType()), new VarExp("myString"));
        Stmt s2 = new NewStmt("myString", new ConstExp(new StringValue("Hello world")));
        Stmt s3 = new WriteHeapStmt("myString", new ConstExp(new StringValue("Changed my mind")));

        s1.exec(state);
        s2.exec(state);
        s3.exec(state);
        ISymTable<String, Value> table = state.getTable();
        IHeap<Integer, Value> heap = state.getHeap();
        int addr = (int) table.lookup("myString").getValue();
        Assert.assertEquals("Testing exec method of WriteHeapStmt",
                heap.lookup(addr).getValue(), "Changed my mind");
    }

    @Test(expected = NotAllocated.class)
    public void NotAllocatedTest() throws MyException{
        Stmt s1 = new VarDecl(new Ref(new StringType()), new VarExp("myString"));
        Stmt s2 = new WriteHeapStmt("myString", new ConstExp(new StringValue("Changed my mind")));

        s1.exec(state);
        try{
            s2.exec(state);
        }
        catch (ThreadException te){
            throw te.getException();
        }
    }

}
