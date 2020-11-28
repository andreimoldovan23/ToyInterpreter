package ToyInterpreter.tests.testModel;

import ToyInterpreter.Main;
import ToyInterpreter.exceptions.*;
import ToyInterpreter.model.PrgState;
import ToyInterpreter.model.adts.*;
import ToyInterpreter.model.exps.*;
import ToyInterpreter.model.stmts.*;
import ToyInterpreter.model.types.*;
import ToyInterpreter.model.values.*;
import org.junit.*;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;

public class testIfStmt {

    @Rule
    public TemporaryFolder myFolder = new TemporaryFolder();

    private Exp varExpInt, varExpBool;
    private Exp constExpInt;
    private Type intType, boolType;
    private Exp arithmeticExp, logicExp;
    private PrgState state;

    @BeforeClass
    public static void printName(){
        System.out.println("Testing IfStmt");
    }

    @AfterClass
    public static void printEnd(){
        System.out.println();
    }

    @Before
    public void setUp(){
        intType = new Int();
        boolType = new Bool();
        constExpInt = new ConstExp(new IntValue(10));
        varExpInt = new VarExp("number");
        varExpBool = new VarExp("boolean");
        arithmeticExp = new ArithmeticExp(constExpInt, constExpInt, "*");
        logicExp = new LogicExp(new ConstExp(new False()), new ConstExp(new False()), "|");
        state = new PrgState(new ExeStack<>(), new SymTable<>(), new Out<>(), new FileTable<>(), new Heap<>(),
                new NOP());
    }

    @SuppressWarnings("DuplicatedCode")
    @After
    public void tearDown() throws IOException {
        state.getTable().clear();
        Main.clean(state.getOut(), state.getFileTable(), state.getHeap());
        intType = null;
        boolType = null;
        constExpInt = null;
        varExpBool = null;
        varExpInt = null;
        arithmeticExp = null;
        logicExp = null;
        state = null;
    }

    @Test
    public void execTest() throws MyException{
        Stmt varDeclBool = new VarDecl(boolType, varExpBool);
        Stmt varDeclInt = new VarDecl(intType, varExpInt);
        Stmt ifStmt = new IfStmt(logicExp, varDeclBool, varDeclInt);
        ifStmt.exec(state);
        IExeStack<Stmt> stack = state.getStack();
        Stmt s = stack.pop();
        s.exec(state);
        Assert.assertTrue("Testing exec method of IfStmt", state.getTable().isDefined(varExpInt.toString()));
    }

    @Test(expected = InvalidIfCondition.class)
    public void InvalidIfConditionTest() throws MyException {
        Stmt varDeclBool = new VarDecl(boolType, varExpBool);
        Stmt varDeclInt = new VarDecl(intType, varExpInt);
        Stmt ifStmt = new IfStmt(arithmeticExp, varDeclBool, varDeclInt);
        try{
            ifStmt.exec(state);
        }
        catch (ThreadException te){
            throw te.getException();
        }
    }

    @Test
    public void toStringTest() throws InvalidVariable {
        Stmt printStmt = new PrintStmt(arithmeticExp);
        Stmt assignStmt = new AssignStmt(varExpInt, constExpInt);
        Stmt ifStmt = new IfStmt(logicExp, printStmt, assignStmt);
        String printStmtString = "print (10 * 10)\n";
        String assignStmtString = "number=10\n";
        String ifStmtString = "if (false | false) then \n\t" + printStmtString + "else \n\t" + assignStmtString;
        Assert.assertEquals("Testing toString method of IfStmt", ifStmtString, ifStmt.toString());
    }

    @Test
    public void toStringPrefixTest() throws InvalidVariable{
        Stmt printStmt = new PrintStmt(arithmeticExp);
        Stmt assignStmt = new AssignStmt(varExpInt, constExpInt);
        Stmt ifStmt = new IfStmt(logicExp, printStmt, assignStmt);

        String expected = "\tif (false | false) then \n" +
                "\t\tprint (10 * 10)\n" +
                "\telse \n" +
                "\t\tnumber=10\n";

        Assert.assertEquals("Testing toStringPrefix method of IfStmt",
                expected, ifStmt.toStringPrefix("\t"));
    }

}
