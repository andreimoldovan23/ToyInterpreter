package ToyInterpreter.tests.testModel;

import ToyInterpreter.exceptions.InvalidAddress;
import ToyInterpreter.model.adts.Heap;
import ToyInterpreter.model.adts.IHeap;
import org.junit.*;

public class testIHeap {

    @BeforeClass
    public static void printName(){
        System.out.println("Testing IHeap");
    }

    @AfterClass
    public static void printEnd(){
        System.out.println();
    }

    @Test(expected = InvalidAddress.class)
    public void IHeapTest() throws InvalidAddress {
        IHeap<Integer, String> heap = new Heap<>();
        int first = heap.add("Hello world");
        int second = heap.add("Hi guys");
        String expected = first + "-->Hello world\n" + second + "-->Hi guys\n";

        Assert.assertEquals("Testing add/toString methods of IHeap", expected, heap.toString());
        Assert.assertTrue("Testing isDefined method of IHeap", heap.isDefined(first));
        Assert.assertEquals("Testing lookup method of IHeap", "Hi guys", heap.lookup(second));
        heap.remove(first);
        Assert.assertFalse("Testing remove method of IHeap", heap.isDefined(first));
        heap.update(second, "Hello world");
        Assert.assertEquals("Testing update method of IHeap", "Hello world", heap.lookup(second));
        heap.clear();
        Assert.assertFalse("Testing clear method of IHeap", heap.isDefined(second));
        heap.remove(first);
        System.out.println("Testing InvalidAddress exception of remove method of IHeap");
    }

}
