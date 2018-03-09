package main.view_controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import main.Main;
import main.data.DbConnection;
import main.log.UserLog;
import main.model.ConnectedUser;

import java.sql.SQLException;
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
    void handleLogin(ActionEvent event) throws SQLException, ClassNotFoundException {
        textFieldLoginName.setText(removeWhiteSpace(textFieldLoginName.getText()));
        textFieldLoginPassword.setText(removeWhiteSpace(textFieldLoginPassword.getText()));

        String user = textFieldLoginName.getText();
        String pass = textFieldLoginPassword.getText();
        int userId = mainApp.getDatabase().getDbConnection().userLogin(user, pass);

        boolean loggedIn = userId != DbConnection.QUERY_ERROR; //Returns true if our login query returned a user

        if (loggedIn) { //If login was successful
            ConnectedUser connectedUser = new ConnectedUser(userId, user, pass);

            /*Add the username and login time to our log file*/
            UserLog userLog = new UserLog(connectedUser);
            userLog.logUser();

            mainApp.getDatabase().getDbConnection().setConnectedUser(connectedUser);

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

        //If ok is pressed, load next scene?
//        alert.showAndWait().ifPresent((response -> {
//            if (response == ButtonType.OK) {
//                System.out.println("Alerting!");
//                Parent main = null;
//                try {
//                    main = FXMLLoader.load(getClass().getResource("Next.fxml"));
//                    Scene scene = new Scene(main);
//
//                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//                    stage.setScene(scene);
//
//                    stage.show();
//                } catch (IOException ex) {
//                    ex.printStackTrace();
//                }
//            }
//        }));
    }

    /*Removes all whitespaces and non-visible characters (e.g., tab, \n)*/
    public String removeWhiteSpace(String s) {
        return s.replaceAll("\\s+", "");
    }
}
