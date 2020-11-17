package ToyInterpreter.tests.testModel;

import ToyInterpreter.model.adts.*;
import org.junit.*;

public class testIOut {

    @BeforeClass
    public static void printName(){
        System.out.println("Testing IOut");
    }

    @AfterClass
    public static void printEnd(){
        System.out.println();
    }

    @Test
    public void IOutTest(){
        IOut<String> out = new Out<>();
        String testerString = "Hello World";
        String outString = testerString + "\n";
        String emptyOutString = "";
        out.add(testerString);

        Assert.assertEquals("Testing add/toString methods of IOut", outString, out.toString());

        out.clear();
        Assert.assertEquals("Testing clear method of IOut", emptyOutString, out.toString());
    }

}
