package hr.algebra.codenames.controller;

import hr.algebra.codenames.CodenamesApplication;
import hr.algebra.codenames.model.enums.CardColor;
import hr.algebra.codenames.model.singleton.GameSettings;
import hr.algebra.codenames.model.singleton.GameState;
import hr.algebra.codenames.utils.DialogUtils;
import hr.algebra.codenames.utils.FXMLLoaderUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class WinnerGameController {

    @FXML
    private ImageView ivWinner;
    @FXML
    private VBox pnlWinner;
    @FXML
    private  Label lblWinnerSpymaster;
    @FXML
    private Label lblWinnerOperative;
    @FXML
    private  Label lblSpymasterTitle;
    @FXML
    private Label lblOperativeTitle;


    @FXML
    private void initialize() {
        if(GameState.getWinnerTeam().getTeamColor() == CardColor.Red){
            this.lblSpymasterTitle.getStyleClass().add(GameSettings.RED_TEXT_CSS);
            this.lblOperativeTitle.getStyleClass().add(GameSettings.RED_TEXT_CSS);
            this.pnlWinner.getStyleClass().add(GameSettings.RED_PANE_CSS);
        } else {
            this.lblSpymasterTitle.getStyleClass().add(GameSettings.BLUE_TEXT_CSS);
            this.lblOperativeTitle.getStyleClass().add(GameSettings.BLUE_TEXT_CSS);
            this.pnlWinner.getStyleClass().add(GameSettings.BLUE_PANE_CSS);
        }
    }

    @FXML
    private void playAgain(ActionEvent actionEvent){

    }

    @FXML
    private void exit(ActionEvent actionEvent){

    }
}
