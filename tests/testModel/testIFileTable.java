package ToyInterpreter.tests.testModel;

import ToyInterpreter.exceptions.InvalidFilenameException;
import ToyInterpreter.model.adts.*;
import org.junit.*;

public class testIFileTable {

    @BeforeClass
    public static void printName(){
        System.out.println("Testing IFileTable");
    }

    @AfterClass
    public static void printEnd(){
        System.out.println();
    }

    @Test(expected = InvalidFilenameException.class)
    public void IFileTableTest() throws InvalidFilenameException{
        IFileTable<String, String> ft = new FileTable<>();
        ft.add("hello", "world");

        Assert.assertTrue("Testing isDefined method of IFileTable", ft.isDefined("hello"));
        Assert.assertEquals("Testing lookup method of IFileTable", ft.lookup("hello"), "world");
        Assert.assertEquals("Testing toString method of IFileTable", "hello=world\n",
                ft.toString());
        ft.clear();
        Assert.assertFalse("Testing clear method of IFileTable", ft.isDefined("hello"));

        ft.add("hi", "guys");
        try {
            ft.remove("hi");
        }
        catch (InvalidFilenameException e){
            System.out.println("Testing add method of IFileTable");
        }
        ft.remove("hi");
        System.out.println("Testing remove method of IFileTable");
    }

}
