package main.view_controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.data.Database;
import main.model.Appointment;
import main.view.DateTimePicker;
import main.view.NumberFieldConverter;

import java.time.LocalDateTime;

import static main.data.Database.CODE_NEW_ENTITY;

public class AddAppointmentController {
    public static final String FXML_ADD_APPOINTMENT = "view_controller/add_appointment.fxml";

    @FXML
    private Label labelCustomerName;

    @FXML
    private TextField textFieldAppointmentType;

    @FXML
    private DateTimePicker appointmentDateTimePicker;

    @FXML
    private TextField appointmentDuration;

    private Stage dialogStage;
    private Database database;
    private Appointment appointment;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setAppointment(Database database, String customerName, Appointment appointment) {
        this.database = database;
        this.appointment = appointment;

        labelCustomerName.setText(customerName);

        textFieldAppointmentType.setText(appointment.getType());

        appointmentDuration.setTextFormatter(new NumberFieldConverter().getFormatter()); //appointmentDuration will now only accept numbers as input


        if (isNewAppointment()) {
            appointmentDateTimePicker.setDateTimeValue(LocalDateTime.now());
        } else {
            appointmentDateTimePicker.setDateTimeValue(database.dateTimeFromString(appointment.getStart()));
        }

        appointmentDateTimePicker = new DateTimePicker();

    }

    @FXML
    void handleAppointmentSave() {
        int id = (isNewAppointment()) ? database.nextAppointmentId() : appointment.getId();
        int custId = appointment.getCustomerId();
        String type = textFieldAppointmentType.getText();

        String startTime = appointmentDateTimePicker.getFormattedString();
        String endTime = appointmentDateTimePicker.getFormattedString(); //todo CHANGE

        //todo change this
        Appointment newAppointment = new Appointment(id, custId, type, startTime, endTime);

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
        //todo the duration must be 1 or more minutes
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
