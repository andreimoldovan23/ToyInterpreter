package ToyInterpreter.tests.testModel;

import ToyInterpreter.Main;
import ToyInterpreter.exceptions.InvalidVariable;
import ToyInterpreter.exceptions.MyException;
import ToyInterpreter.model.PrgState;
import ToyInterpreter.model.adts.*;
import ToyInterpreter.model.exps.ConstExp;
import ToyInterpreter.model.exps.ReadHeapExp;
import ToyInterpreter.model.exps.RelationalExp;
import ToyInterpreter.model.exps.VarExp;
import ToyInterpreter.model.stmts.*;
import ToyInterpreter.model.types.Int;
import ToyInterpreter.model.types.Ref;
import ToyInterpreter.model.values.IntValue;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

public class testForkStmt {

    private Stmt fork;

    @BeforeClass
    public static void printName(){
        System.out.println("Testing ForkStmt");
    }

    @AfterClass
    public static void printEnd(){
        System.out.println();
    }

    @Before
    public void setUp() throws InvalidVariable {
        Stmt assign = new AssignStmt(new VarExp("var"), new ConstExp(new IntValue(20)));
        Stmt ifStmt = new IfStmt(
                new RelationalExp(new VarExp("var"), new ConstExp(new IntValue(0)), ">"),
                new PrintStmt(new VarExp("var")),
                new NOP());
        fork = new ForkStmt(Main.assemble(new ArrayList<>(List.of(assign, ifStmt))));
    }

    @After
    public void tearDown() {
        fork = null;
    }

    @Test
    public void toStringTest() {
        String expected = "fork (var=20 | if (var > 0) then print var | else No operation | No operation)\n";
        Assert.assertEquals("Testing toString method of ForkStmt", expected, fork.toString());
    }

    @Test
    public void toStringPrefixTest() {
        String expected = "\tfork (var=20 | if (var > 0) then print var | else No operation | No operation)\n";
        Assert.assertEquals("Testing toStringPrefix method of ForkStmt", expected, fork.toStringPrefix("\t"));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void execTest() throws MyException {
        Stmt s1 = new VarDecl(new Int(), new VarExp("var"));
        Stmt s2 = new AssignStmt(new VarExp("var"), new ConstExp(new IntValue(10)));
        Stmt s3 = new VarDecl(new Ref(new Int()), new VarExp("refVar"));
        Stmt s4 = new NewStmt("refVar", new ConstExp(new IntValue(20)));
        Stmt s5 = new PrintStmt(new ReadHeapExp(new VarExp("refVar")));
        PrgState original = new PrgState(new ExeStack<>(), new SymTable<>(), new Out<>(),
                new FileTable<>(), new Heap<>(), Main.assemble(new ArrayList<>(List.of(s1, s2, s3, s4, s5, fork))));

        Stmt top;
        PrgState newState, newProgram = null;
        IExeStack<Stmt> stack = original.getStack();
        while(!stack.empty()){
            top = stack.pop();
            newState = top.exec(original);
            if(newState != null)
                newProgram = newState;
        }

        stack = newProgram.getStack();
        while(!stack.empty()){
            top = stack.pop();
            top.exec(newProgram);
        }

        String expectedSymTable = "refVar=(1 -> int)\nvar=20\n";
        String expectedOut = "20\n20\n";
        String expectedHeap = "1-->20\n";
        Assert.assertEquals("Testing exec method of ForkStmt", expectedSymTable,
                newProgram.getTable().toString());
        Assert.assertEquals(expectedOut, newProgram.getOut().toString());
        Assert.assertEquals(expectedHeap, newProgram.getHeap().toString());
    }

}
