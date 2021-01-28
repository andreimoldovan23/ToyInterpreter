package view;

import controller.Controller;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LogPrompt {

    private Controller controller;

    public void start(Stage newStage) {
        newStage.setTitle("LogPrompt");
        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.centerOnScreen();

        HBox layout = new HBox();
        layout.requestFocus();
        layout.setSpacing(10);
        layout.setAlignment(Pos.CENTER);

        TextField textField = new TextField();
        textField.setFocusTraversable(false);
        textField.setPromptText("Type new log file name...");
        Button button = new Button("Submit");

        button.setOnAction(e -> {
            controller.handleValidLog(textField.getText());
            newStage.close();
        });
        button.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.LEFT){
                textField.requestFocus();
            }
        });
        textField.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.RIGHT){
                button.requestFocus();
            }
        });
        layout.getChildren().addAll(textField, button);

        Scene newScene = new Scene(layout, 300, 100);
        newScene.getStylesheets().add(controller.convertToUrl("view\\logPromptStyle.css"));
        newScene.getAccelerators().put(new KeyCodeCombination(KeyCode.ESCAPE), newStage::close);
        newScene.getAccelerators().put(new KeyCodeCombination(KeyCode.ENTER), button::fire);

        newStage.setScene(newScene);
        newStage.setResizable(false);
        newStage.getIcons().add(new Image(controller.convertToUrl("journal.jpg")));
        newStage.show();
    }

    public void setController(Controller c) {
        controller = c;
    }

}
