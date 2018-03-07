package main.view_controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import main.Main;
import main.data.DbConnection;
import main.model.ConnectedUser;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import static main.Main.PATH_RB;

public class LoginController {
    public static final String FXML_LOGIN = "view_controller/login.fxml";

    @FXML
    private TextField textFieldLoginName;
    @FXML
    private TextField textFieldLoginPassword;
    @FXML
    private ResourceBundle rb;

    private Main mainApp;
    private DbConnection dbConnection;

    public LoginController() {
        dbConnection = new DbConnection();
        rb = ResourceBundle.getBundle(PATH_RB);
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    //todo translate log-in and error control messages into 2 languages.

    @FXML
    void handleLogin(ActionEvent event) throws ClassNotFoundException {
        String user = textFieldLoginName.getText();
        String pass = textFieldLoginPassword.getText();
        int userId = dbConnection.userLogin(user, pass);

        boolean loggedIn = userId != DbConnection.USER_NOT_FOUND; //Returns true if our login query returned a user

        if (loggedIn) {
            ConnectedUser connectedUser = new ConnectedUser(userId, user, pass);

            logUser(connectedUser);

        } else {
            showAlertExample();
        }
    }

    void showAlertExample() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(rb.getString("error"));
        alert.setHeaderText(null);
        alert.setContentText(rb.getString("username_password_not_match"));

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

    private void logUser(ConnectedUser connectedUser) {

        try {

            //File prints to log.txt at the project directory above the "src" folder
            String path = "log.txt";

            //By setting this to true, our line will append to the log instead of creating a new one
            PrintStream fileStream = new PrintStream(new FileOutputStream(path, true));

            //Current timestamp
            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

            //Print the timestamp and username to the log
            fileStream.println(timeStamp + " User Logged In: " + connectedUser.getUsername());

        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }

    }

}
