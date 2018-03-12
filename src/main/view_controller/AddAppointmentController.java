package main.view_controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.Main;
import main.data.Database;
import main.model.Appointment;
import main.view.DateTimePicker;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;

import static main.data.Database.CODE_NEW_ENTITY;

public class AddAppointmentController {
    public static final String FXML_ADD_APPOINTMENT = "view_controller/add_appointment.fxml";

    @FXML
    private Label labelCustomerName;

    @FXML
    private TextField textFieldAppointmentType;

    @FXML
    private DateTimePicker appointmentDateTimePickerStart;

    @FXML
    private DateTimePicker appointmentDateTimePickerEnd;

    private Stage dialogStage;
    private Database database;
    private Appointment appointment;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setAppointment(Database database, String customerName, Appointment appointment) {
        this.database = database;
        this.appointment = appointment;

        /*Set fields in the controller*/
        labelCustomerName.setText(customerName);
        textFieldAppointmentType.setText(appointment.getType());
        appointmentDateTimePickerStart.setDateTimeValue(database.localDateTimeFromString(appointment.getStart()));
        appointmentDateTimePickerEnd.setDateTimeValue(database.localDateTimeFromString(appointment.getStart()));
    }

    @FXML
    void handleAppointmentSave() {
        try {
            inputError();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        int id = (isNewAppointment()) ? database.nextAppointmentId() : appointment.getId();
        int custId = appointment.getCustomerId();
        String type = textFieldAppointmentType.getText();
        String startTime = appointmentDateTimePickerStart.getFormattedString();
        String endTime = appointmentDateTimePickerEnd.getFormattedString();

        Appointment newAppointment = new Appointment(id, custId, type, startTime, endTime);

        try {
            appointmentOverlaps(newAppointment);
        } catch (Exception e) {
            e.printStackTrace();
        }

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
    private void inputError() throws Exception {
        Alert alert = buildAlert();

        //Display alert for incorrect inputs
        if (textFieldAppointmentType.getText().equals("")) {
            alert.setContentText("Type cannot be empty");
            alert.show();
            throw new Exception("Type cannot be empty");
        }

        if (database.isOutsideBusinessHours(appointmentDateTimePickerStart.getDateTimeValueGMT().getHour()) ||
                database.isOutsideBusinessHours(appointmentDateTimePickerEnd.getDateTimeValueGMT().getHour())) {
            alert.setContentText("Appointment must be between 8:00 and 22:00 GMT.");
            alert.show();
            throw new Exception("Appointment must be between 8:00 and 22:00 GMT.");
        }

        //todo finish this, will have to pass through a list of all appointments in the current customer
        //If start date is earlier than now
        //If end date is before start date OR equals start date
        /*Only check after all inputs are checked*/
    }

    private Alert buildAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error Saving Appointment");
        alert.setHeaderText(null);

        return alert;
    }

    private void appointmentOverlaps(Appointment newAppointment) throws Exception {
        if (database.appointmentOverlaps(newAppointment)) {

            Alert alert = buildAlert();
            alert.setContentText("This appointment overlaps one or more existing appointments.");
            alert.show();
            throw new Exception("This appointment overlaps one or more existing appointments.");
        }
    }
}
