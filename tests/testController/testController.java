package tests.testController;

import controller.Controller;
import exceptions.MyException;
import exceptions.NoProgramsAvailableException;
import model.PrgState;
import model.adts.*;
import model.exps.ConstExp;
import model.exps.Exp;
import model.exps.VarExp;
import model.stmts.AssignStmt;
import model.stmts.PrintStmt;
import model.stmts.Stmt;
import model.stmts.VarDecl;
import model.types.*;
import model.values.IntValue;
import org.junit.*;
import repository.*;
import java.util.ArrayList;
import java.util.List;

public class testController {
    Controller controller;
    IRepo<PrgState> repo;
    PrgState prg;

    @BeforeClass
    public static void printName(){
        System.out.println("Testing Controller class");
    }

    @AfterClass
    public static void printEnd(){
        System.out.println();
    }

    @Before
    public void setUp() throws MyException {
        repo = new Repo<>();
        Exp var = new VarExp("a");
        Stmt s1 = new VarDecl(new Int(), var);
        Stmt s2 = new AssignStmt(var, new ConstExp(new IntValue(8)));
        Stmt s3 = new PrintStmt(var);
        prg = new PrgState(new ExeStack<>(), new SymTable<>(), new Out<>(),
                new ArrayList<>(List.of(s1, s2, s3)));
    }

    @Test
    public void getStatementsTest() throws MyException{
        repo.add(prg);
        controller = new Controller(repo);
        List<Stmt> controllerStatements = controller.getStatements();
        for(var s : controllerStatements)
            Assert.assertEquals("Testing getStatements method of Controller",
                    s.toString(), prg.getInitialProgram().toString());
    }

    @Test(expected = NoProgramsAvailableException.class)
    public void NoProgramsAvailableGetStatementsTest() throws MyException{
        controller = new Controller(repo);
        Assert.assertNotNull("Testing NorProgramsAvailable exception within getStatements method of Controller",
                controller.getStatements());
    }

    @Test
    public void removeProgramTest() throws MyException{
        repo.add(prg);
        controller = new Controller(repo);
        controller.removeProgram(0);
        Assert.assertEquals("Testing removeProgram method of Controller", 0, controller.getNumberStatements());
    }

    @Test
    public void getCurrentStatementTest() throws MyException{
        repo.add(prg);
        controller = new Controller(repo);
        Stmt s = controller.getCurrentStatement();
        Assert.assertEquals("Testing getCurrentStatement method of Controller",
                prg.getInitialProgram().toString(), s.toString());
    }

    @Test(expected = NoProgramsAvailableException.class)
    public void NoProgramsAvailableGetCurrentStatementTest() throws MyException{
        repo.add(prg);
        controller = new Controller(repo);
        controller.removeProgram(0);
        Assert.assertNotNull("Testing NoProgramsAvailable exception within getCurrentStatement method of Controller",
                controller.getCurrentStatement());
    }

}
