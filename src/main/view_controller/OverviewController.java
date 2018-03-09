package main.view_controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import main.Main;
import main.model.Customer;
import main.data.Database;

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
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;

        mainApp.getDatabase().setCustomersFromDatabase();

        tableViewCustomer.setItems(mainApp.getDatabase().getCustomers());
    }

    @FXML
    void handleShowCalendar() {
        mainApp.showCalendar();
    }

    @FXML
    void handleAddCustomer() {
        Customer newCustomer = new Customer(Database.CODE_NEW_CUSTOMER, "", "", "");

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
}