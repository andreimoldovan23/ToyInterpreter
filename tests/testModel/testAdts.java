package ToyInterpreter.tests.testModel;

import ToyInterpreter.exceptions.StackEmptyException;
import ToyInterpreter.model.adts.*;
import org.junit.*;

public class testAdts {

    @BeforeClass
    public static void printName(){
        System.out.println("Testing ADTs");
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

    @Test
    public void ISymTableTest(){
        ISymTable<String, String> table = new SymTable<>();
        String key = "Hello";
        String value = "World";
        String update = "Guys";
        String tableString = key + "=" + update + "\n";
        table.add(key, value);

        Assert.assertEquals("Testing add/lookup method of ISymTable", value, table.lookup(key));
        Assert.assertNull("Testing lookup method of ISymTable for nulls", table.lookup(value));
        Assert.assertTrue("Testing isDefined method of ISymTable for true", table.isDefined(key));
        Assert.assertFalse("Testing isDefined method of ISymTable for false", table.isDefined(value));

        table.update(key, update);
        Assert.assertEquals("Testing update method of ISymTable", update, table.lookup(key));
        Assert.assertEquals("Testing toString method of ISymTable", tableString, table.toString());

        table.clear();
        Assert.assertNull("Testing clear method of ISymTable", table.lookup(key));
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
