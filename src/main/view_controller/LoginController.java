package main.view_controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import main.Main;
import main.data.UserDAO;
import main.log.UserLog;

import java.util.ResourceBundle;

import static main.Main.PATH_RB;

public class LoginController {
    public static final String FXML_LOGIN = "view_controller/login.fxml";

    @FXML
    private TextField textFieldLoginName;
    @FXML
    private TextField textFieldLoginPassword;

    private ResourceBundle rb;

    private Main mainApp;

    public LoginController() {
        rb = ResourceBundle.getBundle(PATH_RB);
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    void handleLogin(ActionEvent event) {
        textFieldLoginName.setText(removeWhiteSpace(textFieldLoginName.getText()));
        textFieldLoginPassword.setText(removeWhiteSpace(textFieldLoginPassword.getText()));

        String username = textFieldLoginName.getText();
        String password = textFieldLoginPassword.getText();
        int successCode = mainApp.getDatabase().login(username, password);

        boolean loggedIn = successCode == UserDAO.CODE_SUCCESS; //Returns true if our login query returned a user

        if (loggedIn) { //If login was successful
            /*Add the username and login time to our log file*/
            UserLog userLog = new UserLog(username);
            userLog.logUser();

            mainApp.showOverview(); //Show the overview scene
        } else { //If login failed
            showErrorAlert();
        }
    }

    void showErrorAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(rb.getString("error"));
        alert.setHeaderText(null);
        alert.setContentText(rb.getString("username_password_not_match"));
        alert.showAndWait();
    }

    /*Removes all whitespaces and non-visible characters (e.g., tab, \n)*/
    public String removeWhiteSpace(String s) {
        return s.replaceAll("\\s+", "");
    }
}
