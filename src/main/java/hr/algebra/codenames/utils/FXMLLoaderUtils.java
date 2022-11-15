package hr.algebra.codenames.utils;

import hr.algebra.codenames.CodenamesApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class FXMLLoaderUtils {
    private FXMLLoaderUtils(){}
    public static void loadScene(Stage stage, String title, String fxmlPath) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(CodenamesApplication.class.getResource(fxmlPath));
        Scene scene = null;
        scene = new Scene(fxmlLoader.load(), 1500, 900);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }
}
