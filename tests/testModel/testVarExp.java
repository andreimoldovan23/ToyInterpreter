package ToyInterpreter.tests.testModel;

import ToyInterpreter.exceptions.*;
import ToyInterpreter.model.adts.Heap;
import ToyInterpreter.model.adts.IHeap;
import ToyInterpreter.model.adts.ISymTable;
import ToyInterpreter.model.adts.SymTable;
import ToyInterpreter.model.exps.*;
import ToyInterpreter.model.values.*;
import org.junit.*;

public class testVarExp {

    private ISymTable<String, Value> table;
    private IHeap<Integer, Value> heap;
    private Exp varInt, varBool, varThrowsIsNotDefined;

    @BeforeClass
    public static void printName(){
        System.out.println("Testing VarExp");
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

        varInt = new VarExp("number");
        varBool = new VarExp("boolean");
        varThrowsIsNotDefined = new VarExp("a");
    }

    @SuppressWarnings("DuplicatedCode")
    @After
    public void tearDown(){
        varInt = null;
        varBool = null;
        varThrowsIsNotDefined = null;
        table.clear();
        table = null;
        heap.clear();
        heap = null;
    }

    @Test
    public void toStringTest(){
        String varIntString = "number";
        Assert.assertEquals("Testing toString method of VarExp", varIntString, varInt.toString());
    }

    @Test
    public void equalsTest(){
        Assert.assertEquals("Testing equals method of VarExp", varInt, varBool);
    }

    @Test
    public void evalTest() throws MyException {
        Assert.assertEquals("Testing eval method of VarExp with int", 5,
                varInt.eval(table, heap).getValue());
        Assert.assertEquals("Testing eval method of VarExp with bool",
                true, varBool.eval(table, heap).getValue());
    }

    @Test(expected = IsNotDefinedException.class)
    public void IsNotDefinedTest() throws MyException{
        Assert.assertNotNull("Testing IsNotDefined exception", varThrowsIsNotDefined.eval(table, heap));
    }

}
