package main.view_controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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
    void handleLogin(ActionEvent event) {
        System.out.println("Login button clicked! " + "Username: " + textFieldLoginName.getText() + ", Password: " + textFieldLoginPassword.getText());

        showAlertExample();
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
