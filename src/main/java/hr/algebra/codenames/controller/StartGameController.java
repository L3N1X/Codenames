package hr.algebra.codenames.controller;

import hr.algebra.codenames.CodenamesApplication;
import hr.algebra.codenames.model.singleton.GameState;
import hr.algebra.codenames.repository.implementations.MemoryWordRepository;
import hr.algebra.codenames.repository.interfaces.WordRepository;
import hr.algebra.codenames.utils.DialogUtils;
import hr.algebra.codenames.utils.FXMLLoaderUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.util.List;

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

    public void btnStartGameClick(){
        if(!formValid()){
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

    public void startGame(){
        try {
            // TODO: 14.10.2022. Remove this abomination
            String redOperativeName = this.tfRedOperativeName.getText().trim();
            String redSpymasterName = this.tfRedSpymasterName.getText().trim();
            String blueOperativeName = this.tfBlueOperativeName.getText().trim();
            String blueSpymasterName = this.tfBlueSpymasterName.getText().trim();
            GameState.initialize(redSpymasterName, redOperativeName, blueSpymasterName, blueOperativeName);
            FXMLLoaderUtils.loadScreen(CodenamesApplication.getMainStage(), "Codenames Java Edition", "view/spymasterGameScreen.fxml");
        } catch (IOException e) {
            DialogUtils.showErrorDialog("Error", "Fatal application error", "Please restart the application");
        }
    }
}
