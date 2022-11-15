package hr.algebra.codenames.controller;

import hr.algebra.codenames.CodenamesApplication;
import hr.algebra.codenames.gameutils.CardColorGenerator;
import hr.algebra.codenames.model.Card;
import hr.algebra.codenames.model.GameHolder;
import hr.algebra.codenames.model.Player;
import hr.algebra.codenames.model.Team;
import hr.algebra.codenames.model.enums.CardColor;
import hr.algebra.codenames.model.enums.PlayerRole;
import hr.algebra.codenames.model.singleton.GameLogger;
import hr.algebra.codenames.model.singleton.GameSettings;
import hr.algebra.codenames.model.singleton.GameState;
import hr.algebra.codenames.model.singleton.ScoreManager;
import hr.algebra.codenames.repository.factories.RepositoryFactory;
import hr.algebra.codenames.repository.implementations.FileRepository;
import hr.algebra.codenames.utils.DialogUtils;
import hr.algebra.codenames.utils.FXMLLoaderUtils;
import hr.algebra.codenames.utils.Ldocs.LDocs;
import hr.algebra.codenames.utils.Ldocs.LDocsFormattingUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class StartGameController {
    @FXML
    private TextField tfRedOperativeName;
    @FXML
    private TextField tfRedSpymasterName;
    @FXML
    private TextField tfBlueOperativeName;
    @FXML
    private TextField tfBlueSpymasterName;
    @FXML
    private Button btnStartGame;

    @FXML
    private void logsClicked(ActionEvent actionEvent) {
        try {
            FXMLLoaderUtils.loadScene(new Stage(), GameSettings.LOGS_TITLE, GameSettings.LOGS_VIEW_PATH);
        } catch (IOException e) {
            DialogUtils.showFatalErrorDialog();
        }
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

    public void btnStartGameClick() {
        String ldocs = LDocs.generateLDocs(
                "Codenames Java Edition",
                "Leon Krušlin",
                "v1.1",
                HighscoreGameController.class,
                LogsGameController.class,
                OperativeGameController.class,
                SpymasterGameController.class,
                StartGameController.class,
                WinnerGameController.class,
                CardColorGenerator.class,
                CardColor.class,
                PlayerRole.class,
                GameLogger.class,
                GameSettings.class,
                GameState.class,
                ScoreManager.class,
                Card.class,
                Player.class,
                Team.class,
                RepositoryFactory.class,
                FileRepository.class,
                DialogUtils.class,
                LDocsFormattingUtils.class,
                FXMLLoaderUtils.class,
                LDocs.class,
                CodenamesApplication.class);
        try {
            RepositoryFactory.getRepository().writeReflectionDocs(ldocs);
        } catch (IOException e) {
            DialogUtils.showFatalErrorDialog();
        }
        if (!formValid()) {
            // TODO: 17.10.2022. Disable start game button instead of showing dialog window
            DialogUtils.showInformationDialog("Codenames", "Game can be played only by 4 players", "Please enter all player's names");
            return;
        }
        startGame();
    }

    private boolean formValid() {
        String redOperativeName = this.tfRedOperativeName.getText().trim();
        String redSpymasterName = this.tfRedSpymasterName.getText().trim();
        String blueOperativeName = this.tfBlueOperativeName.getText().trim();
        String blueSpymasterName = this.tfBlueSpymasterName.getText().trim();
        return !redOperativeName.isEmpty()
                && !redSpymasterName.isEmpty()
                && !blueOperativeName.isEmpty()
                && !blueSpymasterName.isEmpty();
    }

    public void startGame() {
        try {
            // TODO: 14.10.2022. Remove this abomination
            String redOperativeName = this.tfRedOperativeName.getText().trim();
            String redSpymasterName = this.tfRedSpymasterName.getText().trim();
            String blueOperativeName = this.tfBlueOperativeName.getText().trim();
            String blueSpymasterName = this.tfBlueSpymasterName.getText().trim();
            GameHolder.GAMESTATE = new GameState();
            GameHolder.GAMESTATE.initialize(redSpymasterName, redOperativeName, blueSpymasterName, blueOperativeName);
            FXMLLoaderUtils.loadScene(CodenamesApplication.getMainStage(), GameSettings.GAME_TITLE, GameSettings.SPYMASTER_VIEW_PATH);
        } catch (IOException e) {
            DialogUtils.showErrorDialog("Error", "Fatal application error", "Please restart the application");
        }
    }
}
