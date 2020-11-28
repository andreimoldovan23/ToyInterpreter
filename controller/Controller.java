package ToyInterpreter.controller;

import ToyInterpreter.model.PrgState;
import ToyInterpreter.model.adts.IHeap;
import ToyInterpreter.model.stmts.*;
import ToyInterpreter.model.values.RefValue;
import ToyInterpreter.model.values.Value;
import ToyInterpreter.repository.IRepo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Controller {

    private final IRepo<PrgState> programs;
    private final List<String> exceptions;
    private final ExecutorService executor;
    private boolean displayFlag;

    public Controller(IRepo<PrgState> r) {
        programs = r;
        displayFlag = true;
        exceptions = new ArrayList<>();
        executor = Executors.newFixedThreadPool(4);
    }

    public Stmt getInitialProgram() {
        return programs.getMainProgram().getInitialProgram();
    }

    public void setDisplayFlag(boolean f){
        displayFlag = f;
    }

    public void setLogFile(String path) throws IOException{
        programs.setLogFile(path);
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
        return new ConcurrentHashMap<>(heap.getContent().stream()
                .filter(e -> addresses.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }

    private List<PrgState> removeCompletedPrograms(List<PrgState> programs) {
        return programs.stream()
                .filter(PrgState::isNotCompleted)
                .collect(Collectors.toList());
    }

    private void oneStepForAll(List<PrgState> states) throws InterruptedException {
        List<Callable<PrgState>> callables = states.stream()
                        .map(s -> (Callable<PrgState>)(s::oneStep))
                        .collect(Collectors.toList());

        List<PrgState> newPrograms = executor.invokeAll(callables)
                        .stream()
                        .map(c -> {
                            try{
                                return c.get();
                            }
                            catch (InterruptedException ie){
                                ie.printStackTrace();
                            }
                            catch (ExecutionException ee){
                                exceptions.add(ee.toString());
                            }
                            return null;
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

        states.addAll(newPrograms);
        states.forEach(programs::logCurrentPrg);
        programs.setPrgList(states);
    }

    public void allStep() throws InterruptedException {
        IHeap<Integer, Value> heap = programs.getMainProgram().getHeap();
        List<PrgState> states = programs.getAll();
        while(states.size() > 0){
            oneStepForAll(states);
            states = removeCompletedPrograms(programs.getAll());

            if(states.size() > 0) {
                List<Value> symTablesValues = states.stream()
                        .flatMap(s -> s.getTable().getValues().stream())
                        .collect(Collectors.toList());
                heap.setContent(garbageCollector(getAddresses(symTablesValues, heap), heap));
            }

            exceptions.forEach(System.out::println);
            exceptions.clear();

            if(displayFlag)
                states.forEach(System.out::println);
        }

        executor.shutdownNow();
        programs.closeWriter();
        programs.setPrgList(states);
    }

}
