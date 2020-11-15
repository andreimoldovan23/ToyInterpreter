package ToyInterpreter.tests.testModel;

import ToyInterpreter.model.types.*;
import ToyInterpreter.model.values.*;
import org.junit.*;

public class testValues {

    private Value trueVal, falseVal, intVal, boolVal;
    private int val;
    private boolean flag;
    private Type intType, boolType;

    @BeforeClass
    public static void printName(){
        System.out.println("Testing Value classes");
    }

    @AfterClass
    public static void printEnd(){
        System.out.println();
    }

    @Before
    public void setUp(){
        trueVal = new True();
        falseVal = new False();

        val = 10;
        flag = true;

        intType = new Int();
        boolType = new Bool();

        intVal = new IntValue(val);
        boolVal = new BoolValue(flag);
    }

    @Test
    public void toStringTest(){
        Assert.assertEquals("Testing toString method of True", trueVal.toString(), "true");
        Assert.assertEquals("Testing toString method of False", falseVal.toString(), "false");
        Assert.assertEquals("Testing toString method of IntValue", intVal.toString(), Integer.toString(val));
        Assert.assertEquals("Testing toString method of BoolValue", boolVal.toString(), Boolean.toString(flag));
    }

    @Test
    public void getTypeTest(){
        Assert.assertEquals("Testing getType method of True", trueVal.getType(), boolType);
        Assert.assertEquals("Testing getType method of False", falseVal.getType(), boolType);
        Assert.assertEquals("Testing getType method of BoolValue", boolVal.getType(), boolType);
        Assert.assertEquals("Testing getType method of IntValue", intVal.getType(), intType);
    }

    @Test
    public void getValueTest(){
        Assert.assertEquals("Testing getValue method of True", trueVal.getValue(), true);
        Assert.assertEquals("Testing getValue method of False", falseVal.getValue(), false);
        Assert.assertEquals("Testing getValue method of BoolValue", boolVal.getValue(), flag);
        Assert.assertEquals("Testing getValue method of IntValue", intVal.getValue(), val);
    }

}
