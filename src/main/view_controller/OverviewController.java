package main.view_controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import main.Main;
import main.model.Customer;

import java.sql.SQLException;

public class OverviewController {
    public static final String FXML_OVERVIEW = "view_controller/overview.fxml";

    @FXML
    private TableView<Customer> tableViewCustomer;

    @FXML
    private TableColumn<Customer, Integer> columnID;

    @FXML
    private TableColumn<Customer, String> columnName;

    @FXML
    private TableColumn<Customer, String> columnAddress;

    @FXML
    private TableColumn<Customer, String> columnPhone;

    private ObservableList<Customer> customers;

    private Main mainApp;

    @FXML
    private void initialize() {

        columnID.setCellValueFactory(
                cellData -> cellData.getValue().idProperty().asObject()
        );

        columnName.setCellValueFactory(
                cellData -> cellData.getValue().nameProperty()
        );

        columnAddress.setCellValueFactory(
                cellData -> cellData.getValue().addressProperty()
        );

        columnPhone.setCellValueFactory(
                cellData -> cellData.getValue().phoneProperty()
        );

        customers = FXCollections.observableArrayList();


    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;

        /*Select all customers from the database*/
        try {
            customers = FXCollections.observableList(mainApp.getDbConnection().getCustomers());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        tableViewCustomer.setItems(customers);
    }

    @FXML
    void handleShowCalendar() {
        mainApp.showCalendar();
    }

    @FXML
    void handleAddCustomer() {
        Customer customer = new Customer(nextCustomerId(), "PersonName", "Address", "Phone");

        int numUpdated = mainApp.getDbConnection().insertCustomer(customer);

        if (numUpdated > 0) {
            customers.add(customer);
        }
    }

    @FXML
    void handleModifyCustomer() {
        //todo open new window
    }

    @FXML
    void handleDeleteCustomer() {
        Customer selectedCustomer = tableViewCustomer.getSelectionModel().getSelectedItem();

        int numUpdated = mainApp.getDbConnection().deleteCustomer(selectedCustomer);

        if (numUpdated > 0) {
            customers.remove(selectedCustomer);
        }
    }

    /*Since the database does not use autoincrement, we will take the last customer and increment the id*/
    private int nextCustomerId() {
        if (customers.size() == 0) { //If no customers exist
            return 1;
        }

        return customers.get(customers.size() - 1).getId() + 1;
    }

}
