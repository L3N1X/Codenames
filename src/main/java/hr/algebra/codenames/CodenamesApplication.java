package hr.algebra.codenames;

import hr.algebra.codenames.controller.StartGameController;
import hr.algebra.codenames.model.singleton.GameSettings;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class CodenamesApplication extends Application {

    private static Stage mainStage;

    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(CodenamesApplication.class.getResource(GameSettings.START_VIEW_PATH));
        Scene scene = new Scene(fxmlLoader.load(), 1500, 900);
        stage.setTitle(GameSettings.GAME_TITLE);
        stage.setScene(scene);
        stage.show();
    }

    public static Stage getMainStage() {
        return mainStage;
    }

    public static void main(String[] args) {
        launch();
    }
}