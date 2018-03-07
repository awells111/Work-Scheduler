package main.view_controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import main.Main;
import main.data.DbConnection;

public class OverviewController {
    public static final String FXML_OVERVIEW = "view_controller/overview.fxml";

    @FXML
    private Button calendarButton;

    private Main mainApp;
    private DbConnection dbConnection;

    public OverviewController() {
        dbConnection = new DbConnection();
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    void handleShowCalendar() {
        mainApp.showCalendar();
    }
}
