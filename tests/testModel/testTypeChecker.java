package ToyInterpreter.tests.testModel;

import ToyInterpreter.exceptions.*;
import ToyInterpreter.model.adts.ITypeEnv;
import ToyInterpreter.model.adts.TypeEnv;
import ToyInterpreter.model.exps.*;
import ToyInterpreter.model.stmts.*;
import ToyInterpreter.model.types.*;
import ToyInterpreter.model.values.*;
import org.junit.*;

public class testTypeChecker {

    private ITypeEnv<String, Type> typeEnv;

    @BeforeClass
    public static void printName() {
        System.out.println("Testing TypeChecker");
    }

    @AfterClass
    public static void printEnd() {
        System.out.println();
    }

    @Before
    public void setUp() {
        typeEnv = new TypeEnv<>();
    }

    @After
    public void tearDown() {
        typeEnv.clear();
        typeEnv = null;
    }

    @Test(expected = IsNotDefinedException.class)
    public void varExpTest() throws MyException {
        typeEnv.add("var", new Int());
        Exp var = new VarExp("var");
        Exp number = new VarExp("number");
        Assert.assertEquals("Testing typeCheck for VarExp", new Int(), var.typeCheck(typeEnv));
        Assert.assertNull("Testing IsNotDefined exception for VarExp typeCheck", number.typeCheck(typeEnv));
    }

    @Test
    public void constExpTest() throws MyException {
        Exp constInt = new ConstExp(Int.defaultValue());
        Exp constBool = new ConstExp(Bool.defaultValue());
        Exp constString = new ConstExp(StringType.defaultValue());
        Exp constRef = new ConstExp(Ref.defaultValue(new Int()));

        Assert.assertEquals("Testing typeCheck for ConstExp", new Int(), constInt.typeCheck(typeEnv));
        Assert.assertEquals(new Bool(), constBool.typeCheck(typeEnv));
        Assert.assertEquals(new StringType(), constString.typeCheck(typeEnv));
        Assert.assertEquals(new Ref(new Int()), constRef.typeCheck(typeEnv));
    }

    @Test(expected = InvalidArithmeticTypeException.class)
    public void arithmeticExpTest() throws MyException {
        Exp var = new VarExp("var");
        Exp constInt = new ConstExp(new IntValue(10));
        Exp arithmeticVar = new ArithmeticExp(var, constInt, "+");
        Exp arithmeticConst = new ArithmeticExp(constInt, constInt, "*");
        Exp arithmeticComp = new ArithmeticExp(arithmeticVar, arithmeticConst, "-");
        Exp constString = new ConstExp(new StringValue("Hello"));

        typeEnv.add("var", new Int());
        Assert.assertEquals("Testing typeCheck for ArithmeticExp", new Int(),
                arithmeticComp.typeCheck(typeEnv));
        Assert.assertNull("Testing InvalidArithmeticTypeException for ArithmeticExp typeCheck",
                new ArithmeticExp(var, constString, "*").typeCheck(typeEnv));
    }

    @Test(expected = InvalidLogicTypeException.class)
    public void logicExpTest() throws MyException {
        Exp var = new VarExp("var");
        Exp constBool = new ConstExp(new True());
        Exp logicVar = new LogicExp(var, constBool, "&");
        Exp logicConst = new LogicExp(constBool, constBool, "|");
        Exp logicComp = new LogicExp(logicVar, logicConst, "&");
        Exp constString = new ConstExp(new StringValue("Hello"));

        typeEnv.add("var", new Bool());
        Assert.assertEquals("Testing typeCheck for LogicExp", new Bool(),
                logicComp.typeCheck(typeEnv));
        Assert.assertNull("Testing InvalidLogicTypeException for LogicExp typeCheck",
                new LogicExp(var, constString, "&").typeCheck(typeEnv));
    }

    @Test(expected = InvalidRelationalType.class)
    public void relationalExpTest() throws MyException {
        Exp var = new VarExp("var");
        Exp constInt = new ConstExp(new IntValue(10));
        Exp relationalVar = new RelationalExp(var, constInt, "<");
        Exp relationalConst = new RelationalExp(constInt, constInt, "==");
        Exp relationalComp = new RelationalExp(relationalVar, relationalConst, ">=");
        Exp constString = new ConstExp(new StringValue("Hello"));

        typeEnv.add("var", new Int());
        Assert.assertEquals("Testing typeCheck for RelationalExp", new Bool(),
                relationalComp.typeCheck(typeEnv));
        Assert.assertNull("Testing InvalidRelationalType exception for RelationalExp typeCheck",
                new RelationalExp(var, constString, "<=").typeCheck(typeEnv));
    }

    @Test(expected = InvalidReadHeapType.class)
    public void readHeapTest() throws MyException {
        Exp var = new VarExp("var");
        Exp readValid = new ReadHeapExp(var);
        Exp readInvalid = new ReadHeapExp(new ConstExp(new IntValue(1)));

        typeEnv.add("var", new Ref(new Int()));
        Assert.assertEquals("Testing typeCheck for ReadHeapExp", new Int(), readValid.typeCheck(typeEnv));
        Assert.assertNull("Testing InvalidReadHeapType exception for ReadHeapExp typeCheck",
                readInvalid.typeCheck(typeEnv));
    }

