package company.freightroutingnn.controllers;

import company.databaseFiles.SQLFunctions;
import company.freightroutingnn.SceneHandler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {
    @FXML
    private TextField emailAddress;

    @FXML
    private TextField password;

    @FXML
    private Label userMessage;

    @FXML
    private Button signUp;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        signUp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (SQLFunctions.validateEmail(emailAddress.getText())/*checks email is valid*/) {
                    SQLFunctions.enterUser(emailAddress.getText(), password.getText());
                    SceneHandler.changeScene(actionEvent, "login-view.fxml", "Login", 320, 240); //changes scene
                }else{
                    userMessage.setText("Please enter a valid email address");
                }
            }
        });
    }
}
