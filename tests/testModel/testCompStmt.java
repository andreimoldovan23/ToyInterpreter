package ToyInterpreter.tests.testModel;

import ToyInterpreter.exceptions.*;
import ToyInterpreter.model.PrgState;
import ToyInterpreter.model.adts.*;
import ToyInterpreter.model.exps.*;
import ToyInterpreter.model.stmts.*;
import ToyInterpreter.model.types.*;
import ToyInterpreter.model.values.*;
import org.junit.*;
import org.junit.rules.TemporaryFolder;

public class testCompStmt {

    @Rule
    public TemporaryFolder myFolder = new TemporaryFolder();

    private Exp varExpInt, varExpBool, varExpString;
    private Exp constExpInt;
    private Type intType, boolType;
    private Exp arithmeticExp, logicExp;
    private PrgState state;

    @BeforeClass
    public static void printName(){
        System.out.println("Testing CompStmt");
    }

    @AfterClass
    public static void printEnd(){
        System.out.println();
    }

    @Before
    public void setUp(){
        boolType = new Bool();
        intType = new Int();
        constExpInt = new ConstExp(new IntValue(10));
        varExpInt = new VarExp("number");
        varExpBool = new VarExp("boolean");
        arithmeticExp = new ArithmeticExp(constExpInt, constExpInt, "*");
        logicExp = new LogicExp(new ConstExp(new False()), new ConstExp(new False()), "|");
        varExpString = new ConstExp(new StringValue("myTestFile.txt"));
        state = new PrgState(new ExeStack<>(), new SymTable<>(), new Out<>(), new FileTable<>(), new NOP());
    }

    @SuppressWarnings("DuplicatedCode")
    @After
    public void tearDown(){
        intType = null;
        boolType = null;
        constExpInt = null;
        varExpBool = null;
        varExpInt = null;
        arithmeticExp = null;
        logicExp = null;
        varExpString = null;
        state = null;
    }

    @Test
    public void execTest() throws MyException{
        Stmt varDeclBool = new VarDecl(boolType, varExpBool);
        Stmt varDeclInt = new VarDecl(intType, varExpInt);
        Stmt compStmt = new CompStmt(varDeclBool, varDeclInt);
        state = compStmt.exec(state);

        IExeStack<Stmt> stack = state.getStack();
        Stmt s1 = stack.pop();
        state = s1.exec(state);
        Stmt s2 = stack.pop();
        state = s2.exec(state);
        ISymTable<String, Value> table = state.getTable();

        Assert.assertTrue("Testing exec method of CompStmt", table.isDefined(varExpBool.toString()));
        Assert.assertTrue(table.isDefined(varExpInt.toString()));
    }

    @Test
    public void toStringTest() throws InvalidVariable {
        Stmt printStmt = new PrintStmt(arithmeticExp);
        Stmt assignStmt = new AssignStmt(varExpInt, constExpInt);
        Stmt ifStmt = new IfStmt(logicExp, printStmt, assignStmt);
        Stmt nop = new NOP();
        Stmt compStmt = new CompStmt(ifStmt, nop);
        String printStmtString = "print (10 * 10)\n";
        String assignStmtString = "number=10\n";
        String ifStmtString = "if (false | false) then \n\t" + printStmtString + "else \n\t" + assignStmtString;
        Assert.assertEquals("Testing toString method of CompStmt", ifStmtString + "No operation\n",
                compStmt.toString());
    }

    @Test
    public void toStringPrefixTest() throws InvalidVariable{
        Stmt varDecl = new VarDecl(intType, varExpInt);
        Stmt openStmt = new OpenFileStmt(varExpString);
        Stmt compStmt = new CompStmt(varDecl, openStmt);

        String expected = "\tint number\n" +
                "\topenFile (myTestFile.txt)\n";

        Assert.assertEquals("Testing toStringPrefix method of CompStmt",
                expected, compStmt.toStringPrefix("\t"));
    }

}
