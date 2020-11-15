package ToyInterpreter.tests.testModel;

import ToyInterpreter.model.types.*;
import org.junit.*;

public class testTypes {

    private Type intType, boolType;

    @BeforeClass
    public static void printName(){
        System.out.println("Testing Type classes");
    }

    @AfterClass
    public static void printEnd(){
        System.out.println();
    }

    @Before
    public void setUp(){
        intType = new Int();
        boolType = new Bool();
    }

    @Test
    public void toStringTest(){
        Assert.assertEquals("Testing toString method of Int", "int", intType.toString());
        Assert.assertEquals("Testing toString method of Bool", "boolean", boolType.toString());
    }

    @Test
    public void equalsTest(){
        Assert.assertEquals("Testing equals method of Int", intType, new Int());
        Assert.assertEquals("Testing equals method of Bool", boolType, new Bool());
    }

    @Test
    public void defaultValueTest(){
        Assert.assertEquals("Testing defaultValue method of Int", Int.defaultValue().getValue(), 0);
        Assert.assertEquals("Testing defaultValue method of Bool", Bool.defaultValue().getValue(), false);
    }

}
