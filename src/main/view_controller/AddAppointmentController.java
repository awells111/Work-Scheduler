package main.view_controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Main;
import main.data.Database;
import main.model.Appointment;
import main.view.DateTimePicker;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

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

    @FXML
    private ResourceBundle resources;

    private Stage dialogStage;
    private Database database;
    private Appointment appointment;

    private Main mainApp;

    /*MYSQL character limits by column*/
    private static final int LIMIT_TYPE = 255;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;

    }

    public void setAppointment(Database database, String customerName, Appointment appointment) {
        this.database = database;
        this.appointment = appointment;

        setFields();

        /*Set fields in the controller*/
        labelCustomerName.setText(customerName);
        textFieldAppointmentType.setText(appointment.getType());
        appointmentDateTimePickerStart.setDateTimeValue(appointment.getStart());
        appointmentDateTimePickerEnd.setDateTimeValue(appointment.getEnd());
    }

    @FXML
    void handleAppointmentSave() {
        if (inputError()) {
            return;
        }

        int id = appointment.getId();
        int custId = appointment.getCustomerId();
        String type = textFieldAppointmentType.getText();
        LocalDateTime start = appointmentDateTimePickerStart.getDateTimeValue();
        LocalDateTime end = appointmentDateTimePickerEnd.getDateTimeValue();

        Appointment newAppointment = new Appointment(id, custId, type, start, end);

        if (appointmentOverlaps(newAppointment)) {
            return;
        }

        try {
            /*If Appointment does not exist in the database, else*/
            if (appointment.getId() == Integer.MIN_VALUE) {
                database.addAppointment(newAppointment);
            } else {
                database.updateAppointment(appointment, newAppointment);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            mainApp.showDatabaseErrorAlert(resources.getString("Error_Saving_Appointment"));
            return;
        }

        dialogStage.close();
    }

    @FXML
    void handleAppointmentCancel() {
        dialogStage.close();
    }

    /*Returns false if there are no errors in saving the appointment*/
    private boolean inputError() {
        Alert alert = buildAlert();

        //Display alert for incorrect inputs
        if (textFieldAppointmentType.getText().equals("")) {
            alert.setContentText(resources.getString("Type_cannot_be_empty"));
            alert.show();
            return true;
        }

        if (database.isOutsideBusinessHours(appointmentDateTimePickerStart.getDateTimeValue()) ||
                database.isOutsideBusinessHours(appointmentDateTimePickerEnd.getDateTimeValue())) {
            alert.setContentText(resources.getString("not_between_business_hours"));
            alert.show();
            return true;
        }

        LocalDateTime start = appointmentDateTimePickerStart.getDateTimeValue();
        LocalDateTime end = appointmentDateTimePickerEnd.getDateTimeValue();

        if (end.isBefore(start) || end.equals(start)) {
            alert.setContentText(resources.getString("end_not_after_start"));
            alert.show();
            return true;
        }

        return false;
    }

    private Alert buildAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(resources.getString("Error_Saving_Appointment"));
        alert.setHeaderText(null);

        return alert;
    }

    private boolean appointmentOverlaps(Appointment newAppointment) {
        try {
            if (database.appointmentOverlaps(newAppointment)) {
                Alert alert = buildAlert();
                alert.setContentText(resources.getString("appointment_overlaps"));
                alert.show();
                return true;
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            mainApp.showDatabaseErrorAlert();
            return true;
        }

        return false;
    }

    private void setFields() {
        /*Only Accept Letters*/
        textFieldAppointmentType.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\sa-zA-Z*")) {
                textFieldAppointmentType.setText(newValue.replaceAll("[^\\sa-zA-Z]", ""));
            }
        });

        setLimit(textFieldAppointmentType, LIMIT_TYPE);
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

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }
}
