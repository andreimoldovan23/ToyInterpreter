package view;

import controller.Controller;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {

    private Controller controller;

    public void start(Stage newStage, String message) {
        int length;
        if (message.length() > 38)
            length = 900;
        else
            length = 500;

        newStage.setTitle("InvalidProgram");
        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.centerOnScreen();

        HBox box = new HBox();
        box.setAlignment(Pos.CENTER);
        Label label = new Label(message);
        box.getChildren().add(label);

        Scene newScene = new Scene(box, length, 100);
        newScene.getStylesheets().add(controller.convertToUrl("view\\alertBoxStyle.css"));
        newScene.getAccelerators().put(new KeyCodeCombination(KeyCode.ENTER), newStage::close);
        newScene.getAccelerators().put(new KeyCodeCombination(KeyCode.ESCAPE), newStage::close);

        newStage.getIcons().add(new Image(controller.convertToUrl("alert.jpg")));
        newStage.setScene(newScene);
        newStage.setResizable(true);
        newStage.show();
    }

    public void setController(Controller c) {
        controller = c;
    }

}
