package hr.algebra.codenames.controller;

import hr.algebra.codenames.CodenamesApplication;
import hr.algebra.codenames.model.GameHolder;
import hr.algebra.codenames.model.enums.CardColor;
import hr.algebra.codenames.model.singleton.GameSettings;
import hr.algebra.codenames.model.singleton.GameState;
import hr.algebra.codenames.utils.DialogUtils;
import hr.algebra.codenames.utils.FXMLLoaderUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

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
        initializeUI();
    }

    @FXML
    private void loadGame(ActionEvent actionEvent) {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Codenames saves", "*.ser"));
        File file = chooser.showOpenDialog(null);
        if(file != null)
            DialogUtils.showInformationDialog("FILE", "You have selected a file", file.getAbsolutePath());
    }

    @FXML
    private void saveGame(ActionEvent actionEvent) {

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
            GameHolder.GAMESTATE.reinitialize();
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
        if (GameHolder.GAMESTATE.getWinnerTeam().getTeamColor() == CardColor.Red) {
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
        this.lblWinnerOperative.setText(GameHolder.GAMESTATE.getWinnerTeam().getOperative().getName());
        this.lblWinnerSpymaster.setText(GameHolder.GAMESTATE.getWinnerTeam().getSpymaster().getName());
        URL resource = CodenamesApplication.class.getResource(winnerTeamImagePath);
        this.ivWinner.setImage(new Image(resource.toString()));
    }
}
