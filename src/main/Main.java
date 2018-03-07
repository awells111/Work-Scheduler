package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.view_controller.LoginController;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import static main.view_controller.LoginController.FXML_LOGIN;

public class Main extends Application {

    public static final String PATH_RB = "main.rb";

    private Stage primaryStage;

    private ResourceBundle rb;

    public Main() {
        //todo Required for A. Log-in Form **Requires import java.util.Locale;
        //Locale.setDefault(new Locale("fr"));
        //Locale.setDefault(new Locale("es"));

        rb = ResourceBundle.getBundle(PATH_RB);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        getPrimaryStage().setTitle("Work Scheduler");

        initRootLayout();

    }

    private void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(FXML_LOGIN), rb);
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
