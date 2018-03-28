package main.view_controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import main.Main;
import main.data.UserDAO;
import main.log.UserLog;

import java.util.ResourceBundle;

public class LoginController {
    public static final String FXML_LOGIN = "view_controller/login.fxml";

    @FXML
    private TextField textFieldLoginName;
    @FXML
    private TextField textFieldLoginPassword;
    @FXML
    private ResourceBundle resources;

    private Main mainApp;

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    void handleLogin() throws Exception {
        textFieldLoginName.setText(removeWhiteSpace(textFieldLoginName.getText()));
        textFieldLoginPassword.setText(removeWhiteSpace(textFieldLoginPassword.getText()));

        String username = textFieldLoginName.getText();
        String password = textFieldLoginPassword.getText();
        int successCode = mainApp.getDatabase().login(username, password);

        loggedin(successCode, username);
    }

    void loggedin(int successCode, String username) throws Exception {
        boolean loggedIn = successCode == UserDAO.CODE_SUCCESS; //Returns true if our login query returned a user

        if (loggedIn) { //If login was successful
            /*Add the username and login time to our log file*/
            UserLog userLog = new UserLog(username);
            userLog.logUser();

            mainApp.showOverview(); //Show the overview scene
        } else { //If login failed
            showErrorAlert();
            throw new Exception(resources.getString("username_password_not_match"));
        }
    }

    void showErrorAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(resources.getString("Error"));
        alert.setHeaderText(null);
        alert.setContentText(resources.getString("username_password_not_match"));
        alert.show();
    }

    /*Removes all whitespaces and non-visible characters (e.g., tab, \n)*/
    public String removeWhiteSpace(String s) {
        return s.replaceAll("\\s+", "");
    }
}
