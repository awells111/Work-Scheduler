package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
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
import java.util.Locale;
import java.util.ResourceBundle;

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

    public void showAddAppointment(Appointment appointment) {
        try {
            StyledScene styledScene = new StyledScene(this, FXML_ADD_APPOINTMENT);
            //todo lambda for part C
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
            controller.setAppointment(database, appointment);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ResourceBundle getRb() {
        return rb;
    }
}
