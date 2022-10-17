package company.freightroutingnn;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SceneHandler {
    public static void changeScene(Event event, String fxmlFile, String title, int width, int height) {

        Parent root = null;

        try {
            root = FXMLLoader.load((SceneHandler.class.getResource(fxmlFile)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setTitle(title);
        stage.setScene(new Scene(root, width, height));
        stage.centerOnScreen();
        stage.show();

    }
}
