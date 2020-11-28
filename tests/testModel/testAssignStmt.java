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

import java.io.IOException;

public class testAssignStmt {

    private Exp varExpInt;
    private Exp constExpInt, constExpBool;
    private Type intType;
    private PrgState state;

    @BeforeClass
    public static void printName(){
        System.out.println("Testing AssignStmt");
    }

    @AfterClass
    public static void printEnd(){
        System.out.println();
    }

    @Before
    public void setUp(){
        intType = new Int();
        constExpBool = new ConstExp(new True());
        constExpInt = new ConstExp(new IntValue(10));
        varExpInt = new VarExp("number");
        state = new PrgState(new ExeStack<>(), new SymTable<>(), new Out<>(), new FileTable<>(), new Heap<>(),
                new NOP());
    }

    @SuppressWarnings("DuplicatedCode")
    @After
    public void tearDown() throws IOException {
        state.getTable().clear();
        Main.clean(state.getOut(), state.getFileTable(), state.getHeap());
        intType = null;
        constExpInt = null;
        varExpInt = null;
        constExpBool = null;
        state = null;
    }

    @Test
    public void execTest() throws MyException{
        Stmt varDeclInt = new VarDecl(intType, varExpInt);
        varDeclInt.exec(state);
        Stmt assignStmt = new AssignStmt(varExpInt, constExpInt);
        assignStmt.exec(state);
        ISymTable<String, Value> table = state.getTable();
        Assert.assertEquals("Testing exec method of AssignStmt", 10,
                table.lookup(varExpInt.toString()).getValue());
    }

    @Test(expected = InvalidVariable.class)
    public void InvalidVariableTest() throws MyException{
        new AssignStmt(constExpInt, constExpInt);
        System.out.println("Testing InvalidVariable exception within AssignStmt");
    }

    @Test(expected = InvalidAssignTypeException.class)
    public void InvalidAssignTypeTest() throws MyException{
        Stmt varDeclInt = new VarDecl(intType, varExpInt);
        Stmt assignStmt = new AssignStmt(varExpInt, constExpBool);

        varDeclInt.exec(state);
        try{
            assignStmt.exec(state);
        }
        catch (ThreadException te){
            throw te.getException();
        }
    }

    @Test
    public void toStringTest() throws InvalidVariable {
        Stmt assignStmt = new AssignStmt(varExpInt, constExpInt);
        String assignStmtString = "number=10\n";
        Assert.assertEquals("Testing toString method of AssignStmt", assignStmtString, assignStmt.toString());
    }

    @Test
    public void toStringPrefixTest() throws InvalidVariable{
        Stmt assignStmt = new AssignStmt(varExpInt, constExpInt);
        String expected = "\tnumber=10\n";
        Assert.assertEquals("Testing toStringPrefix method of AssignStmt", expected,
                assignStmt.toStringPrefix("\t"));
    }

}

