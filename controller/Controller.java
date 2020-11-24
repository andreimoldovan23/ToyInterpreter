package ToyInterpreter.controller;

import ToyInterpreter.exceptions.MyException;
import ToyInterpreter.exceptions.StackEmptyException;
import ToyInterpreter.model.PrgState;
import ToyInterpreter.model.adts.IExeStack;
import ToyInterpreter.model.adts.IHeap;
import ToyInterpreter.model.stmts.*;
import ToyInterpreter.model.values.RefValue;
import ToyInterpreter.model.values.Value;
import ToyInterpreter.repository.IRepo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public Stmt getInitialProgram() {
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
        currentProgram.cleanAll();
    }

    private Stream<Integer> getReferencedAddresses(Value val, IHeap<Integer, Value> heap){
        int addr = (int) val.getValue();
        if(!(heap.lookup(addr) instanceof RefValue))
            return Stream.of(addr);
        else
            return Stream.concat(Stream.of(addr), getReferencedAddresses(heap.lookup(addr), heap));
    }

    private List<Integer> getAddresses(List<Value> values, IHeap<Integer, Value> heap){
        return values.stream()
                .filter(e -> e instanceof RefValue)
                .flatMap(e -> getReferencedAddresses(e, heap))
                .collect(Collectors.toList());
    }

    private Map<Integer, Value> garbageCollector(List<Integer> addresses, IHeap<Integer, Value> heap){
        return heap.getContent().stream()
                .filter(e -> addresses.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public void oneStep() throws MyException {
        IExeStack<Stmt> stack = currentProgram.getStack();
        Stmt top;
        try {
            top = stack.pop();
        }
        catch (StackEmptyException e){
            System.out.println("Out:\n" + currentProgram.getOut());
            throw e;
        }
        top.exec(currentProgram);

        if(displayFlag)
            System.out.println("Current state:\n" + currentProgram);
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public void allStep() throws MyException {
        programs.logCurrentPrg();
        IHeap<Integer, Value> heap = currentProgram.getHeap();
        while(true){
            oneStep();
            heap.setContent(
                    garbageCollector(
                            getAddresses(currentProgram.getTable().getValues(), heap),
                            heap));
            programs.logCurrentPrg();
        }
    }

}
