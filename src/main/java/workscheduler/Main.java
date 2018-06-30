package workscheduler;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;
import workscheduler.data.Database;
import workscheduler.model.Appointment;
import workscheduler.model.Customer;
import workscheduler.view.CalendarDialog;
import workscheduler.view.StyledScene;
import workscheduler.view_controller.AddAppointmentController;
import workscheduler.view_controller.AddCustomerController;
import workscheduler.view_controller.LoginController;
import workscheduler.view_controller.OverviewController;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import static workscheduler.view_controller.AddAppointmentController.FXML_ADD_APPOINTMENT;
import static workscheduler.view_controller.AddCustomerController.FXML_ADD_CUSTOMER;
import static workscheduler.view_controller.LoginController.FXML_LOGIN;
import static workscheduler.view_controller.OverviewController.FXML_OVERVIEW;

public class Main extends Application {

    /*Path of the resource bundle that will be used for this application*/
    public static final String PATH_RB = "rb";

    /*The entire application window "The JavaFX Stage class is the top level JavaFX container."*/
    private Stage window;

    private Database database;

    private ResourceBundle rb;

    public Main() {
        rb = ResourceBundle.getBundle(PATH_RB);
    }

    @Override
    public void start(Stage primaryStage) {
        this.window = primaryStage;
        getWindow().setTitle(getRb().getString("application_title"));

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
            StyledScene styledScene = new StyledScene(FXML_LOGIN, getRb());
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
            StyledScene styledScene = new StyledScene(FXML_OVERVIEW, getRb());
            Scene scene = styledScene.create();
            getWindow().setScene(scene);
            getWindow().show();
            OverviewController controller = styledScene.getLoader().getController();

            /*If Customers or Appointments do not load properly, go back to the login screen*/
            try {
                controller.setMainApp(this);
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                showDatabaseErrorAlert();
                showLogin();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*Show a calendar in a new window*/
    public void showCalendar() {
        CalendarDialog calendarDialog = new CalendarDialog(database.getAppointments());

        calendarDialog.showDialog(getWindow());
    }

    public void showAddCustomer(Customer customer) {
        try {
            StyledScene styledScene = new StyledScene(FXML_ADD_CUSTOMER, getRb());

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle(getRb().getString("add_customer_title"));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(getWindow());
            Scene scene = styledScene.create();
            dialogStage.setScene(scene);

            // Set the Customer in the controller.
            AddCustomerController controller = styledScene.getLoader().getController();
            controller.setDialogStage(dialogStage);
            controller.setCustomer(database, customer);
            controller.setMainApp(this);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showAddAppointment(String customerName, Appointment appointment) {
        try {
            StyledScene styledScene = new StyledScene(FXML_ADD_APPOINTMENT, getRb());

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle(getRb().getString("add_appointment_title"));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(getWindow());
            Scene scene = styledScene.create();
            dialogStage.setScene(scene);

            // Set the Appointment in the controller.
            AddAppointmentController controller = styledScene.getLoader().getController();
            controller.setDialogStage(dialogStage);
            controller.setAppointment(database, customerName, appointment);
            controller.setMainApp(this);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showReports() {
        List<String> choices = new ArrayList<>();
        choices.add(getRb().getString("Appointments_by_Type"));
        choices.add(getRb().getString("Schedules_by_Customer"));
        choices.add(getRb().getString("Appointments_by_Customer"));

        ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);
        dialog.setTitle(getRb().getString("Reports"));
        dialog.setHeaderText(null);
        dialog.setContentText(getRb().getString("Choose_a_report"));

        dialog.showAndWait().ifPresent((String result) -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(result);
            alert.setHeaderText(null);
            alert.setContentText(result);

            StringBuilder sb = new StringBuilder();

            TextArea textArea = new TextArea();
            textArea.setEditable(false);
            textArea.setWrapText(true);

            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            GridPane.setVgrow(textArea, Priority.ALWAYS);
            GridPane.setHgrow(textArea, Priority.ALWAYS);

            GridPane expContent = new GridPane();
            expContent.setMaxWidth(Double.MAX_VALUE);
            expContent.add(textArea, 0, 1);

            alert.getDialogPane().setContent(expContent);

            if (result.equals(choices.get(0))) {
                HashMap<String, Integer> hashMap = new HashMap<>();

                for (Appointment a : getDatabase().getAppointments()) { //Count each type in the database
                    if (!hashMap.containsKey(a.getType())) {
                        hashMap.put(a.getType(), 1);
                    } else {
                        hashMap.put(a.getType(), hashMap.get(a.getType()) + 1);
                    }
                }

                hashMap.forEach((k, v) -> {
                    sb.append(k);
                    sb.append(": ");
                    sb.append(Integer.toString(v));
                    sb.append(System.lineSeparator());
                });

            } else if (result.equals(choices.get(1))) {
                HashMap<Integer, StringBuilder> appointmentMap = new HashMap<>();

                for (Appointment a : getDatabase().getAppointments()) { //Build a String containing each appointment
                    appointmentMap.putIfAbsent(a.getCustomerId(), new StringBuilder());

                    StringBuilder currentAppointment = appointmentMap.get(a.getCustomerId());

                    currentAppointment.append(a.getStart())
                            .append(" - ")
                            .append(a.getEnd())
                            .append(System.lineSeparator());

                    appointmentMap.put(a.getCustomerId(), currentAppointment);
                }

                for (Customer c : getDatabase().getCustomers()) {
                    sb.append(getRb().getString("Schedule_of")).append(c.getName()).append(System.lineSeparator());

                    if (!appointmentMap.containsKey(c.getId())) {
                        sb.append(getRb().getString("This_customer_has_no_appointments")).append(System.lineSeparator());
                    } else {
                        sb.append(appointmentMap.get(c.getId()).toString());
                    }

                    sb.append(System.lineSeparator());
                }

            } else if (result.equals(choices.get(2))) {
                HashMap<Integer, Integer> hashMap = new HashMap<>();

                for (Appointment a : getDatabase().getAppointments()) {
                    if (!hashMap.containsKey(a.getCustomerId())) {
                        hashMap.put(a.getCustomerId(), 1);
                    } else {
                        hashMap.put(a.getCustomerId(), hashMap.get(a.getCustomerId()) + 1);
                    }
                }

                for (Customer c : getDatabase().getCustomers()) {
                    sb.append(c.getName()).append(getRb().getString("_has_"));

                    if (hashMap.get(c.getId()) == null) {
                        sb.append("0");
                    } else {
                        sb.append(Integer.toString(hashMap.get(c.getId())));
                    }

                    sb.append(getRb().getString("_appointments"));
                    sb.append(System.lineSeparator());
                }
            }

            textArea.setText(sb.toString());
            alert.showAndWait();
        });
    }

    public Database getDatabase() {
        return database;
    }

    private ResourceBundle getRb() {
        return rb;
    }

    public void showDatabaseErrorAlert() {
        showDatabaseErrorAlert(getRb().getString("Database_Connection_Error"));
    }

    public void showDatabaseErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(getRb().getString("Database_Connection_Error"));
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
