package ToyInterpreter.tests.testModel;

import ToyInterpreter.model.adts.*;
import ToyInterpreter.model.values.IntValue;
import ToyInterpreter.model.values.Value;
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
        ISymTable<String, Value> table = new SymTable<>();
        Value intValue = new IntValue(10);
        Value intValueUpdate = new IntValue(20);
        String key = "Hello";
        String value = "World";
        String tableString = key + "=" + intValueUpdate.toString() + "\n";
        table.add(key, intValue);

        Assert.assertEquals("Testing add/lookup method of ISymTable", intValue.getValue(),
                table.lookup(key).getValue());
        Assert.assertNull("Testing lookup method of ISymTable for nulls",
                table.lookup(value));
        Assert.assertTrue("Testing isDefined method of ISymTable for true", table.isDefined(key));
        Assert.assertFalse("Testing isDefined method of ISymTable for false", table.isDefined(value));

        table.update(key, intValueUpdate);
        Assert.assertEquals("Testing update method of ISymTable", intValueUpdate.getValue(),
                table.lookup(key).getValue());
        Assert.assertEquals("Testing toString method of ISymTable", tableString, table.toString());

        ISymTable<String, Value> newTable = table.copy();
        Assert.assertEquals("Testing copy method of ISymTable", newTable.lookup(key).getValue(),
                table.lookup(key).getValue());

        table.clear();
        Assert.assertNull("Testing clear method of ISymTable", table.lookup(key));
    }

}
