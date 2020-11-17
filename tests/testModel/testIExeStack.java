package ToyInterpreter.tests.testModel;

import ToyInterpreter.exceptions.StackEmptyException;
import ToyInterpreter.model.adts.*;
import org.junit.*;

public class testIExeStack {

    @BeforeClass
    public static void printName(){
        System.out.println("Testing IExeStack");
    }

    @AfterClass
    public static void printEnd(){
        System.out.println();
    }

    @Test(expected = StackEmptyException.class)
    public void IExeStackTest() throws StackEmptyException {
        IExeStack<String> stack = new ExeStack<>();
        String testerString = "Hello";
        String stackString = "[" + testerString.substring(0, testerString.length() - 1) + "]\n";

        Assert.assertTrue("Testing empty method of IExeStack", stack.empty());

        stack.push(testerString);
        Assert.assertFalse("Testing push method of IExeStack", stack.empty());
        Assert.assertEquals("Testing toString method of IExeStack", stackString, stack.toString());
        Assert.assertEquals("Testing pop method of IExeStack", testerString, stack.pop());
        Assert.assertEquals("Testing StackEmptyException", testerString, stack.pop());
    }

}
