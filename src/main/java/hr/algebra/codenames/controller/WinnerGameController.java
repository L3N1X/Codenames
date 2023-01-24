package hr.algebra.codenames.controller;

import hr.algebra.codenames.CodenamesApplication;
import hr.algebra.codenames.model.GameHolder;
import hr.algebra.codenames.model.SerializableTurnLog;
import hr.algebra.codenames.model.TurnLog;
import hr.algebra.codenames.model.enums.CardColor;
import hr.algebra.codenames.model.singleton.GameLogger;
import hr.algebra.codenames.model.singleton.GameSettings;
import hr.algebra.codenames.model.singleton.GameState;
import hr.algebra.codenames.utils.DialogUtils;
import hr.algebra.codenames.utils.FXMLLoaderUtils;
import hr.algebra.codenames.xml.jaxb.XMLConverter;
import hr.algebra.codenames.xml.model.XmlTurnListWrapper;
import hr.algebra.codenames.xml.model.XmlTurnLog;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WinnerGameController {

    //region private FXML
    @FXML
    private ImageView ivWinner;
    @FXML
    private VBox pnlWinner;
    @FXML
    private Label lblWinnerSpymaster;
    @FXML
    private Label lblWinnerOperative;
    @FXML
    private Label lblSpymasterTitle;
    @FXML
    private Label lblOperativeTitle;
    //endregion

    @FXML
    private void initialize() {
        List<SerializableTurnLog> turnLogs = GameLogger.getInstance().getTurnLogs();
        XmlTurnListWrapper turnListWrapper = new XmlTurnListWrapper(turnLogs);
        try {
            System.out.println(XMLConverter.ConvertToXml(turnListWrapper));
        } catch (Exception e) {
            e.printStackTrace();
        }
        initializeUI();
    }

    @FXML
    private void loadGame(ActionEvent actionEvent) {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Codenames saves", "*.ser"));
        File file = chooser.showOpenDialog(null);
        if(file != null){
            try {
                FileInputStream fs = new FileInputStream(file);
                ObjectInputStream in = new ObjectInputStream(fs);
                // Method for deserialization of object
                GameHolder.GAMESTATE.setValue((GameState) in.readObject());
                in.close();
                fs.close();
                if(GameHolder.GAMESTATE.getValue().isOperativeTurn()){
                    FXMLLoaderUtils.loadScene(CodenamesApplication.getMainStage(),
                            GameSettings.GAME_TITLE,
                            GameSettings.OPERATIVE_VIEW_PATH);
                } else {
                    FXMLLoaderUtils.loadScene(CodenamesApplication.getMainStage(),
                            GameSettings.GAME_TITLE,
                            GameSettings.SPYMASTER_VIEW_PATH);
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    private void saveGame(ActionEvent actionEvent) {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Codenames saves", "*.ser"));
        File file = chooser.showSaveDialog(null);
        if(file != null){
            try {
                FileOutputStream fs = new FileOutputStream(file.getAbsolutePath());
                ObjectOutputStream out = new ObjectOutputStream(fs);
                out.writeObject(GameHolder.GAMESTATE);
                out.close();
            } catch (Exception e) {
                DialogUtils.showErrorDialog("Error",
                        "Unable to save the game.",
                        "Please try saving to different location");
            }
        }
    }

    @FXML
    private void logsClicked(ActionEvent actionEvent) {
        try {
            FXMLLoaderUtils.loadScene(new Stage(), GameSettings.LOGS_TITLE, GameSettings.LOGS_VIEW_PATH);
        } catch (IOException e) {
            DialogUtils.showFatalErrorDialog();
        }
    }

    @FXML
    private void highscoreClicked(ActionEvent actionEvent) {
        try {
            FXMLLoaderUtils.loadScene(new Stage(), GameSettings.HIGHSCORE_TITLE, GameSettings.HIGHSCORE_VIEW_PATH);
        } catch (IOException e) {
            DialogUtils.showFatalErrorDialog();
        }
    }

    @FXML
    private void playAgain(ActionEvent actionEvent) {
        try {
            GameHolder.GAMESTATE.getValue().reinitialize();
            FXMLLoaderUtils.loadScene(CodenamesApplication.getMainStage(),
                    GameSettings.GAME_TITLE,
                    GameSettings.SPYMASTER_VIEW_PATH);
        } catch (IOException e) {
            DialogUtils.showFatalErrorDialog();
        }
    }

    @FXML
    private void exit(ActionEvent actionEvent) {
        Platform.exit();
    }

    private void initializeUI() {
        File file;
        String winnerTeamImagePath;
        if (GameHolder.GAMESTATE.getValue().getWinnerTeam().getTeamColor() == CardColor.Red) {
            file = new File(GameSettings.RED_TEAM_IMAGE_PATH);
            this.lblSpymasterTitle.getStyleClass().add(GameSettings.RED_TEXT_CSS);
            this.lblOperativeTitle.getStyleClass().add(GameSettings.RED_TEXT_CSS);
            this.pnlWinner.getStyleClass().add(GameSettings.RED_PANE_CSS);
            winnerTeamImagePath = GameSettings.RED_TEAM_IMAGE_PATH;
        } else {
            file = new File(GameSettings.BLUE_TEAM_IMAGE_PATH);
            this.lblSpymasterTitle.getStyleClass().add(GameSettings.BLUE_TEXT_CSS);
            this.lblOperativeTitle.getStyleClass().add(GameSettings.BLUE_TEXT_CSS);
            this.pnlWinner.getStyleClass().add(GameSettings.BLUE_PANE_CSS);
            winnerTeamImagePath = GameSettings.BLUE_TEAM_IMAGE_PATH;
        }
        this.lblWinnerOperative.setText(GameHolder.GAMESTATE.getValue().getWinnerTeam().getOperative().getName());
        this.lblWinnerSpymaster.setText(GameHolder.GAMESTATE.getValue().getWinnerTeam().getSpymaster().getName());
        URL resource = CodenamesApplication.class.getResource(winnerTeamImagePath);
        this.ivWinner.setImage(new Image(resource.toString()));
    }
}
