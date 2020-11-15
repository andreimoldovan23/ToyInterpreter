package tests.testModel;

import exceptions.*;
import model.PrgState;
import model.adts.*;
import model.exps.*;
import model.stmts.*;
import model.types.*;
import model.values.*;
import org.junit.*;

public class testStmts {

    private Exp varExpInt, varExpBool;
    private Exp constExpInt, constExpBool;
    private Type intType, boolType;
    private Exp arithmeticExp, logicExp;
    private PrgState state;

    @BeforeClass
    public static void printName(){
        System.out.println("Testing Stmt classes");
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
        constExpBool = new ConstExp(new True());
        varExpInt = new VarExp("number");
        varExpBool = new VarExp("boolean");
        arithmeticExp = new ArithmeticExp(constExpInt, constExpInt, "*");
        logicExp = new LogicExp(new ConstExp(new False()), new ConstExp(new False()), "|");
        state = new PrgState(new ExeStack<>(), new SymTable<>(), new Out<>(), new NOP());
    }

    @Test
    public void execVarDeclTest() throws MyException {
        Stmt varDeclInt = new VarDecl(intType, varExpInt);
        state = varDeclInt.exec(state);
        ISymTable<String, Value> table = state.getTable();
        Assert.assertTrue("Testing exec method of VarDecl", table.isDefined(varExpInt.toString()));
    }

    @Test(expected = InvalidVariable.class)
    public void InvalidVariableVarDeclTest() throws MyException{
        new VarDecl(intType, constExpInt);
        System.out.println("Testing InvalidVariable exception within VarDecl");
    }

    @Test(expected = IsAlreadyDefined.class)
    public void IsAlreadyDefinedTest() throws MyException {
        Stmt varDeclInt = new VarDecl(intType, varExpInt);
        state = varDeclInt.exec(state);
        Assert.assertNotNull("Testing IsAlreadyDefined exception within VarDecl",
                varDeclInt.exec(state));
    }

    @Test
    public void execPrintStmtTest() throws MyException {
        Stmt printStmt = new PrintStmt(arithmeticExp);
        String expected = "100\n";
        state = printStmt.exec(state);
        IOut<String> out = state.getOut();
        Assert.assertEquals("Testing exec method of PrintStmt", expected, out.toString());
    }

    @Test
    public void execAssignStmtTest() throws MyException{
        Stmt varDeclInt = new VarDecl(intType, varExpInt);
        state = varDeclInt.exec(state);

        Stmt assignStmt = new AssignStmt(varExpInt, constExpInt);
        state = assignStmt.exec(state);
        ISymTable<String, Value> table = state.getTable();
        Assert.assertEquals("Testing exec method of AssignStmt", 10,
                table.lookup(varExpInt.toString()).getValue());
    }

    @Test(expected = InvalidVariable.class)
    public void InvalidVariableAssignStmtTest() throws MyException{
        new AssignStmt(constExpInt, constExpInt);
        System.out.println("Testing InvalidVariable exception within AssignStmt");
    }

    @Test(expected = InvalidAssignTypeException.class)
    public void InvalidAssignTypeTest() throws MyException{
        Stmt varDeclInt = new VarDecl(intType, varExpInt);
        Stmt assignStmt = new AssignStmt(varExpInt, constExpBool);

        state = varDeclInt.exec(state);
        Assert.assertNotNull("Testing InvalidAssignType exception within AssignStmt", assignStmt.exec(state));
    }

    @Test
    public void execIfStmtTest() throws MyException{
        Stmt varDeclBool = new VarDecl(boolType, varExpBool);
        Stmt varDeclInt = new VarDecl(intType, varExpInt);
        Stmt ifStmt = new IfStmt(logicExp, varDeclBool, varDeclInt);
        state = ifStmt.exec(state);
        IExeStack<Stmt> stack = state.getStack();
        Stmt s = stack.pop();
        state = s.exec(state);
        Assert.assertTrue("Testing exec method of IfStmt", state.getTable().isDefined(varExpInt.toString()));
    }

    @Test(expected = InvalidIfCondition.class)
    public void InvalidIfConditionTest() throws MyException {
        Stmt varDeclBool = new VarDecl(boolType, varExpBool);
        Stmt varDeclInt = new VarDecl(intType, varExpInt);
        Stmt ifStmt = new IfStmt(arithmeticExp, varDeclBool, varDeclInt);
        Assert.assertNotNull("Testing InvalidIfCondition exception within IfStmt", ifStmt.exec(state));
    }

    @Test
    public void execCompStmtTest() throws MyException{
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
    public void execNOPTest() throws MyException {
        Stmt nop = new NOP();
        state = nop.exec(state);
        IExeStack<Stmt> stack = state.getStack();
        ISymTable<String, Value> table = state.getTable();
        IOut<String> out = state.getOut();
        Assert.assertEquals("Testing exec method of NOP", "[No operation]\n", stack.toString());
        Assert.assertEquals("", table.toString());
        Assert.assertEquals("", out.toString());
    }

    @Test
    public void toStringTest() throws InvalidVariable {
        Stmt varDecl = new VarDecl(intType, varExpInt);
        Stmt printStmt = new PrintStmt(arithmeticExp);
        Stmt assignStmt = new AssignStmt(varExpInt, constExpInt);
        Stmt ifStmt = new IfStmt(logicExp, printStmt, assignStmt);
        Stmt nop = new NOP();
        Stmt compStmt = new CompStmt(ifStmt, nop);

        String varDeclString = "int number\n";
        String printStmtString = "print (10 * 10)\n";
        String assignStmtString = "number=10\n";
        String ifStmtString = "if (false | false) then \n\t" + printStmtString + "else \n\t" + assignStmtString;

        Assert.assertEquals("Testing toString method of VarDecl", varDeclString, varDecl.toString());
        Assert.assertEquals("Testing toString method of PrintStmt", printStmtString, printStmt.toString());
        Assert.assertEquals("Testing toString method of AssignStmt", assignStmtString, assignStmt.toString());
        Assert.assertEquals("Testing toString method of IfStmt", ifStmtString, ifStmt.toString());
        Assert.assertEquals("Testing toString method of NOP", "No operation\n", nop.toString());
        Assert.assertEquals("Testing toString method of CompStmt", ifStmtString + "No operation\n",
                compStmt.toString());
    }

    @Test
    public void toStringPrefixTest() throws InvalidVariable{
        Stmt varDecl = new VarDecl(intType, varExpInt);
        Stmt printStmt = new PrintStmt(arithmeticExp);
        Stmt assignStmt = new AssignStmt(varExpInt, constExpInt);
        Stmt nop = new NOP();
        Stmt compStmt = new CompStmt(varDecl, nop);
        Stmt ifStmtInner = new IfStmt(logicExp, printStmt, assignStmt);
        Stmt elseStmtInner = new IfStmt(logicExp, compStmt, nop);
        Stmt ifStmt = new IfStmt(logicExp, ifStmtInner, elseStmtInner);

        String expected = "if (false | false) then \n" +
                "\tif (false | false) then \n" +
                "\t\tprint (10 * 10)\n" +
                "\telse \n" +
                "\t\tnumber=10\n" +
                "else \n" +
                "\tif (false | false) then \n" +
                "\t\tint number\n" +
                "\t\tNo operation\n" +
                "\telse \n" +
                "\t\tNo operation\n";

        Assert.assertEquals("Testing toStringPrefix method of all Stmt classes", expected, ifStmt.toString());
    }

}
