package main.view_controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import main.Main;
import main.model.Appointment;
import main.model.Customer;
import main.data.Database;

import java.util.ArrayList;

public class OverviewController {
    public static final String FXML_OVERVIEW = "view_controller/overview.fxml";

    //todo myTableView.setPlaceholder(new Label("My table is empty message"));

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

        tableViewCustomer.setItems(mainApp.getDatabase().getCustomers());
        tableViewAppointment.setItems(mainApp.getDatabase().getAppointments());

        selectACustomer = new Label("Select a customer to view its appointments");
        addAnAppointment = new Label("Add appointments for this customer!");

        tableViewCustomer.setPlaceholder(new Label("Add a customer to get started"));
        tableViewAppointment.setPlaceholder(selectACustomer);
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
}