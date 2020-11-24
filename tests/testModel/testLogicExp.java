package ToyInterpreter.tests.testModel;

import ToyInterpreter.exceptions.*;
import ToyInterpreter.model.adts.Heap;
import ToyInterpreter.model.adts.IHeap;
import ToyInterpreter.model.adts.ISymTable;
import ToyInterpreter.model.adts.SymTable;
import ToyInterpreter.model.exps.*;
import ToyInterpreter.model.values.*;
import org.junit.*;

public class testLogicExp {

    private ISymTable<String, Value> table;
    private IHeap<Integer, Value> heap;
    private Exp varBool;
    private Exp constInt;
    private Exp logicAnd, logicOr, logicComposite;
    private Exp logicThrowsInvalidOperator, logicThrowsInvalidLogicType1, logicThrowsInvalidLogicType2;

    @BeforeClass
    public static void printName(){
        System.out.println("Testing LogicExp");
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
        table.add("boolean", new True());

        varBool = new VarExp("boolean");

        constInt = new ConstExp(new IntValue(10));
        Exp constFalse = new ConstExp(new False());

        logicAnd = new LogicExp(varBool, constFalse, "&");
        logicOr = new LogicExp(varBool, constFalse, "|");
        logicComposite = new LogicExp(logicAnd, logicOr, "&");
        logicThrowsInvalidOperator = new LogicExp(varBool, constFalse, "+");
        logicThrowsInvalidLogicType1 = new LogicExp(constInt, varBool, "&");
        logicThrowsInvalidLogicType2 = new LogicExp(varBool, constInt, "&");
    }

    @SuppressWarnings("DuplicatedCode")
    @After
    public void tearDown(){
        varBool = null;
        constInt = null;
        logicAnd = null;
        logicOr = null;
        logicComposite = null;
        logicThrowsInvalidOperator = null;
        logicThrowsInvalidLogicType1 = null;
        logicThrowsInvalidLogicType2 = null;
        table.clear();
        table = null;
        heap.clear();
        heap = null;
    }

    @Test
    public void toStringTest(){
        String logicString = "(boolean & false)";
        String logicCompositeString = "((boolean & false) & (boolean | false))";

        Assert.assertEquals("Testing toString method of LogicExp", logicString, logicAnd.toString());
        Assert.assertEquals("Testing toString method of composite LogicExp",
                logicCompositeString, logicComposite.toString());

    }

    @Test
    public void equalsTest(){
        Assert.assertEquals("Testing equals method of LogicExp", logicAnd, logicOr);
    }

    @Test
    public void evalTest() throws MyException {
        Assert.assertEquals("Testing eval method of LogicExp with &",
                false, logicAnd.eval(table, heap).getValue());
        Assert.assertEquals("Testing eval method of LogicExp with |",
                true, logicOr.eval(table, heap).getValue());
        Assert.assertEquals("Testing eval method of LogicExp composed of multiple LogicExp objects",
                false, logicComposite.eval(table, heap).getValue());
    }

    @Test(expected = InvalidOperator.class)
    public void LogicInvalidOperatorTest() throws MyException{
        Assert.assertNotNull("Testing InvalidOperator exception for LogicExp",
                logicThrowsInvalidOperator.eval(table, heap));
    }

    @Test(expected = InvalidLogicTypeException.class)
    public void InvalidLogicTypeFirstTest() throws MyException{
        Assert.assertNotNull("Testing InvalidLogicType exception for left expression",
                logicThrowsInvalidLogicType1.eval(table, heap));
    }

    @Test(expected = InvalidLogicTypeException.class)
    public void InvalidLogicTypeSecondTest() throws MyException{
        Assert.assertNotNull("Testing InvalidLogicType exception for right expression",
                logicThrowsInvalidLogicType2.eval(table, heap));
    }

}
