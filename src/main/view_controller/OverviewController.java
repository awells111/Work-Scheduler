package main.view_controller;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import main.Main;
import main.data.Database;
import main.model.Appointment;
import main.model.Customer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class OverviewController {
    public static final String FXML_OVERVIEW = "view_controller/overview.fxml";

    @FXML
    private Button modifyCustomerButton;

    @FXML
    private Button deleteCustomerButton;

    @FXML
    private Button addAppointmentButton;

    @FXML
    private Button modifyAppointmentButton;

    @FXML
    private Button deleteAppointmentButton;

    @FXML
    private TableView<Customer> tableViewCustomer;

    @FXML
    private TableColumn<Customer, Integer> columnCustId;

    @FXML
    private TableColumn<Customer, String> columnCustName;

    @FXML
    private TableColumn<Customer, String> columnCustAddress;

    @FXML
    private TableColumn<Customer, String> columnCustPhone;

    @FXML
    private TableView<Appointment> tableViewAppointment;

    @FXML
    private TableColumn<Appointment, Integer> columnApptId;

    @FXML
    private TableColumn<Appointment, String> columnApptType;

    @FXML
    private TableColumn<Appointment, LocalDateTime> columnApptStart;

    @FXML
    private TableColumn<Appointment, LocalDateTime> columnApptEnd;

    @FXML
    private ResourceBundle resources;

    private FilteredList<Appointment> filteredAppointmentData;

    private Main mainApp;

    @FXML
    private void initialize() {

        //Need to add .asObject() for non-Strings in JavaFX
        columnCustId.setCellValueFactory(
                cellData -> cellData.getValue().idProperty().asObject()
        );

        columnCustName.setCellValueFactory(
                cellData -> cellData.getValue().nameProperty()
        );

        columnCustAddress.setCellValueFactory(
                cellData -> cellData.getValue().addressProperty()
        );

        columnCustPhone.setCellValueFactory(
                cellData -> cellData.getValue().phoneProperty()
        );

        columnApptId.setCellValueFactory(
                cellData -> cellData.getValue().idProperty().asObject()
        );

        columnApptType.setCellValueFactory(
                cellData -> cellData.getValue().typeProperty()
        );

        columnApptStart.setCellValueFactory(
                cellData -> cellData.getValue().startProperty() //todo start and end print in wrong format
        );

        columnApptEnd.setCellValueFactory(
                cellData -> cellData.getValue().endProperty()
        );

    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;

        mainApp.getDatabase().setCustomersFromDatabase();
        mainApp.getDatabase().setAppointmentsFromDatabase();

        tableViewCustomer.setItems(mainApp.getDatabase().getCustomers());

        /*Called when the table's selection changes*/
        tableViewAppointment.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) { //If an Appointment is selected, enable the buttons that can be used
                modifyAppointmentButton.setDisable(false);
                deleteAppointmentButton.setDisable(false);
            } else { //Else if no appointment is selected, disable the buttons
                modifyAppointmentButton.setDisable(true);
                deleteAppointmentButton.setDisable(true);
            }
        });

        initAppointmentFilter();

        showCloseAppointments();
    }

    @FXML
    void handleShowCalendar() {
        mainApp.showCalendar();
    }

    @FXML
    void handleShowReports() {
        mainApp.showReports();
    }

    @FXML
    void handleAddCustomer() {
        Customer newCustomer = new Customer(Database.CODE_NEW_ENTITY, "", "", "");

        mainApp.showAddCustomer(newCustomer);
    }

    @FXML
    void handleModifyCustomer() {
        mainApp.showAddCustomer(tableViewCustomer.getSelectionModel().getSelectedItem());
    }

    @FXML
    void handleDeleteCustomer() {
        Customer selectedCustomer = tableViewCustomer.getSelectionModel().getSelectedItem();
        mainApp.getDatabase().deleteCustomer(selectedCustomer);
    }

    @FXML
    void handleAddAppointment() {
        LocalDateTime now = LocalDateTime.now();
        Appointment newAppointment = new Appointment(Database.CODE_NEW_ENTITY, tableViewCustomer.getSelectionModel().getSelectedItem().getId(), "", now, now);

        mainApp.showAddAppointment(tableViewCustomer.getSelectionModel().getSelectedItem().getName(), newAppointment);
    }

    @FXML
    void handleModifyAppointment() {
        mainApp.showAddAppointment(tableViewCustomer.getSelectionModel().getSelectedItem().getName(), tableViewAppointment.getSelectionModel().getSelectedItem());
    }

    @FXML
    void handleDeleteAppointment() {
        Appointment selectedAppointment = tableViewAppointment.getSelectionModel().getSelectedItem();

        mainApp.getDatabase().deleteAppointment(selectedAppointment);
    }

    /*This will filter out Appointments based on the selected Customer*/
    private void initAppointmentFilter() {
        filteredAppointmentData = new FilteredList<>(mainApp.getDatabase().getAppointments(), p -> true);

        tableViewCustomer.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) { //If no customer is selected, disable the buttons because they cannot be used.
                deleteCustomerButton.setDisable(true);
                modifyCustomerButton.setDisable(true);
                addAppointmentButton.setDisable(true);
                modifyAppointmentButton.setDisable(true);
                deleteAppointmentButton.setDisable(true);
            } else {
                deleteCustomerButton.setDisable(false);
                modifyCustomerButton.setDisable(false);
                addAppointmentButton.setDisable(false);
            }

            filteredAppointmentData.setPredicate(appointment -> {

                if (newValue == null) { //If selected Customer is null
                    return false;
                }

                int customerId = newValue.getId();

                if (customerId == appointment.getCustomerId()) {
                    return true;
                }

                return false; //Appointment does not match Customer
            });
        });

        /*todo By selecting and unselecting a row, the filter will appropriately filter the appointments. I would like to find a better solution for this if possible.*/
        tableViewCustomer.getSelectionModel().select(0);
        tableViewCustomer.getSelectionModel().clearSelection();

        tableViewCustomer.refresh();
        tableViewAppointment.refresh();

        SortedList<Appointment> sortedAppointmentData = new SortedList<>(filteredAppointmentData);
        sortedAppointmentData.comparatorProperty().bind(tableViewAppointment.comparatorProperty());
        tableViewAppointment.setItems(sortedAppointmentData);
    }

    private void showCloseAppointments() {
        ArrayList<Appointment> closeAppointments = mainApp.getDatabase().getCloseAppointments();

        if (closeAppointments.size() > 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(resources.getString("Appointments_Start_Soon"));
            alert.setHeaderText(null);
            alert.setContentText(resources.getString("One_or_more_appointments"));

            StringBuilder sb = new StringBuilder();

            for (Appointment a : closeAppointments) {
                sb.append(a.toStringUserFriendly());
                sb.append(System.lineSeparator());
            }

            TextArea textArea = new TextArea(sb.toString());
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

            alert.showAndWait();
        }
    }
}