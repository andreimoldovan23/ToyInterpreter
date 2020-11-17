package ToyInterpreter.tests.testModel;

import ToyInterpreter.exceptions.MyException;
import ToyInterpreter.model.PrgState;
import ToyInterpreter.model.adts.*;
import ToyInterpreter.model.exps.ConstExp;
import ToyInterpreter.model.exps.Exp;
import ToyInterpreter.model.exps.VarExp;
import ToyInterpreter.model.stmts.AssignStmt;
import ToyInterpreter.model.stmts.PrintStmt;
import ToyInterpreter.model.stmts.Stmt;
import ToyInterpreter.model.stmts.VarDecl;
import ToyInterpreter.model.types.Int;
import ToyInterpreter.model.values.IntValue;
import ToyInterpreter.model.values.StringValue;
import ToyInterpreter.model.values.Value;
import org.junit.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class testPrgState {
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
    public void setUp() throws MyException {
        var = new VarExp("a");
        Stmt s1 = new VarDecl(new Int(), var);
        Stmt s2 = new AssignStmt(var, new ConstExp(new IntValue(10)));
        Stmt s3 = new PrintStmt(var);

        state = new PrgState(new ExeStack<>(), new SymTable<>(), new Out<>(), new FileTable<>(),
                new ArrayList<>(List.of(s1, s2, s3)));
        IExeStack<Stmt> stack = state.getStack();
        while(!stack.empty()) {
            Stmt s = stack.pop();
            state = s.exec(state);
            stack = state.getStack();
        }
    }

    @After
    public void tearDown(){
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
    public void resetTest() throws MyException, IOException {
        state.reset();
        ISymTable<String, Value> table = state.getTable();
        IOut<String> out = state.getOut();
        IFileTable<StringValue, MyBufferedReader> fileTable = state.getFileTable();
        Assert.assertEquals("Testing reset method of PrgState", "", table.toString());
        Assert.assertEquals("", out.toString());
        Assert.assertEquals("", fileTable.toString());
    }

}
