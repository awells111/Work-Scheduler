package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.data.Database;
import main.data.DbConnection;
import main.model.Customer;
import main.view.CalendarDialog;
import main.view_controller.AddCustomerController;
import main.view_controller.LoginController;
import main.view_controller.OverviewController;

import java.io.IOException;
import java.util.ResourceBundle;

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
        //Locale.setDefault(new Locale("fr"));
        //Locale.setDefault(new Locale("es"));

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
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(FXML_LOGIN), rb);
            AnchorPane rootLayout = loader.load();
            Scene scene = new Scene(rootLayout);
            getWindow().setScene(scene);
            getWindow().show();
            LoginController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*Sends our application to the overview screen*/
    public void showOverview() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(FXML_OVERVIEW));
            AnchorPane rootLayout = loader.load();
            Scene scene = new Scene(rootLayout);
            getWindow().setScene(scene);
            getWindow().show();
            OverviewController controller = loader.getController();
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

    public void showAddCustomer(DbConnection dbConnection, Customer customer) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource(FXML_ADD_CUSTOMER));
            AnchorPane root = loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add Customer");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(getWindow());
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            // Set the customer into the controller.
            AddCustomerController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setCustomers(database, customer);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
