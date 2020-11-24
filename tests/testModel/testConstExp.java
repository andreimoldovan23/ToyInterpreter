package ToyInterpreter.tests.testModel;

import ToyInterpreter.exceptions.*;
import ToyInterpreter.model.adts.Heap;
import ToyInterpreter.model.adts.IHeap;
import ToyInterpreter.model.adts.ISymTable;
import ToyInterpreter.model.adts.SymTable;
import ToyInterpreter.model.exps.*;
import ToyInterpreter.model.values.*;
import org.junit.*;

public class testConstExp {

    private ISymTable<String, Value> table;
    private IHeap<Integer, Value> heap;
    private Exp constInt, constTrue;

    @BeforeClass
    public static void printName(){
        System.out.println("Testing ConstExp");
    }

    @AfterClass
    public static void printEnd(){
        System.out.println();
    }

    @Before
    public void setUp(){
        table = new SymTable<>();
        heap = new Heap<>();
        constTrue = new ConstExp(new True());
        constInt = new ConstExp(new IntValue(10));
    }

    @SuppressWarnings("DuplicatedCode")
    @After
    public void tearDown(){
        constInt = null;
        constTrue = null;
        table.clear();
        table = null;
        heap.clear();
        heap = null;
    }

    @Test
    public void toStringTest(){
        String constString = "10";
        Assert.assertEquals("Testing toString method of ConstExp", constString, constInt.toString());
    }

    @Test
    public void equalsTest(){
        Assert.assertEquals("Testing equals method of ConstExp", constInt, constTrue);
    }

    @Test
    public void evalTest() throws MyException {
        Assert.assertEquals("Testing eval method of ConstExp", 10, constInt.eval(table, heap).getValue());
    }

}
