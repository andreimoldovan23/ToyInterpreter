package ToyInterpreter.tests.testModel;

import ToyInterpreter.model.types.*;
import org.junit.*;

public class testTypes {

    private Type intType, boolType, stringType, refType;

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
        stringType = new StringType();
        refType = new Ref(intType);
    }

    @After
    public void tearDown(){
        refType = null;
        intType = null;
        boolType = null;
        stringType = null;
    }

    @Test
    public void toStringTest(){
        Assert.assertEquals("Testing toString method of Int", "int", intType.toString());
        Assert.assertEquals("Testing toString method of Bool", "boolean", boolType.toString());
        Assert.assertEquals("Testing toString method of StringType", "string", stringType.toString());
        Assert.assertEquals("Testing toString method of Ref", "ref (int)", refType.toString());
    }

    @Test
    public void equalsTest(){
        Assert.assertEquals("Testing equals method of Int", intType, new Int());
        Assert.assertEquals("Testing equals method of Bool", boolType, new Bool());
        Assert.assertEquals("Testing equals method of StringType", stringType, new StringType());
        Assert.assertEquals("Testing equals method of Ref", refType, new Ref(new Int()));
    }

    @Test
    public void defaultValueTest(){
        Assert.assertEquals("Testing defaultValue method of Int", Int.defaultValue().getValue(), 0);
        Assert.assertEquals("Testing defaultValue method of Bool", Bool.defaultValue().getValue(), false);
        Assert.assertEquals("Testing defaultValue method of StringType",
                StringType.defaultValue().getValue(), "");
        Assert.assertEquals("Testing defaultValue method of Ref", Ref.defaultValue(new Int()).getValue(), 0);
    }

    @Test
    public void copyTest() {
        Assert.assertEquals("Testing copy method of Int", intType.copy(), intType);
        Assert.assertEquals("Testing copy method of Bool", boolType.copy(), boolType);
        Assert.assertEquals("Testing copy method of StringType", stringType.copy(), stringType);
        Assert.assertEquals("Testing copy method of Ref", refType.copy(), refType);
    }

}
