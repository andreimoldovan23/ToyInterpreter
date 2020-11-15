package tests.testModel;

import exceptions.*;
import model.adts.ISymTable;
import model.adts.SymTable;
import model.exps.*;
import model.values.*;
import org.junit.*;

public class testExps {

    private ISymTable<String, Value> table;
    private Exp varInt, varBool, varThrowsIsNotDefined;
    private Exp constInt, constTrue;
    private Exp arithmeticAdd, arithmeticSub, arithmeticDiv, arithmeticMultiply, arithmeticComposite;
    private Exp arithmeticThrowsDivisionByZero, arithmeticThrowsInvalidOperator, arithmeticThrowsInvalidArithmeticType1,
            arithmeticThrowsInvalidArithmeticType2;
    private Exp logicAnd, logicOr, logicComposite;
    private Exp logicThrowsInvalidOperator, logicThrowsInvalidLogicType1, logicThrowsInvalidLogicType2;

    @BeforeClass
    public static void printName(){
        System.out.println("Testing Exp classes");
    }

    @AfterClass
    public static void printEnd(){
        System.out.println();
    }

    @Before
    public void setUp(){
        table = new SymTable<>();
        table.add("number", new IntValue(5));
        table.add("boolean", new True());

        varInt = new VarExp("number");
        varBool = new VarExp("boolean");
        varThrowsIsNotDefined = new VarExp("a");

        constInt = new ConstExp(new IntValue(10));
        Exp constZero = new ConstExp(new IntValue(0));
        constTrue = new ConstExp(new True());
        Exp constFalse = new ConstExp(new False());

        arithmeticAdd = new ArithmeticExp(constInt, varInt, "+");
        arithmeticSub = new ArithmeticExp(constInt, varInt, "-");
        arithmeticDiv = new ArithmeticExp(constInt, varInt, "/");
        arithmeticMultiply = new ArithmeticExp(constInt, varInt, "*");
        arithmeticComposite = new ArithmeticExp(arithmeticAdd, arithmeticSub, "*");
        arithmeticThrowsDivisionByZero = new ArithmeticExp(varInt, constZero, "/");
        arithmeticThrowsInvalidOperator = new ArithmeticExp(constInt, constInt, "!");
        arithmeticThrowsInvalidArithmeticType2 = new ArithmeticExp(constInt, constTrue, "+");
        arithmeticThrowsInvalidArithmeticType1 = new ArithmeticExp(constFalse, constInt, "+");

        logicAnd = new LogicExp(varBool, constFalse, "&");
        logicOr = new LogicExp(varBool, constFalse, "|");
        logicComposite = new LogicExp(logicAnd, logicOr, "&");
        logicThrowsInvalidOperator = new LogicExp(varBool, constFalse, "+");
        logicThrowsInvalidLogicType1 = new LogicExp(constInt, varBool, "&");
        logicThrowsInvalidLogicType2 = new LogicExp(varBool, constInt, "&");
    }

    @Test
    public void toStringTest(){
        String varIntString = "number";
        String constString = "10";
        String arithmeticString = "(10 + number)";
        String arithmeticCompositeString = "((10 + number) * (10 - number))";
        String logicString = "(boolean & false)";
        String logicCompositeString = "((boolean & false) & (boolean | false))";

        Assert.assertEquals("Testing toString method of VarExp", varIntString, varInt.toString());
        Assert.assertEquals("Testing toString method of ConstExp", constString, constInt.toString());
        Assert.assertEquals("Testing toString method of ArithmeticExp", arithmeticString, arithmeticAdd.toString());
        Assert.assertEquals("Testing toString method of composite ArithmeticExp",
                arithmeticCompositeString, arithmeticComposite.toString());
        Assert.assertEquals("Testing toString method of LogicExp", logicString, logicAnd.toString());
        Assert.assertEquals("Testing toString method of composite LogicExp",
                logicCompositeString, logicComposite.toString());

    }

