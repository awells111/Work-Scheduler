package main.view_controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import main.Main;
import main.data.DbConnection;
import main.model.ConnectedUser;

public class LoginController {
    public static final String FXML_LOGIN = "view_controller/login.fxml";
    @FXML
    private TextField textFieldLoginName;
    @FXML
    private TextField textFieldLoginPassword;
    private Main mainApp;
    private DbConnection dbConnection;
    private ConnectedUser connectedUser;

    public LoginController() {
        dbConnection = new DbConnection();
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    void handleLogin(ActionEvent event) throws ClassNotFoundException {
        String user = textFieldLoginName.getText();
        String pass = textFieldLoginPassword.getText();
        int userId = dbConnection.userLogin(user, pass);

        boolean loggedIn = userId != DbConnection.USER_NOT_FOUND; //Returns true if our login query returned a user

        if (loggedIn) {
            connectedUser = new ConnectedUser(userId, user, pass);
        } else {
            showAlertExample();
        }
    }

    void showAlertExample() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error Signing In");
        alert.setHeaderText(null);
        alert.setContentText("Incorrect username or password");

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

        alert.showAndWait();
    }
}
