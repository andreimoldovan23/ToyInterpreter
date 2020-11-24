package ToyInterpreter.tests.testModel;

import ToyInterpreter.exceptions.*;
import ToyInterpreter.model.PrgState;
import ToyInterpreter.model.adts.*;
import ToyInterpreter.model.exps.*;
import ToyInterpreter.model.stmts.*;
import ToyInterpreter.model.types.*;
import ToyInterpreter.model.values.*;
import org.junit.*;

import java.io.IOException;

public class testVarDecl {

    private Exp varExpInt;
    private Exp constExpInt;
    private Type intType;
    private PrgState state;

    @BeforeClass
    public static void printName(){
        System.out.println("Testing VarDecl Stmt");
    }

    @AfterClass
    public static void printEnd(){
        System.out.println();
    }

    @Before
    public void setUp(){
        intType = new Int();
        constExpInt = new ConstExp(new IntValue(10));
        varExpInt = new VarExp("number");
        state = new PrgState(new ExeStack<>(), new SymTable<>(), new Out<>(), new FileTable<>(), new Heap<>(),
                new NOP());
    }

    @SuppressWarnings("DuplicatedCode")
    @After
    public void tearDown() throws IOException {
        state.cleanAll();
        intType = null;
        constExpInt = null;
        varExpInt = null;
        state = null;
    }

    @Test
    public void execTest() throws MyException {
        Stmt varDeclInt = new VarDecl(intType, varExpInt);
        varDeclInt.exec(state);
        ISymTable<String, Value> table = state.getTable();
        Assert.assertTrue("Testing exec method of VarDecl", table.isDefined(varExpInt.toString()));
    }

    @Test(expected = InvalidVariable.class)
    public void InvalidVariableTest() throws MyException{
        new VarDecl(intType, constExpInt);
        System.out.println("Testing InvalidVariable exception within VarDecl");
    }

    @Test(expected = IsAlreadyDefined.class)
    public void IsAlreadyDefinedTest() throws MyException {
        Stmt varDeclInt = new VarDecl(intType, varExpInt);
        varDeclInt.exec(state);
        Assert.assertNotNull("Testing IsAlreadyDefined exception within VarDecl",
                varDeclInt.exec(state));
    }

    @Test
    public void toStringTest() throws InvalidVariable {
        Stmt varDecl = new VarDecl(intType, varExpInt);
        String varDeclString = "int number\n";
        Assert.assertEquals("Testing toString method of VarDecl", varDeclString, varDecl.toString());
    }

    @Test
    public void toStringPrefixTest() throws InvalidVariable{
        Stmt varDecl = new VarDecl(intType, varExpInt);
        String expected = "\tint number\n";
        Assert.assertEquals("Testing toStringPrefix method of VarDecl", expected,
                varDecl.toStringPrefix("\t"));
    }

}
