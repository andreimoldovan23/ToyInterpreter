package ToyInterpreter.tests.testModel;

import ToyInterpreter.Main;
import ToyInterpreter.exceptions.InvalidVariable;
import ToyInterpreter.exceptions.InvalidWhileTypeException;
import ToyInterpreter.exceptions.MyException;
import ToyInterpreter.model.PrgState;
import ToyInterpreter.model.adts.*;
import ToyInterpreter.model.exps.*;
import ToyInterpreter.model.stmts.*;
import ToyInterpreter.model.types.Int;
import ToyInterpreter.model.values.IntValue;
import ToyInterpreter.model.values.StringValue;
import org.junit.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class testWhileStmt {

    private PrgState state;
    private Stmt whileStmt, declVar, assignVar;

    @BeforeClass
    public static void printName(){
        System.out.println("Testing WhileStmt");
    }

    @AfterClass
    public static void printEnd(){
        System.out.println();
    }

    @Before
    public void setUp() throws InvalidVariable {
        Exp var = new VarExp("var");
        Exp relational = new RelationalExp(var, new ConstExp(new IntValue(0)), ">");

        declVar = new VarDecl(new Int(), var);
        assignVar = new AssignStmt(var, new ConstExp(new IntValue(123)));

        Stmt assignDigit = new AssignStmt(var, new ArithmeticExp(var, new ConstExp(new IntValue(10)), "/"));
        Stmt printVar = new PrintStmt(var);
        whileStmt = new WhileStmt(relational,
                Main.assemble(new ArrayList<>(List.of(assignDigit, printVar))));
    }

    @After
    public void tearDown() throws IOException {
        if(state != null) {
            state.cleanAll();
            state = null;
        }
        declVar = null;
        assignVar = null;
        whileStmt = null;
    }

    @Test
    public void toStringTest() {
        String expected = "while (var > 0) then\n" +
                "\tvar=(var / 10)\n" +
                "\tprint var\n" +
                "\tNo operation\n";
        Assert.assertEquals("Testing toString method of WhileStmt", expected, whileStmt.toString());
    }

    @Test
    public void toStringPrefixTest(){
        String expected = "\twhile (var > 0) then\n" +
                "\t\tvar=(var / 10)\n" +
                "\t\tprint var\n" +
                "\t\tNo operation\n";
        Assert.assertEquals("Testing toStringPrefix method of WhileStmt", expected, whileStmt.toStringPrefix("\t"));
    }

    @Test
    public void execTest() throws MyException {
        state = new PrgState(new ExeStack<>(), new SymTable<>(), new Out<>(), new FileTable<>(),
                new Heap<>(), Main.assemble(new ArrayList<>(List.of(declVar, assignVar, whileStmt))));
        IExeStack<Stmt> stack = state.getStack();
        Stmt top = stack.pop();
        while(true){
            top.exec(state);
            if(!stack.empty())
                top = stack.pop();
            else
                break;
        }

        String expected =
                "12\n" +
                "1\n" +
                "0\n";
        Assert.assertEquals("Testing exec method of WhileStmt", expected, state.getOut().toString());
    }

    @Test(expected = InvalidWhileTypeException.class)
    public void InvalidWhileTypeExceptionTest() throws MyException {
        Stmt whileInvalid = new WhileStmt(new ArithmeticExp(new ConstExp(new IntValue(5)),
                new ConstExp(new IntValue(7)), "+"), new PrintStmt(new ConstExp(new StringValue("Hello"))));
        state = new PrgState(new ExeStack<>(), new SymTable<>(), new Out<>(), new FileTable<>(),
                new Heap<>(), new NOP());
        Assert.assertNotNull("Testing InvalidWhileType exception of exec method of WhileStmt",
                whileInvalid.exec(state));
    }

}
