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

public class LoginController implements Initializable {
    @FXML
    private TextField username;

    @FXML
    private TextField password;

    @FXML
    private Button signUp;

    @FXML
    protected void onLoginButtonClick() {
        if(SQLFunctions.checkUser(password.getText(), username.getText())){
            System.out.println("access granted");
        }else{
            System.out.println("Wrong username or password");
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        signUp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                SceneHandler.changeScene(actionEvent, "signup.fxml", "Sign Up", 320, 240);
            }
        });
    }
}