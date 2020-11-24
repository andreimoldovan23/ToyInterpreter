package ToyInterpreter.tests.testModel;

import ToyInterpreter.exceptions.*;
import ToyInterpreter.model.adts.Heap;
import ToyInterpreter.model.adts.IHeap;
import ToyInterpreter.model.adts.ISymTable;
import ToyInterpreter.model.adts.SymTable;
import ToyInterpreter.model.exps.*;
import ToyInterpreter.model.values.*;
import org.junit.*;

public class testRelationalExp {

    private ISymTable<String, Value> table;
    private IHeap<Integer, Value> heap;
    private Exp varInt;
    private Exp constInt, constTrue;
    private Exp relationalSmaller, relationalGreater, relationalEqual,
            relationalSmallerEqual, relationalGreaterEqual, relationalComposite;
    private Exp relationalThrowsInvalidOperator,
            relationalThrowsInvalidRelationalType1,
            relationalThrowsInvalidRelationalType2;

    @BeforeClass
    public static void printName(){
        System.out.println("Testing RelationalExp");
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
        constTrue = new ConstExp(new True());

        relationalSmaller = new RelationalExp(varInt, constInt, "<");
        relationalGreater = new RelationalExp(constInt, varInt, ">");
        relationalEqual = new RelationalExp(varInt, new ConstExp(new IntValue(5)), "==");
        relationalSmallerEqual = new RelationalExp(varInt, varInt, "<=");
        relationalGreaterEqual = new RelationalExp(constInt, constInt, ">=");
        relationalComposite = new RelationalExp(new ArithmeticExp(constInt, varInt, "+"),
                new ArithmeticExp(constInt, varInt, "-"), "<");
        relationalThrowsInvalidOperator = new RelationalExp(varInt, constInt, "+");
        relationalThrowsInvalidRelationalType2 = new RelationalExp(varInt, constTrue, "==");
        relationalThrowsInvalidRelationalType1 = new RelationalExp(constTrue, varInt, "==");
    }

    @SuppressWarnings("DuplicatedCode")
    @After
    public void tearDown(){
        varInt = null;
        constInt = null;
        constTrue = null;
        relationalSmaller = null;
        relationalSmallerEqual = null;
        relationalGreater = null;
        relationalGreaterEqual = null;
        relationalEqual = null;
        relationalThrowsInvalidOperator = null;
        relationalThrowsInvalidRelationalType1 = null;
        relationalThrowsInvalidRelationalType2 = null;
        table.clear();
        table = null;
        heap.clear();
        heap = null;
    }

    @Test
    public void toStringTest(){
        String relationalString = "(number < 10)";
        String relationalCompositeString = "((10 + number) < (10 - number))";

        Assert.assertEquals("Testing toString method of RelationalExp",
                relationalString, relationalSmaller.toString());
        Assert.assertEquals("Testing toString method of composite RelationalExp",
                relationalCompositeString, relationalComposite.toString());
    }

    @Test
    public void equalsTest(){
        Assert.assertEquals("Testing equals method of RelationalExp", relationalGreater, relationalSmaller);
    }

    @Test
    public void evalTest() throws MyException {
        Assert.assertEquals("Testing eval method of RelationalExp with <",
                true, relationalSmaller.eval(table, heap).getValue());
        Assert.assertEquals("Testing eval method of RelationalExp with >",
                true, relationalGreater.eval(table, heap).getValue());
        Assert.assertEquals("Testing eval method of RelationalExp with ==",
                true, relationalEqual.eval(table, heap).getValue());
        Assert.assertEquals("Testing eval method of RelationalExp with <=",
                true, relationalSmallerEqual.eval(table, heap).getValue());
        Assert.assertEquals("Testing eval method of RelationalExp with >=",
                true, relationalGreaterEqual.eval(table, heap).getValue());
        Assert.assertEquals("Testing eval method of RelationalExp composed of multiple Exp objects",
                false, relationalComposite.eval(table, heap).getValue());
    }

    @Test(expected = InvalidOperator.class)
    public void InvalidOperatorTest() throws MyException{
        Assert.assertNotNull("Testing InvalidOperator exception for RelationalExp",
                relationalThrowsInvalidOperator.eval(table, heap));
    }

    @Test(expected = InvalidRelationalType.class)
    public void InvalidRelationalTypeFirstTest() throws MyException{
        Assert.assertNotNull("Testing InvalidRelationalType exception for left expression",
                relationalThrowsInvalidRelationalType1.eval(table, heap));
    }

    @Test(expected = InvalidRelationalType.class)
    public void InvalidRelationalTypeSecondTest() throws MyException{
        Assert.assertNotNull("Testing InvalidRelationalType exception for right expression",
                relationalThrowsInvalidRelationalType2.eval(table, heap));
    }

}