    @Test
    public void equalsTest(){
        Assert.assertEquals("Testing equals method of ConstExp", constInt, constTrue);
        Assert.assertEquals("Testing equals method of VarExp", varInt, varBool);
        Assert.assertEquals("Testing equals method of LogicExp", logicAnd, logicOr);
        Assert.assertEquals("Testing equals method of ArithmeticExp", arithmeticAdd, arithmeticSub);
    }

    @Test
    public void evalTest() throws MyException {
        Assert.assertEquals("Testing eval method of ConstExp", 10, constInt.eval(table).getValue());
        Assert.assertEquals("Testing eval method of VarExp with int", 5, varInt.eval(table).getValue());
        Assert.assertEquals("Testing eval method of VarExp with bool", true, varBool.eval(table).getValue());
        Assert.assertEquals("Testing eval method of ArithmeticExp with +",
                15, arithmeticAdd.eval(table).getValue());
        Assert.assertEquals("Testing eval method of ArithmeticExp with -",
                5, arithmeticSub.eval(table).getValue());
        Assert.assertEquals("Testing eval method of ArithmeticExp with *",
                50, arithmeticMultiply.eval(table).getValue());
        Assert.assertEquals("Testing eval method of ArithmeticExp with /",
                2, arithmeticDiv.eval(table).getValue());
        Assert.assertEquals("Testing eval method of ArithmeticExp composed of multiple ArithmeticExp objects",
                75, arithmeticComposite.eval(table).getValue());
        Assert.assertEquals("Testing eval method of LogicExp with &",
                false, logicAnd.eval(table).getValue());
        Assert.assertEquals("Testing eval method of LogicExp with |",
                true, logicOr.eval(table).getValue());
        Assert.assertEquals("Testing eval method of LogicExp composed of multiple LogicExp objects",
                false, logicComposite.eval(table).getValue());
    }

    @Test(expected = DivisionByZeroException.class)
    public void DivisionByZeroTest() throws MyException {
        Assert.assertNotNull("Testing DivisionByZero exception", arithmeticThrowsDivisionByZero.eval(table));
    }

    @Test(expected = InvalidOperator.class)
    public void ArithmeticInvalidOperatorTest() throws MyException{
        Assert.assertNotNull("Testing InvalidOperator exception for ArithmeticExp",
                arithmeticThrowsInvalidOperator.eval(table));
    }

    @Test(expected = InvalidOperator.class)
    public void LogicInvalidOperatorTest() throws MyException{
        Assert.assertNotNull("Testing InvalidOperator exception for LogicExp",
                logicThrowsInvalidOperator.eval(table));
    }

    @Test(expected = InvalidArithmeticTypeException.class)
    public void InvalidArithmeticTypeFirstTest() throws MyException{
        Assert.assertNotNull("Testing InvalidArithmeticType exception for left expression",
                arithmeticThrowsInvalidArithmeticType1.eval(table));
    }

    @Test(expected = InvalidArithmeticTypeException.class)
    public void InvalidArithmeticTypeSecondTest() throws MyException{
        Assert.assertNotNull("Testing InvalidArithmeticType exception for right expression",
                arithmeticThrowsInvalidArithmeticType2.eval(table));
    }

    @Test(expected = InvalidLogicTypeException.class)
    public void InvalidLogicTypeFirstTest() throws MyException{
        Assert.assertNotNull("Testing InvalidLogicType exception for left expression",
                logicThrowsInvalidLogicType1.eval(table));
    }

    @Test(expected = InvalidLogicTypeException.class)
    public void InvalidLogicTypeSecondTest() throws MyException{
        Assert.assertNotNull("Testing InvalidLogicType exception for right expression",
                logicThrowsInvalidLogicType2.eval(table));
    }

    @Test(expected = IsNotDefinedException.class)
    public void IsNotDefinedTest() throws MyException{
        Assert.assertNotNull("Testing IsNotDefined exception", varThrowsIsNotDefined.eval(table));
    }

}
