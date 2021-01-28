package view;

import controller.Controller;
import model.stmts.Stmt;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.List;

public class ProgramSelect {

    private Controller controller;

    public void start(Stage newStage, ObservableList<String> programNumbers, List<Stmt> stmtList) {
        newStage.setTitle("ProgramSelect");
        newStage.initModality(Modality.NONE);
        newStage.centerOnScreen();

        BorderPane root = new BorderPane();
        TextArea programCode = new TextArea();
        programCode.setEditable(false);
        root.setCenter(programCode);

        ListView<String> programs = new ListView<>();
        programs.setItems(programNumbers);
        programs.setEditable(false);
        programs.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        programs.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, s, t1) -> programCode.setText(stmtList.get(Integer.parseInt(t1) - 1).toString())
        );
        programs.getSelectionModel().selectIndices(0);
        programs.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.RIGHT){
                programCode.requestFocus();
            }
        });
        programCode.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.LEFT){
                programs.requestFocus();
            }
        });

        Button button = new Button("SELECT");
        button.setOnAction(e -> {
            Stmt stmt = stmtList.get(programs.getSelectionModel().getSelectedIndices().get(0));
            controller.handleSelectProgram(stmt);
        });

        root.setLeft(programs);
        root.setBottom(button);
        Scene newScene = new Scene(root, 800, 600);
        newScene.getAccelerators().put(new KeyCodeCombination(KeyCode.ENTER), () -> {
            button.requestFocus();
            button.fire();
        });
        newScene.getAccelerators().put(new KeyCodeCombination(KeyCode.ESCAPE), newStage::close);
        newScene.getStylesheets().add(controller.convertToUrl("view\\programSelectStyle.css"));

        newStage.setScene(newScene);
        newStage.getIcons().add(new Image(controller.convertToUrl("select.jpg")));
        newStage.show();
    }

    public void setController(Controller c) {
        controller = c;
    }

}
