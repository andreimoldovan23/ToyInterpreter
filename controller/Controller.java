package ToyInterpreter.controller;

import ToyInterpreter.exceptions.MyException;
import ToyInterpreter.exceptions.NoProgramsAvailableException;
import ToyInterpreter.exceptions.StackEmptyException;
import ToyInterpreter.model.PrgState;
import ToyInterpreter.model.adts.IExeStack;
import ToyInterpreter.model.stmts.*;
import ToyInterpreter.repository.IRepo;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    private final IRepo<PrgState> programs;
    private PrgState currentProgram;
    private boolean displayFlag;

    public Controller(IRepo<PrgState> r) throws MyException {
        programs = r;
        currentProgram = programs.getCurrent();
        displayFlag = true;
    }

    public List<Stmt> getStatements() throws NoProgramsAvailableException{
        if (getNumberStatements() == 0)
            throw new NoProgramsAvailableException();

        List<PrgState> prgStates = programs.getAll();
        List<Stmt> stmts = new ArrayList<>();
        for(PrgState ps : prgStates)
            stmts.add(ps.getInitialProgram());
        return stmts;
    }

    public int getNumberStatements(){
        return programs.size();
    }

    public Stmt getCurrentStatement() throws NoProgramsAvailableException{
        if(currentProgram == null)
            throw new NoProgramsAvailableException();
        return currentProgram.getInitialProgram();
    }

    public void setDisplayFlag(boolean f){
        displayFlag = f;
    }

    public void removeProgram(int index) throws MyException {
        programs.remove(index);
        try{
            changeCurrentProgram(0);
        }
        catch (NoProgramsAvailableException e){
            currentProgram = null;
        }
    }

    public void changeCurrentProgram(int index) throws MyException{
        programs.changeCurrent(index);
        currentProgram = programs.getCurrent();
    }

    public void oneStep() throws MyException{
        if(currentProgram == null)
            throw new NoProgramsAvailableException();

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

    public void allStep() throws MyException{
        while(true){
            try {
                oneStep();
            }
            catch (StackEmptyException e){
                break;
            }
        }
    }

}
