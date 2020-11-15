package tests.testModel;

import exceptions.MyException;
import model.PrgState;
import model.adts.*;
import model.exps.ConstExp;
import model.exps.Exp;
import model.exps.VarExp;
import model.stmts.AssignStmt;
import model.stmts.PrintStmt;
import model.stmts.Stmt;
import model.stmts.VarDecl;
import model.types.Int;
import model.values.IntValue;
import model.values.Value;
import org.junit.*;

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

        state = new PrgState(new ExeStack<>(), new SymTable<>(), new Out<>(),
                new ArrayList<>(List.of(s1, s2, s3)));
        IExeStack<Stmt> stack = state.getStack();
        while(!stack.empty()) {
            Stmt s = stack.pop();
            state = s.exec(state);
            stack = state.getStack();
        }
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
    public void resetTest() throws MyException{
        state.reset();
        ISymTable<String, Value> table = state.getTable();
        IOut<String> out = state.getOut();
        Assert.assertEquals("Testing reset method of PrgState", "", table.toString());
        Assert.assertEquals("", out.toString());
    }

}
