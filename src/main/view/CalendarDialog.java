package main.view;

import com.sun.javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CalendarDialog extends DatePickerSkin {

    public CalendarDialog(DatePicker datePicker) {
        super(datePicker);

        datePicker.setShowWeekNumbers(true); //Our calendar will always show week numbers
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
