package main.view_controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import main.Main;

public class LoginController {
    public static final String FXML_LOGIN = "view_controller/login.fxml";
    @FXML
    private TextField textFieldLoginName;
    @FXML
    private TextField textFieldLoginPassword;
    private Main mainApp;

    public LoginController() {

    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    void handleLogin() {
        System.out.println("Login button clicked! " + "Username: " + textFieldLoginName.getText() + ", Password: " + textFieldLoginPassword.getText());
    }
}