    @Test(expected = InvalidAssignTypeException.class)
    public void assignStmtTest() throws MyException {
        Exp left = new VarExp("var");
        Exp right = new ConstExp(new IntValue(10));
        Exp rightInvalid = new ConstExp(new StringValue("Hello"));
        Stmt assignValid = new AssignStmt(left, right);
        Stmt assignInvalid = new AssignStmt(left, rightInvalid);

        typeEnv.add("var", new Int());
        Assert.assertNotNull("Testing typeCheck for AssignStmt", assignValid.typeCheck(typeEnv));
        Assert.assertNotNull("Testing InvalidAssignType exception for AssignStmt typeCheck",
                assignInvalid.typeCheck(typeEnv));
    }

    @Test(expected = InvalidFilenameException.class)
    public void closeFileStmtTest() throws MyException {
        Exp file = new VarExp("myFile");
        Exp constExp = new ConstExp(new IntValue(10));
        Stmt valid = new CloseFileStmt(file);
        Stmt invalid = new CloseFileStmt(constExp);

        typeEnv.add("myFile", new StringType());
        Assert.assertNotNull("Testing typeCheck for CloseFileStmt", valid.typeCheck(typeEnv));
        Assert.assertNotNull("Testing InvalidFilenameException for CloseFileStmt typeCheck",
                invalid.typeCheck(typeEnv));
    }

    @Test(expected = IsNotDefinedException.class)
    public void newStmtIsNotDefinedTest() throws MyException {
        String var = "myVar";
        Exp exp = new ConstExp(new IntValue(10));
        Stmt stmt = new NewStmt(var, exp);
        Assert.assertNotNull("Testing IsNotDefined exception for NewStmt typeCheck", stmt.typeCheck(typeEnv));
    }

    @Test(expected = InvalidAllocation.class)
    public void newStmtInvalidAllocationTest() throws MyException {
        String var = "myVar";
        Exp exp = new ConstExp(new IntValue(10));
        typeEnv.add(var, new Int());
        Stmt stmt = new NewStmt(var, exp);
        Assert.assertNotNull("Testing InvalidAllocation exception for NewStmt typeCheck", stmt.typeCheck(typeEnv));
    }

    @Test(expected = InvalidAllocationType.class)
    public void newStmtTest() throws MyException {
        String var = "myVar";
        Exp exp = new ConstExp(new IntValue(10));
        Exp invalidExp = new ConstExp(new StringValue("Hello"));
        Stmt stmt = new NewStmt(var, exp);
        Stmt invalidStmt = new NewStmt(var, invalidExp);
        typeEnv.add(var, new Ref(new Int()));
        Assert.assertNotNull("Testing typeCheck for NewStmt", stmt.typeCheck(typeEnv));
        Assert.assertNotNull("Testing InvalidAllocationType for NewStmt typeCheck", invalidStmt.typeCheck(typeEnv));
    }

    @Test(expected = InvalidFilenameException.class)
    public void openFileStmtTest() throws MyException {
        Exp exp = new ConstExp(new StringValue("myFile"));
        Stmt validStmt = new OpenFileStmt(exp);
        Stmt invalidStmt = new OpenFileStmt(new ConstExp(new IntValue(10)));
        Assert.assertNotNull("Testing typeCheck for OpenFileStmt", validStmt.typeCheck(typeEnv));
        Assert.assertNotNull("Testing InvalidFilenameException for OpenFileStmt typeCheck",
                invalidStmt.typeCheck(typeEnv));
    }

    @Test(expected = IsNotDefinedException.class)
    public void readFileStmtIsNotDefinedTest() throws MyException {
        String var = "myVar";
        Exp exp = new ConstExp(new StringValue("myFile"));
        Stmt read = new ReadFileStmt(exp, var);
        Assert.assertNotNull("Testing IsNotDefinedException for ReadFileStmt typeCheck",
                read.typeCheck(typeEnv));
    }

    @Test(expected = InvalidFileReadType.class)
    public void readFileStmtInvalidFileReadTypeTest() throws MyException {
        String var = "myVar";
        Exp exp = new ConstExp(new StringValue("myFile"));
        typeEnv.add(var, new Bool());
        Stmt read = new ReadFileStmt(exp, var);
        Assert.assertNotNull("Testing InvalidFileReadType for ReadFileStmt typeCheck",
                read.typeCheck(typeEnv));
    }

