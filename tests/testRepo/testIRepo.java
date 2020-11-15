package ToyInterpreter.tests.testRepo;

import ToyInterpreter.exceptions.MyException;
import ToyInterpreter.exceptions.NoProgramsAvailableException;
import ToyInterpreter.exceptions.NotInIntervalException;
import org.junit.*;
import ToyInterpreter.repository.IRepo;
import ToyInterpreter.repository.Repo;

import java.util.List;

public class testIRepo {

    private IRepo<String> repo;

    @BeforeClass
    public static void printName(){
        System.out.println("Testing IRepo");
    }

    @AfterClass
    public static void printEnd(){
        System.out.println();
    }

    @Before
    public void setUp(){
        repo = new Repo<>();
    }

    @Test
    public void sizeTest(){
        Assert.assertEquals("Testing size method of IRepo", 0, repo.size());
    }

    @Test
    public void addTest(){
        repo.add("Hello world");
        Assert.assertEquals("Testing add method of IRepo", 1, repo.size());
    }

    @Test
    public void removeTest() throws MyException {
        repo.add("Hello World");
        repo.remove(0);
        Assert.assertEquals("Testing remove method of IRepo", 0, repo.size());
    }

    @Test(expected = NoProgramsAvailableException.class)
    public void NoProgramAvailableRemoveTest() throws MyException{
        repo.remove(0);
        System.out.println("Testing NoProgramsAvailable exception within remove method of IRepo");
    }

    @Test(expected = NotInIntervalException.class)
    public void NotInIntervalRemoveTest() throws MyException{
        repo.add("Hello world");
        repo.remove(10);
        System.out.println("Testing NotInInterval exception within remove method of IRepo");
    }

    @Test
    public void getCurrentTest() throws MyException{
        repo.add("Hello world");
        repo.add("Hi guys");
        Assert.assertEquals("Testing getCurrent method of IRepo", "Hello world", repo.getCurrent());
    }

    @Test(expected = NoProgramsAvailableException.class)
    public void NoProgramsAvailableGetCurrentTest() throws MyException{
        Assert.assertNotNull("Testing NoProgramsAvailable exception within getCurrent method of IRepo",
                repo.getCurrent());
    }

    @Test
    public void changeCurrentTest() throws MyException {
        repo.add("Hello");
        repo.add("World");
        repo.changeCurrent(1);
        Assert.assertEquals("Testing changeCurrent method of IRepo", "World", repo.getCurrent());
    }

    @Test(expected = NoProgramsAvailableException.class)
    public void NoProgramsAvailableChangeCurrentTest() throws MyException{
        repo.changeCurrent(4);
        System.out.println("Testing NoProgramsAvailable exception within changeCurrent method of IRepo");
    }

    @Test(expected = NotInIntervalException.class)
    public void NotInIntervalChangeCurrentTest() throws MyException{
        repo.add("Hello");
        repo.changeCurrent(4);
        System.out.println("Testing NotInInterval exception within changeCurrent method of IRepo");
    }

    @Test
    public void getAllTest(){
        repo.add("Hello");
        repo.add("World");
        List<String> strings = repo.getAll();
        Assert.assertEquals("Testing getAll method of IRepo", 2, strings.size());
    }

}
