package workscheduler.view_controller;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.util.Callback;
import workscheduler.Main;
import workscheduler.model.Appointment;
import workscheduler.model.Customer;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Locale;
import java.util.ResourceBundle;

import static workscheduler.data.Database.FORMAT_DATETIME;

public class OverviewController {
    public static final String FXML_OVERVIEW = "/workscheduler/view_controller/overview.fxml";

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

        /*Set the properties that will be shown in the table*/
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
                cellData -> cellData.getValue().startProperty()
        );

        columnApptEnd.setCellValueFactory(
                cellData -> cellData.getValue().endProperty()
        );


        /*Format the start and end columns*/
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(FORMAT_DATETIME).withLocale(Locale.getDefault());

        columnApptStart.setCellFactory(createDateCellFactory(dateTimeFormatter));
        columnApptEnd.setCellFactory(createDateCellFactory(dateTimeFormatter));
    }

    public void setMainApp(Main mainApp) throws SQLException, ClassNotFoundException {
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
        mainApp.showAddCustomer(new Customer("", "", ""));
    }

    @FXML
    void handleModifyCustomer() {
        mainApp.showAddCustomer(tableViewCustomer.getSelectionModel().getSelectedItem());
    }

    @FXML
    void handleDeleteCustomer() {
        Customer selectedCustomer = tableViewCustomer.getSelectionModel().getSelectedItem();
        try {
            mainApp.getDatabase().deleteCustomer(selectedCustomer);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            mainApp.showDatabaseErrorAlert();
        }
    }

    @FXML
    void handleAddAppointment() {
        LocalDateTime now = LocalDateTime.now().withSecond(0); //We do not use seconds so we are setting them to 0
        Appointment newAppointment = new Appointment(tableViewCustomer.getSelectionModel().getSelectedItem().getId(), "", now, now);

        mainApp.showAddAppointment(tableViewCustomer.getSelectionModel().getSelectedItem().getName(), newAppointment);
    }

    @FXML
    void handleModifyAppointment() {
        mainApp.showAddAppointment(tableViewCustomer.getSelectionModel().getSelectedItem().getName(), tableViewAppointment.getSelectionModel().getSelectedItem());
    }

    @FXML
    void handleDeleteAppointment() {
        Appointment selectedAppointment = tableViewAppointment.getSelectionModel().getSelectedItem();
        try {
            mainApp.getDatabase().deleteAppointment(selectedAppointment);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            mainApp.showDatabaseErrorAlert();
        }
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

        tableViewCustomer.getSelectionModel().select(0);
        tableViewCustomer.getSelectionModel().clearSelection();

        tableViewCustomer.refresh();
        tableViewAppointment.refresh();

        SortedList<Appointment> sortedAppointmentData = new SortedList<>(filteredAppointmentData);
        sortedAppointmentData.comparatorProperty().bind(tableViewAppointment.comparatorProperty());
        tableViewAppointment.setItems(sortedAppointmentData);
    }

    private void showCloseAppointments() {
        LinkedList<Appointment> closeAppointments;
        try {
            closeAppointments = mainApp.getDatabase().getCloseAppointments();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        if (closeAppointments.size() > 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(resources.getString("Appointments_Start_Soon"));
            alert.setHeaderText(null);
            alert.setContentText(resources.getString("One_or_more_appointments"));

            StringBuilder sb = new StringBuilder();

            for (Appointment a : closeAppointments) {
                sb.append(a.toString());
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

    /*Used to display formatted dates in a TableColumn*/
    private Callback<TableColumn<Appointment, LocalDateTime>, TableCell<Appointment, LocalDateTime>> createDateCellFactory(DateTimeFormatter dateTimeFormatter) {
        return new Callback<TableColumn<Appointment, LocalDateTime>, TableCell<Appointment, LocalDateTime>>() {
            @Override
            public TableCell<Appointment, LocalDateTime> call(TableColumn<Appointment, LocalDateTime> col) {
                return new TableCell<Appointment, LocalDateTime>() {
                    @Override
                    protected void updateItem(LocalDateTime item, boolean empty) {

                        super.updateItem(item, empty);
                        if (empty)
                            setText(null);
                        else
                            setText(item.format(dateTimeFormatter));
                    }
                };
            }
        };
    }
}