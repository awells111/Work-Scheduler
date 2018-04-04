package main.view;

import com.sun.javafx.scene.control.skin.DatePickerSkin;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import main.data.Database;
import main.model.Appointment;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.MonthDay;
import java.util.ArrayList;
import java.util.HashMap;

public class CalendarDialog extends DatePickerSkin {

    private HashMap<MonthDay, ArrayList<Integer>> appointmentHashMap;

    public CalendarDialog(Database database, DatePicker datePicker) {
        super(datePicker);

        ObservableList<Appointment> appointments = database.getAppointments();

        datePicker.setShowWeekNumbers(true); //Our calendar will always show week numbers

        addText(database.getAppointments(), datePicker);

        appointmentHashMap = new HashMap<>();

        for (int i = 0; i < appointments.size(); i++) {
            Appointment current = appointments.get(i);
            LocalDateTime localDateTime = current.getStart();
            MonthDay monthDay = MonthDay.from(localDateTime);

            appointmentHashMap.putIfAbsent(monthDay, new ArrayList<>());
            appointmentHashMap.get(monthDay).add(i);
        }
    }

    private void addText(ObservableList<Appointment> appointments, DatePicker datePicker) {
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                if (appointmentHashMap.containsKey(MonthDay.from(date))) {
                    StringBuilder sb = new StringBuilder();
                    ArrayList<Integer> list = appointmentHashMap.get(MonthDay.from(date));

                    for (int i = 0; i < list.size();) {
                        Integer index = list.get(i);
                        sb.append(appointments.get(index).toStringUserFriendly());

                        if (i++ + 1 != list.size()) {
                            sb.append(System.lineSeparator());
                        }
                    }

                    setTooltip(new Tooltip(sb.toString()));
                    setStyle("-fx-background-color: #ff4444;");
                }
            }
        });
    }

    public void showDialog(Stage window) {
        BorderPane root = new BorderPane();
        Node popupContent = this.getPopupContent();
        root.setCenter(popupContent);

        // Create the dialog Stage.
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Calendar");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(window);
        Scene scene = new Scene(root, root.getPrefWidth(), root.getPrefHeight());
        dialogStage.setScene(scene);
        dialogStage.showAndWait();
    }

}
