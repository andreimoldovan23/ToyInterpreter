package view;

import controller.Controller;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import repository.Repo;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.FileInputStream;
import java.io.IOException;

public class GUI extends Application {

    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        String fxmlDocPath = "src\\view\\setUp.fxml";
        FileInputStream fxmlStream = new FileInputStream(fxmlDocPath);

        loader.setControllerFactory(e -> {
            try {
                return new Controller(new Repo<>(null, ""), stage);
            } catch (IOException ioException) {
                ioException.printStackTrace();
                return null;
            }
        });
        BorderPane root = loader.load(fxmlStream);

        Controller controller = loader.getController();
        stage.setResizable(true);
        stage.setOnCloseRequest(controller::handleExit);
        stage.setOnShown(e -> { controller.handleChangeProgram(); controller.menuHover(); });
        stage.setTitle("Interpreter");
        stage.setScene(new Scene(root));
        stage.getIcons().add(new Image(controller.convertToUrl("interpreterIcon.jpg")));
        stage.getScene().getStylesheets().add(controller.convertToUrl("view\\mainWindowStyle.css"));

        stage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.L, KeyCombination.CONTROL_ANY),
                controller::handleChangeLog);
        stage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_ANY),
                controller::handleChangeProgram);

        stage.show();
    }

    public void run() {
        launch();
    }

}
