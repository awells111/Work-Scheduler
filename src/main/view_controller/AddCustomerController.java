package main.view_controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.data.Database;
import main.model.Customer;

import static main.data.Database.CODE_NEW_ENTITY;

public class AddCustomerController {
    public static final String FXML_ADD_CUSTOMER = "view_controller/add_customer.fxml";

    @FXML
    private TextField textFieldCustomerName;

    @FXML
    private TextField textFieldCustomerPhone;

    @FXML
    private TextField textFieldCustomerAddress;

    private Stage dialogStage;
    private Database database;
    private Customer customer;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setCustomer(Database database, Customer customer) {
        this.database = database;
        this.customer = customer;

        setFields();

        textFieldCustomerName.setText(customer.getName());
        textFieldCustomerAddress.setText(customer.getAddress());
        textFieldCustomerPhone.setText(customer.getPhone());
    }

    private final int LIMIT_NAME = 45;
    private final int LIMIT_ADDRESS = 50;
    private final int LIMIT_PHONE = 20;

    @FXML
    void handleCustomerSave() {
        try {
            errorBeforeSave();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        int id = (isNewCustomer()) ? database.nextCustomerId() : customer.getId();
        String name = textFieldCustomerName.getText();
        String address = textFieldCustomerAddress.getText();
        String phone = textFieldCustomerPhone.getText();

        Customer newCustomer = new Customer(id, name, address, phone);

        if (isNewCustomer()) {
            database.addCustomer(newCustomer);
        } else {
            database.updateCustomer(customer, newCustomer);
        }

        dialogStage.close();
    }

    @FXML
    void handleCustomerCancel() {
        dialogStage.close();
    }

    /*Returns true if customer does not exist in the database*/
    private boolean isNewCustomer() {
        return customer.getId() == CODE_NEW_ENTITY;
    }

    /*Returns false if there are no errors in saving the customer*/
    private void errorBeforeSave() throws Exception {
        Alert alert = buildAlert();

        //Display alert for incorrect inputs
        if (textFieldCustomerName.getText().equals("")) {
            alert.setContentText("Name cannot be empty");
            alert.show();
            throw new Exception("Name cannot be empty");
        }

        if (textFieldCustomerAddress.getText().equals("")) {
            alert.setContentText("Address cannot be empty");
            alert.show();
            throw new Exception("Address cannot be empty");
        }

        if (textFieldCustomerPhone.getText().equals("")) {
            alert.setContentText("Phone cannot be empty");
            alert.show();
            throw new Exception("Phone cannot be empty");
        }
    }

    private void setFields() {
        /*Only Accept Letters*/
        textFieldCustomerName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\sa-zA-Z*")) {
                textFieldCustomerName.setText(newValue.replaceAll("[^\\sa-zA-Z]", ""));
            }
        });

        /*Only Accept Digits*/
        textFieldCustomerPhone.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textFieldCustomerPhone.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        setLimit(textFieldCustomerName, LIMIT_NAME);
        setLimit(textFieldCustomerAddress, LIMIT_ADDRESS);
        setLimit(textFieldCustomerPhone, LIMIT_PHONE);
    }

    private void setLimit(TextField textField, int limit) {
        /*Only allow limit characters*/
        textField.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() > oldValue.intValue()) {
                if (textField.getText().length() >= limit) {
                    textField.setText(textField.getText().substring(0, limit));
                }
            }
        });
    }

    private Alert buildAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error Saving Appointment");
        alert.setHeaderText(null);

        return alert;
    }
}
