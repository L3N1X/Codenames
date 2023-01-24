package hr.algebra.codenames.controller;

import hr.algebra.codenames.CodenamesApplication;
import hr.algebra.codenames.model.GameHolder;
import hr.algebra.codenames.model.SerializableTurnLog;
import hr.algebra.codenames.model.TurnLog;
import hr.algebra.codenames.model.singleton.GameLogger;
import hr.algebra.codenames.model.singleton.GameSettings;
import hr.algebra.codenames.model.singleton.GameState;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class HighscoreGameController implements Initializable {

    //region FXML Controls
    @FXML
    private VBox pnlHighscoreTeam;
    @FXML
    private Label lblTeam;
    @FXML
    private Label lblPlayerOne;
    @FXML
    private Label lblPlayerTwo;
    @FXML
    private Label lblWins;
    @FXML
    private ImageView ivHighscoreTeam;
    @FXML
    private Button btnBackToGame;
    //endregion

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.initializeUI();
    }

    @FXML
    public void backToGame() {
        Stage stage = (Stage) btnBackToGame.getScene().getWindow();
        stage.close();
    }

    private void initializeUI() {
        Integer redTeamWins = 0;
        Integer blueTeamWins = 0;
        List<SerializableTurnLog> turns = GameLogger.getInstance().getTurnLogs();
        for (SerializableTurnLog log : turns) {
            if (log.getWinnerTeam() != null) {
                switch (log.getWinnerTeam().getTeamColor()) {
                    case Red -> redTeamWins++;
                    case Blue -> blueTeamWins++;
                }
            }
        }
        if (redTeamWins.equals(blueTeamWins)) {
            this.lblTeam.setText("Both teams are equal");
            this.lblWins.setText("Wins" + redTeamWins);
            this.lblPlayerOne.setText("");
            this.lblPlayerTwo.setText("");
        } else if (redTeamWins > blueTeamWins) {
            URL resource = CodenamesApplication.class.getResource(GameSettings.RED_TEAM_IMAGE_PATH);
            this.ivHighscoreTeam.setImage(new Image(resource.toString()));
            this.pnlHighscoreTeam.getStyleClass().add(GameSettings.RED_PANE_CSS);
            this.lblTeam.getStyleClass().add(GameSettings.RED_TEXT_CSS);
            this.lblPlayerOne.getStyleClass().add(GameSettings.RED_TEXT_CSS);
            this.lblPlayerTwo.getStyleClass().add(GameSettings.RED_TEXT_CSS);
            this.lblWins.getStyleClass().add(GameSettings.RED_TEXT_CSS);
            this.lblTeam.setText("Red Team");
            this.lblPlayerOne.setText(GameHolder.GAMESTATE.getValue().getRedTeam().getSpymaster().getName());
            this.lblPlayerTwo.setText(GameHolder.GAMESTATE.getValue().getRedTeam().getOperative().getName());
            this.lblWins.setText("Wins: " + redTeamWins);
        } else {
            URL resource = CodenamesApplication.class.getResource(GameSettings.BLUE_TEAM_IMAGE_PATH);
            this.ivHighscoreTeam.setImage(new Image(resource.toString()));
            this.pnlHighscoreTeam.getStyleClass().add(GameSettings.BLUE_PANE_CSS);
            this.lblTeam.getStyleClass().add(GameSettings.BLUE_TEXT_CSS);
            this.lblPlayerOne.getStyleClass().add(GameSettings.BLUE_TEXT_CSS);
            this.lblPlayerTwo.getStyleClass().add(GameSettings.BLUE_TEXT_CSS);
            this.lblWins.getStyleClass().add(GameSettings.BLUE_TEXT_CSS);
            this.lblTeam.setText("Blue Team");
            this.lblPlayerOne.setText(GameHolder.GAMESTATE.getValue().getBlueTeam().getSpymaster().getName());
            this.lblPlayerTwo.setText(GameHolder.GAMESTATE.getValue().getBlueTeam().getOperative().getName());
            this.lblWins.setText("Wins: " + redTeamWins);
        }
    }
}
