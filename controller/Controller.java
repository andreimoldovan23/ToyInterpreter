package controller;

import exceptions.MyException;
import run.Main;
import exceptions.InexistingFile;
import model.PrgState;
import model.adts.*;
import model.stmts.*;
import model.values.RefValue;
import model.values.Value;
import repository.IRepo;
import view.AlertBox;
import view.ExitPrompt;
import view.LogPrompt;
import view.ProgramSelect;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@SuppressWarnings({"rawtypes", "FieldMayBeFinal"})
public class Controller implements Initializable {

    private final IRepo<PrgState> programs;
    private final List<String> exceptions;
    private ExecutorService executor;
    private List<Stmt> statements;
    private Stage stage;
    private SimpleIntegerProperty integerProperty;
    private SimpleObjectProperty<ObservableList<Integer>> programsProperty;

    @FXML
    public Label nrPrograms;
    @FXML
    public Button runButton;
    @FXML
    public ListView<Integer> listPrograms;
    @FXML
    public TableView symTableView;
    @FXML
    public TableView fileTableView;
    @FXML
    public TableView heapView;
    @FXML
    public ListView<String> outView;
    @FXML
    public TableView semaphoreTableView;
    @FXML
    public TableView countDownTableView;
    @FXML
    public TableView barrierTableView;
    @FXML
    public TableView lockTableView;
    @FXML
    public TableView procTableView;
    @FXML
    public ListView<String> stackView;
    @FXML
    public MenuBar menuBar;

