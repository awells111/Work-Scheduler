package main.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import main.Main;

import java.io.IOException;
import java.util.ResourceBundle;

public class StyledScene {

    private static final String PATH_CSS = "resources/style.css";

    private FXMLLoader loader;

    public StyledScene(String fxml, ResourceBundle resourceBundle) {
        this.loader = new FXMLLoader(Main.class.getResource(fxml), resourceBundle);
    }

    /*Create a scene that uses the resource bundle and style sheet*/
    public Scene create() throws IOException {
        AnchorPane rootLayout = loader.load();
        Scene scene = new Scene(rootLayout);
        scene.getStylesheets().add(StyledScene.class.getResource(PATH_CSS).toExternalForm());
        return scene;
    }

    public FXMLLoader getLoader() {
        return loader;
    }
}
