package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.view_controller.LoginController;

import java.io.IOException;

import static main.view_controller.LoginController.FXML_LOGIN;

public class Main extends Application {

    private Stage primaryStage;

    public Main() {

    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        getPrimaryStage().setTitle("Work Scheduler");

        initRootLayout();

    }

    private void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource(FXML_LOGIN));
            AnchorPane rootLayout = loader.load();
            Scene scene = new Scene(rootLayout);
            getPrimaryStage().setScene(scene);
            getPrimaryStage().show();
            LoginController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException var5) {
            var5.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch(args);
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}
