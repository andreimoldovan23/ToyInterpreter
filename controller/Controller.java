package ToyInterpreter.controller;

import ToyInterpreter.exceptions.MyException;
import ToyInterpreter.exceptions.StackEmptyException;
import ToyInterpreter.model.PrgState;
import ToyInterpreter.model.adts.IExeStack;
import ToyInterpreter.model.stmts.*;
import ToyInterpreter.repository.IRepo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    private final IRepo<PrgState> programs;
    private PrgState currentProgram;
    private boolean displayFlag;

    public Controller(IRepo<PrgState> r) {
        programs = r;
        currentProgram = programs.getCurrent();
        displayFlag = true;
    }

    public List<Stmt> getStatements() {
        List<PrgState> prgStates = programs.getAll();
        List<Stmt> stmts = new ArrayList<>();
        for(PrgState ps : prgStates)
            stmts.add(ps.getInitialProgram());
        return stmts;
    }

    public Stmt getCurrentStatement() {
        return currentProgram.getInitialProgram();
    }

    public void setDisplayFlag(boolean f){
        displayFlag = f;
    }

    public void setList(List<PrgState> l) {
        programs.setPrgList(l);
        currentProgram = programs.getCurrent();
    }

    public void setLogFile(String path) throws IOException{
        programs.setLogFile(path);
    }

    public void closeAll() throws IOException {
        programs.closeWriter();
        currentProgram.reset();
    }

    public void oneStep() throws MyException, IOException {
        IExeStack<Stmt> stack = currentProgram.getStack();
        Stmt top;
        try {
            top = stack.pop();
        }
        catch (StackEmptyException e){
            System.out.println("Out:\n" + currentProgram.getOut());
            currentProgram.reset();
            throw e;
        }
        currentProgram = top.exec(currentProgram);

        if(displayFlag)
            System.out.println("Current state:\n" + currentProgram);
    }

    public void allStep() throws MyException, IOException{
        programs.logCurrentPrg();
        while(true){
            try {
                oneStep();
                programs.logCurrentPrg();
            }
            catch (StackEmptyException e){
                break;
            }
        }
    }

}