    @FXML
    @SuppressWarnings("unchecked")
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nrPrograms.textProperty().bind(integerProperty.asString());
        listPrograms.itemsProperty().bind(programsProperty);
        integerProperty.set(0);
        programsProperty.set(getProgramIds());
        listPrograms.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, s, t1) -> updateTables()
        );

        listPrograms.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        outView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        symTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        fileTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        heapView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        semaphoreTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        countDownTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        lockTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        barrierTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        procTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        TableColumn<Map, String> symTableNameColumn = new TableColumn<>("name");
        TableColumn<Map, String> symTableValueColumn = new TableColumn<>("value");
        TableColumn<Map, String> fileTableFileColumn = new TableColumn<>("file");
        TableColumn<Map, String> fileTableReaderColumn = new TableColumn<>("reader");
        TableColumn<Map, String> heapAddressColumn = new TableColumn<>("address");
        TableColumn<Map, String> heapValueColumn = new TableColumn<>("value");
        TableColumn<Map, String> semaphoreIdColumn = new TableColumn<>("id");
        TableColumn<Map, String> semaphoreSizeColumn = new TableColumn<>("size");
        TableColumn<Map, String> semaphoreProgramsColumn = new TableColumn<>("acquiredPrograms");
        TableColumn<Map, String> countDownLatchIDColumn = new TableColumn<>("id");
        TableColumn<Map, String> countDownLatchSizeColumn = new TableColumn<>("size");
        TableColumn<Map, String> lockId = new TableColumn<>("id");
        TableColumn<Map, String> lockedProgram = new TableColumn<>("locked");
        TableColumn<Map, String> barrierId = new TableColumn<>("barrierNo");
        TableColumn<Map, String> barrierSize = new TableColumn<>("barrierSize");
        TableColumn<Map, String> atBarrier = new TableColumn<>("atBarrier");
        TableColumn<Map, String> procSignature = new TableColumn<>("signature");
        TableColumn<Map, String> procDefinition = new TableColumn<>("definition");
        symTableNameColumn.setPrefWidth(150);
        symTableValueColumn.setPrefWidth(150);
        fileTableFileColumn.setPrefWidth(150);
        fileTableReaderColumn.setPrefWidth(150);
        heapAddressColumn.setPrefWidth(150);
        heapValueColumn.setPrefWidth(150);
        semaphoreIdColumn.setPrefWidth(150);
        semaphoreSizeColumn.setPrefWidth(150);
        semaphoreProgramsColumn.setPrefWidth(200);
        countDownLatchIDColumn.setPrefWidth(100);
        countDownLatchSizeColumn.setPrefWidth(100);
        lockId.setPrefWidth(100);
        lockedProgram.setPrefWidth(100);
        barrierId.setPrefWidth(150);
        barrierSize.setPrefWidth(150);
        atBarrier.setPrefWidth(200);
        procSignature.setPrefWidth(435);
        procDefinition.setPrefWidth(435);

        symTableNameColumn.setCellValueFactory(new MapValueFactory<>("name"));
        symTableValueColumn.setCellValueFactory(new MapValueFactory<>("value"));
        symTableView.getColumns().addAll(symTableNameColumn, symTableValueColumn);

        fileTableFileColumn.setCellValueFactory(new MapValueFactory<>("file"));
        fileTableReaderColumn.setCellValueFactory(new MapValueFactory<>("reader"));
        fileTableView.getColumns().addAll(fileTableFileColumn, fileTableReaderColumn);

        heapAddressColumn.setCellValueFactory(new MapValueFactory<>("address"));
        heapValueColumn.setCellValueFactory(new MapValueFactory<>("value"));
        heapView.getColumns().addAll(heapAddressColumn, heapValueColumn);

        semaphoreIdColumn.setCellValueFactory(new MapValueFactory<>("id"));
        semaphoreSizeColumn.setCellValueFactory(new MapValueFactory<>("size"));
        semaphoreProgramsColumn.setCellValueFactory(new MapValueFactory<>("acquiredPrograms"));
        semaphoreTableView.getColumns().addAll(semaphoreIdColumn, semaphoreSizeColumn, semaphoreProgramsColumn);

        countDownLatchIDColumn.setCellValueFactory(new MapValueFactory<>("id"));
        countDownLatchSizeColumn.setCellValueFactory(new MapValueFactory<>("size"));
        countDownTableView.getColumns().addAll(countDownLatchIDColumn, countDownLatchSizeColumn);

        lockId.setCellValueFactory(new MapValueFactory<>("id"));
        lockedProgram.setCellValueFactory(new MapValueFactory<>("locked"));
        lockTableView.getColumns().addAll(lockId, lockedProgram);

        barrierId.setCellValueFactory(new MapValueFactory<>("barrierNo"));
        barrierSize.setCellValueFactory(new MapValueFactory<>("barrierSize"));
        atBarrier.setCellValueFactory(new MapValueFactory<>("atBarrier"));
        barrierTableView.getColumns().addAll(barrierId, barrierSize, atBarrier);

        procSignature.setCellValueFactory(new MapValueFactory<>("signature"));
        procDefinition.setCellValueFactory(new MapValueFactory<>("definition"));
        procTableView.getColumns().addAll(procSignature, procDefinition);
    }

    public Controller(IRepo<PrgState> r, Stage s) {
        programs = r;
        exceptions = new ArrayList<>();
        executor = Executors.newFixedThreadPool(4);
        statements = Main.getPrograms();

        integerProperty = new SimpleIntegerProperty();
        programsProperty = new SimpleObjectProperty<>();

        stage = s;
    }

    public void menuHover() {
        HBox container = (HBox) menuBar.lookup("HBox");
        for(int i = 0 ; i < container.getChildren().size() ; i++) {
            Node parentNode = container.getChildren().get(i);
            Menu menu = menuBar.getMenus().get(i);

            parentNode.setOnMouseMoved(e-> menu.show());
        }
    }

    private ObservableList<String> getPrograms() {
        return FXCollections.observableArrayList(
                IntStream.range(1, statements.size() + 1).mapToObj(Integer::toString).collect(Collectors.toList())
        );
    }

    private ObservableList<Integer> getProgramIds() {
        return FXCollections.observableArrayList(
                programs.getAll().stream().map(PrgState::getId).collect(Collectors.toList())
        );
    }

    private <T, U> ObservableList<Map<String, String>> createObservable(List<T> left, List<U> right,
                                                                        String messageLeft, String messageRight) {
        ObservableList<Map<String, String>> items = FXCollections.observableArrayList();
        for(int i = 0; i < left.size(); i++) {
            Map<String, String> map = new HashMap<>();
            map.put(messageLeft, left.get(i).toString());
            map.put(messageRight, right.get(i).toString());
            items.add(map);
        }
        return items;
    }

    private <T, U, V> ObservableList<Map<String, String>> createObservable(List<T> left, List<U> middle, List<V> right,
                                                                           String messageLeft, String messageMiddle,
                                                                           String messageRight) {
        ObservableList<Map<String, String>> items = FXCollections.observableArrayList();
        for(int i = 0; i < left.size(); i++) {
            Map<String, String> map = new HashMap<>();
            map.put(messageLeft, left.get(i).toString());
            map.put(messageMiddle, middle.get(i).toString());
            map.put(messageRight, right.get(i).toString());
            items.add(map);
        }
        return items;
    }

    @SuppressWarnings("unchecked")
    private void updateTables() {
        int index = listPrograms.getSelectionModel().getSelectedIndex();
        cleanTables();

        if(index >= 0) {
            PrgState prg = programs.getProgram(index);
            stackView.setItems(FXCollections.observableList(
                    prg.getStack().getAll().stream()
                            .map(p -> "[" + p.toString().substring(0, p.toString().length() - 1) + "]\n")
                            .collect(Collectors.toList())
            ));
            outView.setItems(FXCollections.observableList(prg.getOut().getValues()));
            symTableView.setItems(createObservable(prg.getTable().getKeys(), prg.getTable().getValues(),
                    "name", "value"));
            fileTableView.setItems(createObservable(prg.getFileTable().getKeys(), prg.getFileTable().getValues(),
                    "file", "reader"));
            heapView.setItems(createObservable(prg.getHeap().getKeys(), prg.getHeap().getValues(),
                    "address", "value"));
            semaphoreTableView.setItems(createObservable(prg.getSemaphoreTable().getLeft(),
                    prg.getSemaphoreTable().getMiddle(), prg.getSemaphoreTable().getRight(),
                    "id", "size", "acquiredPrograms"));
            countDownTableView.setItems(createObservable(prg.getCountDownTable().getLeft(),
                    prg.getCountDownTable().getRight(), "id", "size"));
            barrierTableView.setItems(createObservable(prg.getBarrierTable().getLeft(),
                    prg.getBarrierTable().getMiddle(), prg.getBarrierTable().getRight(),
                    "barrierNo", "barrierSize", "atBarrier"));
            lockTableView.setItems(createObservable(prg.getLockTable().getLeft(), prg.getLockTable().getRight(),
                    "id", "locked"));
            procTableView.setItems(createObservable(prg.getProcTable().getSignature(), prg.getProcTable().getDefinition(),
                    "signature", "definition"));
        }
    }

    @SuppressWarnings("all")
    private void setLogFile(String path) {
        try {
            programs.setLogFile(path);
        }
        catch (IOException e) {
            System.out.println(new InexistingFile());
        }
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

    private void cleanTables() {
        stackView.getItems().clear();
        symTableView.getItems().clear();
        outView.getItems().clear();
        fileTableView.getItems().clear();
        heapView.getItems().clear();
        semaphoreTableView.getItems().clear();
        countDownTableView.getItems().clear();
        lockTableView.getItems().clear();
        barrierTableView.getItems().clear();
        procTableView.getItems().clear();
    }

    private void setProgram(Stmt stmt) throws IOException {
        cleanTables();

        Main.clean();
        PrgState newState = new PrgState(new ExeStack<>(), Main.getProcTable(),
                new ArrayList<>(List.of(new SymTable<>())),
                Main.getOut(), Main.getFileTable(), Main.getHeap(), Main.getSemaphoreTable(),
                Main.getCountDownTable(), Main.getBarrierTable(), Main.getLockTable(), stmt);

        programs.setPrgList(new ArrayList<>(List.of(newState)));
        integerProperty.set(1);
        programsProperty.set(getProgramIds());

        exceptions.clear();
        if(!executor.isShutdown())
            executor.shutdownNow();
        executor = Executors.newFixedThreadPool(4);

        listPrograms.getSelectionModel().select(0);
    }

    public String convertToUrl(String s) {
        String resource;
        String path = "src\\" + s;
        try {
            resource = new File(path).toURI().toURL().toString();
        }
        catch (MalformedURLException e) {
            resource = null;
        }
        return resource;
    }

    public void handleChangeProgram() {
        ProgramSelect programSelect = new ProgramSelect();
        programSelect.setController(this);
        programSelect.start(new Stage(), getPrograms(), statements);
    }

    public void handleYesExit() {
        stage.close();
        try {
            Main.clean();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    public void handleExit(Event event) {
        if(!(event.getSource() instanceof MenuItem))
            event.consume();
        ExitPrompt exitPrompt = new ExitPrompt();
        exitPrompt.setController(this);
        exitPrompt.start(new Stage());
    }

    public void handleChangeLog() {
        LogPrompt logPrompt = new LogPrompt();
        logPrompt.setController(this);
        logPrompt.start(new Stage());
    }

    private void handleExceptions() {
        if(exceptions.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            exceptions.forEach(s -> stringBuilder.append(s.substring(41)).append("\n"));
            AlertBox alertBox = new AlertBox();
            alertBox.setController(this);
            alertBox.start(new Stage(), stringBuilder.toString());
        }
    }

    public void handleRun() {
        if(programs.getSize() == 0) {
            handleAlertBox("There are no programs loaded");
            return;
        }

        IHeap<Integer, Value> heap = programs.getProgram(0).getHeap();
        List<PrgState> states = programs.getAll();
        try {
            oneStepForAll(states);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        states = removeCompletedPrograms(programs.getAll());

        if(states.size() > 0) {
            /*List<Value> symTablesValues = states.stream()
                    .flatMap(s -> s.getTable().getValues().stream())
                    .collect(Collectors.toList());
            */
            List<Value> symTablesValues = states.stream()
                    .flatMap(s -> s.getAllTables().stream()
                                .flatMap(symT -> symT.getValues().stream()))
                    .collect(Collectors.toList());

            heap.setContent(garbageCollector(getAddresses(symTablesValues, heap), heap));
        }
        else {
            executor.shutdownNow();
            programs.closeWriter();
            programs.setPrgList(states);
        }

        handleExceptions();
        exceptions.clear();

        programs.setPrgList(states);
        integerProperty.set(programs.getSize());
        programsProperty.set(getProgramIds());
        listPrograms.getSelectionModel().select(0);
    }

    public void handleSelectProgram(Stmt s) {
        try{
            Main.typeChecker(s);
            try {
                setProgram(s);
                handleChangeLog();
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
        catch (MyException me) {
            handleAlertBox(me.toString());
        }
    }

    private void handleAlertBox(String message) {
        AlertBox alertBox = new AlertBox();
        alertBox.setController(this);
        alertBox.start(new Stage(), message);
    }

    public void handleValidLog(String s) {
        setLogFile(s);
    }

}
