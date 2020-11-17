package ToyInterpreter.tests.testRepo;

import org.junit.*;
import ToyInterpreter.repository.IRepo;
import ToyInterpreter.repository.Repo;
import org.junit.rules.TemporaryFolder;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class testIRepo {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private IRepo<String> repo;
    private String path;

    @BeforeClass
    public static void printName(){
        System.out.println("Testing IRepo");
    }

    @AfterClass
    public static void printEnd(){
        System.out.println();
    }

    @Before
    public void setUp() throws IOException {
        File myTestFile = tempFolder.newFile("testFile.in");
        path = myTestFile.getAbsolutePath();
        repo = new Repo<>("Hello world", path);
    }

    @After
    public void tearDown(){
        repo = null;
    }

    @Test
    public void getCurrentTest(){
        Assert.assertEquals("Testing getCurrent method of IRepo", "Hello world", repo.getCurrent());
    }

    @Test
    public void setPrgListTest() {
        repo.setPrgList(new ArrayList<>(List.of("Hi", "guys")));
        Assert.assertEquals("Testing setPrgList method of IRepo", "Hi guys ",
                repo.getAll().stream().
                        reduce("", (a, b) -> a = a + b + " "));
    }

    @Test
    public void logCurrentPrgTest() throws IOException {
        repo.logCurrentPrg();

        Stream<String> stream = Files.lines(Path.of(path), StandardCharsets.UTF_8);
        long count = stream.count();
        Assert.assertEquals("Testing logCurrentPrg method of IRepo", 2, count);
    }

    @Test
    public void setLogFileTest() throws IOException {
        File myNewLogFile = tempFolder.newFile("newTest.in");
        repo.setLogFile(myNewLogFile.getAbsolutePath());
        repo.logCurrentPrg();

        Stream<String> stream = Files.lines(Path.of(myNewLogFile.getAbsolutePath()), StandardCharsets.UTF_8);
        long count = stream.count();
        Assert.assertEquals("Testing setLogFile method of IRepo", 2, count);
    }

}