    @Test(expected = InvalidFilenameException.class)
    public void readFileStmtTest() throws MyException {
        String var = "myVar";
        Exp exp = new ConstExp(new StringValue("myFile"));
        Exp invalidExp = new ConstExp(new IntValue(10));
        Stmt valid = new ReadFileStmt(exp, var);
        Stmt invalid = new ReadFileStmt(invalidExp, var);
        typeEnv.add(var, new Int());
        Assert.assertNotNull("Testing typeCheck for ReadFileStmt", valid.typeCheck(typeEnv));
        Assert.assertNotNull("Testing InvalidFilenameException for ReadFileStmt typeCheck",
                invalid.typeCheck(typeEnv));
    }

    @Test(expected = IsNotDefinedException.class)
    public void writeHeapStmtIsNotDefinedTest() throws MyException {
        String var = "myVar";
        Exp exp = new ConstExp(new IntValue(10));
        Stmt stmt = new WriteHeapStmt(var, exp);
        Assert.assertNotNull("Testing IsNotDefined exception for WriteHeapStmt typeCheck",
                stmt.typeCheck(typeEnv));
    }

    @Test(expected = InvalidAllocation.class)
    public void writeHeapStmtInvalidAllocationTest() throws MyException {
        String var = "myVar";
        Exp exp = new ConstExp(new IntValue(10));
        typeEnv.add(var, new Int());
        Stmt stmt = new WriteHeapStmt(var, exp);
        Assert.assertNotNull("Testing InvalidAllocation exception for WriteHeapStmt typeCheck",
                stmt.typeCheck(typeEnv));
    }

    @Test(expected = InvalidAllocationType.class)
    public void writeHeapStmtTest() throws MyException {
        String var = "myVar";
        Exp exp = new ConstExp(new IntValue(10));
        Exp invalidExp = new ConstExp(new StringValue("Hello"));
        typeEnv.add(var, new Ref(new Int()));
        Stmt stmt = new WriteHeapStmt(var, exp);
        Stmt invalidStmt = new WriteHeapStmt(var, invalidExp);
        Assert.assertNotNull("Testing typeCheck for WriteHeapStmt",
                stmt.typeCheck(typeEnv));
        Assert.assertNotNull("Testing InvalidAllocationType exception for WriteHeapStmt typeCheck",
                invalidStmt.typeCheck(typeEnv));
    }

    @Test
    public void varDeclTest() throws MyException {
        Exp var = new VarExp("myVar");
        Stmt stmt = new VarDecl(new Int(), var);
        Assert.assertEquals("Testing typeCheck for VarDecl", new Int(), stmt.typeCheck(typeEnv).lookup("myVar"));
    }

    @Test(expected = InvalidIfCondition.class)
    public void ifStmtTest() throws MyException {
        String var = "myVar";
        typeEnv.add(var, new Int());
        Exp validExp = new RelationalExp(new VarExp(var), new ConstExp(new IntValue(5)), "<");
        Stmt print = new PrintStmt(new ConstExp(new StringValue("Hello world")));
        Stmt ifStmt = new IfStmt(validExp, print, new NOP());
        Stmt invalidIf = new IfStmt(new ConstExp(new IntValue(5)), print, new NOP());
        Assert.assertNotNull("Testing typeCheck for IfStmt", ifStmt.typeCheck(typeEnv));
        Assert.assertNotNull("Testing InvalidIfCondition exception for IfStmt typeCheck",
                invalidIf.typeCheck(typeEnv));
    }

    @Test(expected = InvalidWhileTypeException.class)
    public void whileStmtTest() throws MyException {
        String var = "myVar";
        typeEnv.add(var, new Int());
        Exp validExp = new RelationalExp(new VarExp(var), new ConstExp(new IntValue(5)), "<");
        Exp invalidExp = new ArithmeticExp(new VarExp(var), new VarExp(var), "+");
        Stmt print = new PrintStmt(new VarExp(var));
        Stmt dec = new AssignStmt(new VarExp(var),
                new ArithmeticExp(new VarExp(var), new ConstExp(new IntValue(1)), "-"));
        Stmt comp = new CompStmt(print, dec);
        Stmt whileStmt = new WhileStmt(validExp, comp);
        Stmt invalidWhile = new WhileStmt(invalidExp, comp);
        Assert.assertNotNull("Testing typeCheck for WhileStmt", whileStmt.typeCheck(typeEnv));
        Assert.assertNotNull("Testing InvalidWhileTypeException for WhileStmt typeCheck",
                invalidWhile.typeCheck(typeEnv));
    }

    @Test(expected = IsNotDefinedException.class)
    public void forkStmtTest() throws MyException {
        String myVar = "myVar";
        String var = "var";
        typeEnv.add("var", new Int());
        Stmt invalidAssign = new AssignStmt(new VarExp(myVar), new ConstExp(new IntValue(10)));
        Stmt invalidFork = new ForkStmt(invalidAssign);
        Stmt assign = new AssignStmt(new VarExp(var), new ConstExp(new IntValue(10)));
        Stmt fork = new ForkStmt(assign);
        Assert.assertNotNull("Testing valid fork Stmt typeCheck", fork.typeCheck(typeEnv));
        Assert.assertNotNull("Testing invalid fork Stmt typeCheck", invalidFork.typeCheck(typeEnv));
    }

}
