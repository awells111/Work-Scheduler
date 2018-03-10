package main.view_controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.data.Database;
import main.model.Customer;

import static main.data.Database.CODE_NEW_CUSTOMER;

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

        textFieldCustomerName.setText(customer.getName());
        textFieldCustomerAddress.setText(customer.getAddress());
        textFieldCustomerPhone.setText(customer.getPhone());
    }

    @FXML
    void handleCustomerSave() {
        if (errorBeforeSave()) { //Check for errors like empty fields
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
        return customer.getId() == CODE_NEW_CUSTOMER;
    }

    /*Returns false if there are no errors in saving the customer*/
    private boolean errorBeforeSave() {
        return errorBeforeSave(false);
    }

    private boolean errorBeforeSave(boolean error) {

        if (error) {

        }

//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle("Error Saving Part");
//        alert.setHeaderText(null);
//
//        //Display alert for incorrect inputs
//        if (textfieldPartName.getText().equals("")) {
//            alert.setContentText("Name cannot be empty");
//            alert.showAndWait();
//            return true;
//        }
//
//        if (!isInteger(textfieldPartInv.getText())) {
//            alert.setContentText("Inv must be an integer");
//            alert.showAndWait();
//            return true;
//        }
//
//        if (!isDouble(textfieldPartPrice.getText())) {
//            alert.setContentText("Price/Cost must be a number");
//            alert.showAndWait();
//            return true;
//        }
//
//        if (!isInteger(textfieldPartMin.getText())) {
//            alert.setContentText("Min must be an integer");
//            alert.showAndWait();
//            return true;
//        }
//
//        if (!isInteger(textfieldPartMax.getText())) {
//            alert.setContentText("Max must be an integer");
//            alert.showAndWait();
//            return true;
//        }
//
//        if (radioButtonInHouse.isSelected() && !isInteger(textfieldPartMachineID.getText())) {
//            alert.setContentText("Machine ID must be an integer");
//            alert.showAndWait();
//            return true;
//        } else if (radioButtonOutSourced.isSelected() && textfieldPartMachineID.getText().equals("")) {
//            alert.setContentText("Company Name cannot be empty");
//            alert.showAndWait();
//            return true;
//        }
//
//
//        int inv = Integer.parseInt(textfieldPartInv.getText());
//        int min = Integer.parseInt(textfieldPartMin.getText());
//        int max = Integer.parseInt(textfieldPartMax.getText());
//
//        if (min > max || max < min) { //max < min is redundant but I am including it to meet project specifications
//            alert.setContentText("Min cannot be higher than Max");
//            alert.showAndWait();
//            return true;
//        }
//
//        if (inv < min || inv > max) {
//            alert.setContentText("Inv must be an integer between Min and Max");
//            alert.showAndWait();
//            return true;
//        }

        return false;
    }
}
