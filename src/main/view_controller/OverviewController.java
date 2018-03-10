package main.view_controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import main.Main;
import main.data.Database;
import main.model.Appointment;
import main.model.Customer;

import java.util.function.Predicate;

public class OverviewController {
    public static final String FXML_OVERVIEW = "view_controller/overview.fxml";

    //todo myTableView.setPlaceholder(new Label("My table is empty message"));

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
    private TableColumn<Appointment, String> columnApptStart;

    @FXML
    private TableColumn<Appointment, String> columnApptEnd;

    private FilteredList<Appointment> filteredAppointmentData;

    Label selectACustomer; //Will be seen when no customer is selected
    Label addAnAppointment; //Will be seen when a customer is selected but no appointments exist

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
                cellData -> cellData.getValue().startProperty()
        );

        columnApptEnd.setCellValueFactory(
                cellData -> cellData.getValue().endProperty()
        );

    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
        //TODO only show the appointments that are linked to certain customers
        mainApp.getDatabase().setCustomersFromDatabase();
        mainApp.getDatabase().setAppointmentsFromDatabase();

        selectACustomer = new Label("Select a customer to view its appointments");
        addAnAppointment = new Label("Add appointments for this customer!");

        tableViewCustomer.setPlaceholder(new Label("Add a customer to get started"));
        tableViewAppointment.setPlaceholder(selectACustomer);

        tableViewCustomer.setItems(mainApp.getDatabase().getCustomers());

        /*Called when the table's selection changes*/
        tableViewAppointment.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) { //If an Appointment is selected, enable the buttons that can be used
                modifyAppointmentButton.setDisable(false);
                deleteAppointmentButton.setDisable(false);
            }
        });

        initAppointmentFilter();
    }

    @FXML
    void handleShowCalendar() {
        mainApp.showCalendar();
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
        Appointment newAppointment = new Appointment(Database.CODE_NEW_ENTITY, tableViewCustomer.getSelectionModel().getSelectedItem().getId(), "", "", "");

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

        //todo check to see if db behavior needs to be changed. if the appointment list removes items, the invisible table items might not be counted for an insert id
        tableViewCustomer.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Customer>() {
            @Override
            public void changed(ObservableValue<? extends Customer> observable, Customer oldValue, Customer newValue) {
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
                    /*todo modifyAppointmentButton and deleteAppointmentButton will be enabled when an appointment is selected*/
                }

                filteredAppointmentData.setPredicate(new Predicate<Appointment>() {
                    @Override
                    public boolean test(Appointment appointment) {

                        if (newValue == null) { //If selected Customer is null
                            return false;
                        }

                        int customerId = newValue.getId();

                        if (customerId == appointment.getCustomerId()) {
                            return true;
                        }

                        return false; // Does not match.
                    }
                });
            }
        });

        /*todo By selecting and unselecting a row, the filter will appropriately filter the appointments. I would like to find a better solution for this.*/
        tableViewCustomer.getSelectionModel().select(0);
        tableViewCustomer.getSelectionModel().clearSelection();

        tableViewCustomer.refresh();
        tableViewAppointment.refresh();

        SortedList<Appointment> sortedAppointmentData = new SortedList<>(filteredAppointmentData);
        sortedAppointmentData.comparatorProperty().bind(tableViewAppointment.comparatorProperty());
        tableViewAppointment.setItems(sortedAppointmentData);
    }
}