package view;

import controller.Controller;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ExitPrompt {

    private Controller controller = null;

    public void start(Stage newStage) {
        newStage.setTitle("ExitPrompt");
        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.centerOnScreen();

        GridPane layout = new GridPane();
        layout.setHgap(10);
        layout.setVgap(10);
        layout.setGridLinesVisible(false);
        layout.setAlignment(Pos.CENTER);

        Label message = new Label("Are you sure you want to exit?");
        Button yes = new Button("Yes");
        Button no = new Button("No");

        yes.setOnAction(e -> {
            newStage.close();
            controller.handleYesExit();
        });
        no.setOnAction(e -> newStage.close());
        yes.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ENTER){
                newStage.close();
                controller.handleYesExit();
            }
            else if(e.getCode() == KeyCode.LEFT){
                no.requestFocus();
            }
        });
        no.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ENTER){
                newStage.close();
            }
            else if(e.getCode() == KeyCode.RIGHT){
                yes.requestFocus();
            }
        });
        no.requestFocus();

        layout.add(message, 0, 0, 2, 1);
        layout.add(no, 0, 1, 1, 1);
        layout.add(yes, 1, 1, 1, 1);

        Scene newScene = new Scene(layout, 300, 100);
        newScene.getStylesheets().add(controller.convertToUrl("view\\exitPromptStyle.css"));
        newStage.getIcons().add(new Image(controller.convertToUrl("redCross.jpg")));
        newStage.setScene(newScene);
        newStage.setResizable(false);
        newStage.show();
    }

    public void setController(Controller c) {
        controller = c;
    }

}
