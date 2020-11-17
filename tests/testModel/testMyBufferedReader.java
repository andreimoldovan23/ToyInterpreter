package ToyInterpreter.tests.testModel;

import ToyInterpreter.model.adts.*;
import org.junit.*;
import java.io.IOException;
import java.io.InputStreamReader;

public class testMyBufferedReader {

    @BeforeClass
    public static void printName(){
        System.out.println("Testing MyBufferedReader");
    }

    @AfterClass
    public static void printEnd(){
        System.out.println();
    }

    @Test
    public void MyBufferedReaderTest() throws IOException {
        MyBufferedReader rd1 = new MyBufferedReader(new InputStreamReader(System.in));
        MyBufferedReader rd2 = new MyBufferedReader(new InputStreamReader(System.in));

        Assert.assertEquals("Testing toString method of MyBufferedReader",
                "BufferedReader, id: 1", rd1.toString());
        Assert.assertEquals("Testing constructor of MyBufferedReader",
                "BufferedReader, id: 2", rd2.toString());

        rd1.close();
        rd2.close();
    }

}
