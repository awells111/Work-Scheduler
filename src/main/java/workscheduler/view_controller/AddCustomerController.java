package workscheduler.view_controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import workscheduler.Main;
import workscheduler.data.Database;
import workscheduler.model.Customer;

import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddCustomerController {
    public static final String FXML_ADD_CUSTOMER = "/workscheduler/view_controller/add_customer.fxml";

    @FXML
    private TextField textFieldCustomerName;

    @FXML
    private TextField textFieldCustomerPhone;

    @FXML
    private TextField textFieldCustomerAddress;

    @FXML
    private ResourceBundle resources;

    private Stage dialogStage;
    private Database database;
    private Customer customer;

    private Main mainApp;

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

    /*MYSQL character limits by column*/
    private static final int LIMIT_NAME = 45;
    private static final int LIMIT_ADDRESS = 50;
    private static final int LIMIT_PHONE = 20;

    @FXML
    void handleCustomerSave() {
        if(inputError()) {
          return;
        }

        int id = customer.getId();
        String name = textFieldCustomerName.getText();
        String address = textFieldCustomerAddress.getText();
        String phone = textFieldCustomerPhone.getText();

        Customer newCustomer = new Customer(id, name, address, phone);

        try {
            /*If Customer does not exist in the database, else*/
            if (customer.getId() == Integer.MIN_VALUE) {
                database.addCustomer(newCustomer);
            } else {
                database.updateCustomer(customer, newCustomer);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            mainApp.showDatabaseErrorAlert(resources.getString("Error_Saving_Customer"));
            return;
        }

        dialogStage.close();
    }

    @FXML
    void handleCustomerCancel() {
        dialogStage.close();
    }

    /*Returns false if there are no errors in saving the customer*/
    private boolean inputError() {
        Alert alert = buildAlert();

        //Display alert for incorrect inputs
        if (textFieldCustomerName.getText().equals("")) {
            alert.setContentText(resources.getString("Name_cannot_be_empty"));
            alert.show();
            return true;
        }

        if (textFieldCustomerAddress.getText().equals("")) {
            alert.setContentText(resources.getString("Address_cannot_be_empty"));
            alert.show();
            return true;
        }

        if (textFieldCustomerPhone.getText().equals("")) {
            alert.setContentText(resources.getString("Phone_cannot_be_empty"));
            alert.show();
            return true;
        }

        return false;
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
        alert.setTitle(resources.getString("Error_Saving_Customer"));
        alert.setHeaderText(null);

        return alert;
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }
}
