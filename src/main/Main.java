package main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.data.Database;
import main.model.Appointment;
import main.model.Customer;
import main.view.CalendarDialog;
import main.view.StyledScene;
import main.view_controller.AddAppointmentController;
import main.view_controller.AddCustomerController;
import main.view_controller.LoginController;
import main.view_controller.OverviewController;

import java.io.IOException;
import java.time.*;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TimeZone;

import static main.view_controller.AddAppointmentController.FXML_ADD_APPOINTMENT;
import static main.view_controller.AddCustomerController.FXML_ADD_CUSTOMER;
import static main.view_controller.LoginController.FXML_LOGIN;
import static main.view_controller.OverviewController.FXML_OVERVIEW;

public class Main extends Application {

    public static final String PATH_RB = "main.rb";

    private Stage window; //The entire application window "The JavaFX Stage class is the top level JavaFX container."

    private ResourceBundle rb;

    public Database getDatabase() {
        return database;
    }

    private Database database;

    public Main() {
        //todo Required for A. Log-in Form **Requires import java.util.Locale;
//        Locale.setDefault(new Locale("fr"));
//        Locale.setDefault(new Locale("es"));

        rb = ResourceBundle.getBundle(PATH_RB);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.window = primaryStage;
        getWindow().setTitle("Work Scheduler");

        database = new Database();
        showLogin(); //Show login screen on application start
    }

    public static void main(String[] args) {
        launch(args);
    }

    public Stage getWindow() {
        return window;
    }

    /*Sends our application to the login screen*/
    public void showLogin() {
        try {
            StyledScene styledScene = new StyledScene(this, FXML_LOGIN);
            Scene scene = styledScene.create();
            getWindow().setScene(scene);
            getWindow().show();
            LoginController controller = styledScene.getLoader().getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*Sends our application to the overview screen*/
    public void showOverview() {
        try {
            StyledScene styledScene = new StyledScene(this, FXML_OVERVIEW);
            Scene scene = styledScene.create();
            getWindow().setScene(scene);
            getWindow().show();
            OverviewController controller = styledScene.getLoader().getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Show a calendar in a new window
    public void showCalendar() {
        CalendarDialog calendarDialog = new CalendarDialog(new DatePicker());

        calendarDialog.showDialog(getWindow());
    }

    public void showAddCustomer(Customer customer) {
        try {
            StyledScene styledScene = new StyledScene(this, FXML_ADD_CUSTOMER);

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add Customer");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(getWindow());
            Scene scene = styledScene.create();
            dialogStage.setScene(scene);

            // Set the customer into the controller.
            AddCustomerController controller = styledScene.getLoader().getController();
            controller.setDialogStage(dialogStage);
            controller.setCustomer(database, customer);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showAddAppointment(String customerName, Appointment appointment) {
        //todo delete
//        try {
//            StyledScene styledScene = new StyledScene(this, FXML_ADD_APPOINTMENT);
//
//            // Create the dialog Stage.
//            Stage dialogStage = new Stage();
//            dialogStage.setTitle("Add Appointment");
//            dialogStage.initModality(Modality.WINDOW_MODAL);
//            dialogStage.initOwner(getWindow());
//            Scene scene = styledScene.create();
//            dialogStage.setScene(scene);
//
//            // Set the customer into the controller.
//            AddAppointmentController controller = styledScene.getLoader().getController();
//            controller.setDialogStage(dialogStage);
//            controller.setAppointment(database, customerName, appointment);
//
//            // Show the dialog and wait until the user closes it
//            dialogStage.showAndWait();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        /*This project requires a lambda expression to schedule and maintain appointments. I do not think a lambda
         * is necessary, but I do not want to fail this requirement.*/
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Add/Modify appointment?");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure?");

        //If ok is pressed, load next scene?
        alert.showAndWait().ifPresent((response -> {
            if (response == ButtonType.OK) {
                try {
                    StyledScene styledScene = new StyledScene(this, FXML_ADD_APPOINTMENT);

                    // Create the dialog Stage.
                    Stage dialogStage = new Stage();
                    dialogStage.setTitle("Add Appointment");
                    dialogStage.initModality(Modality.WINDOW_MODAL);
                    dialogStage.initOwner(getWindow());

                    Scene scene = styledScene.create();
                    dialogStage.setScene(scene);

                    // Set the customer into the controller.
                    AddAppointmentController controller = styledScene.getLoader().getController();
                    controller.setDialogStage(dialogStage);
                    controller.setAppointment(database, customerName, appointment);

                    // Show the dialog and wait until the user closes it
                    dialogStage.showAndWait();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }));

//    } catch(IOException e)
//    {
//        e.printStackTrace();
//    }
}

    public ResourceBundle getRb() {
        return rb;
    }
}
