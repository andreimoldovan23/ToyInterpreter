package ToyInterpreter.tests.testModel;

import ToyInterpreter.model.adts.*;
import org.junit.*;

public class testISymTable {

    @BeforeClass
    public static void printName(){
        System.out.println("Testing ISymTable");
    }

    @AfterClass
    public static void printEnd(){
        System.out.println();
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

}
