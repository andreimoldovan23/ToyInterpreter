package ToyInterpreter.tests.testModel;

import ToyInterpreter.model.types.*;
import ToyInterpreter.model.values.*;
import org.junit.*;

public class testValues {

    private Value trueVal, falseVal, intVal, boolVal, stringVal;
    private int val;
    private boolean flag;
    private String string;
    private Type intType, boolType, stringType;

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

        string = "Hello world";
        val = 10;
        flag = true;

        stringType = new StringType();
        intType = new Int();
        boolType = new Bool();

        stringVal = new StringValue(string);
        intVal = new IntValue(val);
        boolVal = new BoolValue(flag);
    }

    @After
    public void tearDown(){
        trueVal = null;
        falseVal = null;
        stringType = null;
        intType = null;
        boolType = null;
        stringVal = null;
        intVal = null;
        boolVal = null;
    }

    @Test
    public void toStringTest(){
        Assert.assertEquals("Testing toString method of True", trueVal.toString(), "true");
        Assert.assertEquals("Testing toString method of False", falseVal.toString(), "false");
        Assert.assertEquals("Testing toString method of IntValue", intVal.toString(), Integer.toString(val));
        Assert.assertEquals("Testing toString method of BoolValue", boolVal.toString(), Boolean.toString(flag));
        Assert.assertEquals("Testing toString method of StringValue", stringVal.toString(), string);
    }

    @Test
    public void getTypeTest(){
        Assert.assertEquals("Testing getType method of True", trueVal.getType(), boolType);
        Assert.assertEquals("Testing getType method of False", falseVal.getType(), boolType);
        Assert.assertEquals("Testing getType method of BoolValue", boolVal.getType(), boolType);
        Assert.assertEquals("Testing getType method of IntValue", intVal.getType(), intType);
        Assert.assertEquals("Testing getType method of StringValue", stringVal.getType(), stringType);
    }

    @Test
    public void getValueTest(){
        Assert.assertEquals("Testing getValue method of True", trueVal.getValue(), true);
        Assert.assertEquals("Testing getValue method of False", falseVal.getValue(), false);
        Assert.assertEquals("Testing getValue method of BoolValue", boolVal.getValue(), flag);
        Assert.assertEquals("Testing getValue method of IntValue", intVal.getValue(), val);
        Assert.assertEquals("Testing getValue method of StringValue", stringVal.getValue(), string);
    }

    @Test
    public void equalsTest(){
        Assert.assertEquals("Testing equals method of True", trueVal, new True());
        Assert.assertEquals("Testing equals method of False", falseVal, new False());
        Assert.assertEquals("Testing equals method of BoolValue", boolVal, boolVal);
        Assert.assertEquals("Testing equals method of IntValue", intVal, new IntValue(val));
        Assert.assertEquals("Testing equals method of StringValue", stringVal, new StringValue(""));
    }

    @Test
    public void hashCodeTest(){
        int sum = 0;
        String s = "Hello world";
        for(int i = 0; i < s.length(); i++)
            sum += s.charAt(i);
        Assert.assertEquals("Testing hashCode method of StringValue", stringVal.hashCode(),
                sum);
    }

}
