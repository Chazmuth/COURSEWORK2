package company.freightroutingnn.controllers;

import company.freightroutingnn.SceneHandler;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private Node addJob;

    @FXML
    private MenuItem viewJobs;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /*addJob.(new EventHandler<>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                SceneHandler.changeScene(actionEvent, "addJob-view.fxml", "Freight Router", 500, 700); //changes scene
            }
        });*/
    }
}
