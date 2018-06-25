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
import main.data.Database;
import main.model.Appointment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.MonthDay;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import static main.Main.PATH_RB;

public class CalendarDialog extends DatePickerSkin {

    private HashMap<MonthDay, List<Integer>> appointmentHashMap;

    public CalendarDialog(ObservableList<Appointment> appointments) {
        this(appointments, new DatePicker());
    }

    private CalendarDialog(ObservableList<Appointment> appointments, DatePicker datePicker) {
        super(datePicker);

        datePicker.setShowWeekNumbers(true); //Our calendar will always show week numbers

        addText(appointments, datePicker);

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
                    List<Integer> list = appointmentHashMap.get(MonthDay.from(date));

                    for (int i = 0; i < list.size(); ) {
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
        ResourceBundle rb = ResourceBundle.getBundle(PATH_RB);

        BorderPane root = new BorderPane();
        Node popupContent = this.getPopupContent();
        root.setCenter(popupContent);

        // Create the dialog Stage.
        Stage dialogStage = new Stage();
        dialogStage.setTitle(rb.getString("calendar_title"));
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(window);
        Scene scene = new Scene(root, root.getPrefWidth(), root.getPrefHeight());
        dialogStage.setScene(scene);
        dialogStage.showAndWait();
    }

}
