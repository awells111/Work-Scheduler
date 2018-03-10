package main.view_controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.data.Database;
import main.model.Appointment;

import static main.data.Database.CODE_NEW_ENTITY;

public class AddAppointmentController {
    public static final String FXML_ADD_APPOINTMENT = "view_controller/add_appointment.fxml";

    @FXML
    private TextField textFieldAppointmentType;

    @FXML
    private TextField textFieldAppointmentStart; //Todo switch to datetime or something

    @FXML
    private TextField textFieldAppointmentEnd;

    private Stage dialogStage;
    private Database database;
    private Appointment appointment;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setAppointment(Database database, Appointment appointment) {
        this.database = database;
        this.appointment = appointment;

        textFieldAppointmentType.setText(appointment.getType());
        textFieldAppointmentStart.setText(appointment.getStart());
        textFieldAppointmentEnd.setText(appointment.getEnd());
    }

    @FXML
    void handleAppointmentSave() {
        if (errorBeforeSave()) { //Check for errors like empty fields
            return;
        }

        int id = (isNewAppointment()) ? database.nextAppointmentId() : appointment.getId();
        int custId = appointment.getCustomerId();
        String type = textFieldAppointmentType.getText();
        String start = textFieldAppointmentStart.getText();
        String end = textFieldAppointmentEnd.getText();

        Appointment newAppointment = new Appointment(id, custId, type, start, end);

        if (isNewAppointment()) {
            database.addAppointment(newAppointment);
        } else {
            database.updateAppointment(appointment, newAppointment);
        }

        dialogStage.close();
    }

    @FXML
    void handleAppointmentCancel() {
        dialogStage.close();
    }

    /*Returns true if appointment does not exist in the database*/
    private boolean isNewAppointment() {
        return appointment.getId() == CODE_NEW_ENTITY;
    }

    /*Returns false if there are no errors in saving the appointment*/
    private boolean errorBeforeSave() {
        return errorBeforeSave(false);
    }

    private boolean errorBeforeSave(boolean error) {

        if (error) {

        }

//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setType("Error Saving Part");
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
