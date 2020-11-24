package ToyInterpreter.tests.testModel;

import ToyInterpreter.exceptions.*;
import ToyInterpreter.model.adts.Heap;
import ToyInterpreter.model.adts.IHeap;
import ToyInterpreter.model.adts.ISymTable;
import ToyInterpreter.model.adts.SymTable;
import ToyInterpreter.model.exps.*;
import ToyInterpreter.model.values.*;
import org.junit.*;

public class testArithmeticExp {

    private ISymTable<String, Value> table;
    private IHeap<Integer, Value> heap;
    private Exp varInt;
    private Exp constInt, constTrue;
    private Exp arithmeticAdd, arithmeticSub, arithmeticDiv, arithmeticMultiply, arithmeticComposite;
    private Exp arithmeticThrowsDivisionByZero, arithmeticThrowsInvalidOperator,
            arithmeticThrowsInvalidArithmeticType1,
            arithmeticThrowsInvalidArithmeticType2;

    @BeforeClass
    public static void printName(){
        System.out.println("Testing ArithmeticExp");
    }

    @AfterClass
    public static void printEnd(){
        System.out.println();
    }

    @Before
    public void setUp(){
        table = new SymTable<>();
        heap = new Heap<>();
        table.add("number", new IntValue(5));
        varInt = new VarExp("number");

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
    }

    @SuppressWarnings("DuplicatedCode")
    @After
    public void tearDown(){
        varInt = null;
        constInt = null;
        constTrue = null;
        arithmeticAdd = null;
        arithmeticSub = null;
        arithmeticDiv = null;
        arithmeticMultiply = null;
        arithmeticComposite = null;
        arithmeticThrowsDivisionByZero = null;
        arithmeticThrowsInvalidOperator = null;
        arithmeticThrowsInvalidArithmeticType2 = null;
        arithmeticThrowsInvalidArithmeticType1 = null;
        table.clear();
        table = null;
        heap.clear();
        heap = null;
    }

    @Test
    public void toStringTest(){
        String arithmeticString = "(10 + number)";
        String arithmeticCompositeString = "((10 + number) * (10 - number))";

        Assert.assertEquals("Testing toString method of ArithmeticExp",
                arithmeticString, arithmeticAdd.toString());
        Assert.assertEquals("Testing toString method of composite ArithmeticExp",
                arithmeticCompositeString, arithmeticComposite.toString());
    }

    @Test
    public void equalsTest(){
        Assert.assertEquals("Testing equals method of ArithmeticExp", arithmeticAdd, arithmeticSub);
    }

    @Test
    public void evalTest() throws MyException {
        Assert.assertEquals("Testing eval method of ArithmeticExp with +",
                15, arithmeticAdd.eval(table, heap).getValue());
        Assert.assertEquals("Testing eval method of ArithmeticExp with -",
                5, arithmeticSub.eval(table, heap).getValue());
        Assert.assertEquals("Testing eval method of ArithmeticExp with *",
                50, arithmeticMultiply.eval(table, heap).getValue());
        Assert.assertEquals("Testing eval method of ArithmeticExp with /",
                2, arithmeticDiv.eval(table, heap).getValue());
        Assert.assertEquals("Testing eval method of ArithmeticExp composed of multiple ArithmeticExp objects",
                75, arithmeticComposite.eval(table, heap).getValue());
    }

    @Test(expected = DivisionByZeroException.class)
    public void DivisionByZeroTest() throws MyException {
        Assert.assertNotNull("Testing DivisionByZero exception",
                arithmeticThrowsDivisionByZero.eval(table, heap));
    }

    @Test(expected = InvalidOperator.class)
    public void InvalidOperatorTest() throws MyException{
        Assert.assertNotNull("Testing InvalidOperator exception for ArithmeticExp",
                arithmeticThrowsInvalidOperator.eval(table, heap));
    }

    @Test(expected = InvalidArithmeticTypeException.class)
    public void InvalidArithmeticTypeFirstTest() throws MyException{
        Assert.assertNotNull("Testing InvalidArithmeticType exception for left expression",
                arithmeticThrowsInvalidArithmeticType1.eval(table, heap));
    }

    @Test(expected = InvalidArithmeticTypeException.class)
    public void InvalidArithmeticTypeSecondTest() throws MyException{
        Assert.assertNotNull("Testing InvalidArithmeticType exception for right expression",
                arithmeticThrowsInvalidArithmeticType2.eval(table, heap));
    }

}
