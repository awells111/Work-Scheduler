package workscheduler.view_controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import workscheduler.Main;
import workscheduler.log.UserLog;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class LoginController {
    public static final String FXML_LOGIN = "/workscheduler/view_controller/login.fxml";

    private static final String LOG_PATH = "log.txt";

    @FXML
    private TextField textFieldLoginName;
    @FXML
    private PasswordField textFieldLoginPassword;
    @FXML
    private ResourceBundle resources;

    private Main mainApp;

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    void handleLogin() {
        textFieldLoginName.setText(removeWhiteSpace(textFieldLoginName.getText()));
        textFieldLoginPassword.setText(removeWhiteSpace(textFieldLoginPassword.getText()));

        String username = textFieldLoginName.getText();
        String password = textFieldLoginPassword.getText();

        boolean loggedIn;
        try {
            loggedIn = mainApp.getDatabase().login(username, password);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            mainApp.showDatabaseErrorAlert(resources.getString("Error_Signing_In"));
            return;
        }

        if (loggedIn) { //If login was successful
            /*Add the username and login time to our log file*/
            UserLog userLog = new UserLog(username, LOG_PATH, LocalDateTime.now());
            try {
                userLog.logUser();
            } catch (IOException e) {
                e.printStackTrace();
            }

            mainApp.showOverview(); //Show the overview scene
        } else { //If login failed
            showErrorAlert();
        }
    }

    private void showErrorAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(resources.getString("Error"));
        alert.setHeaderText(null);
        alert.setContentText(resources.getString("username_password_not_match"));
        alert.show();
    }

    /*Removes all whitespaces and non-visible characters (e.g., tab, \n)*/
    private String removeWhiteSpace(String s) {
        return s.replaceAll("\\s+", "");
    }
}
