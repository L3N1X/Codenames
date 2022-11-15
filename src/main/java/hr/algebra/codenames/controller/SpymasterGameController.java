package hr.algebra.codenames.controller;

import hr.algebra.codenames.CodenamesApplication;
import hr.algebra.codenames.model.Card;
import hr.algebra.codenames.model.GameHolder;
import hr.algebra.codenames.model.Player;
import hr.algebra.codenames.model.enums.CardColor;
import hr.algebra.codenames.model.enums.PlayerRole;
import hr.algebra.codenames.model.singleton.GameLogger;
import hr.algebra.codenames.model.singleton.GameState;
import hr.algebra.codenames.model.singleton.GameSettings;
import hr.algebra.codenames.utils.DialogUtils;
import hr.algebra.codenames.utils.FXMLLoaderUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;


public class SpymasterGameController {

    //region FXML controls
    @FXML
    private Label lblRedOperative;
    @FXML
    private Label lblRedSpymaster;
    @FXML
    private Label lblBlueOperative;
    @FXML
    private Label lblBlueSpymaster;
    @FXML
    private Label lblCountdown;
    @FXML
    private Label lblCardCount;
    @FXML
    private Text lblRedPoints;
    @FXML
    private Text lblBluePoints;
    @FXML
    private TextField tfClue;
    @FXML
    private GridPane cardsGrid;
    @FXML
    private Button btnClue;
    //endregion

    //region Private variables
    private HashMap<String, Card> cardsMap;
    private int secondsLeft = GameSettings.SPYMASTER_TURN_DURATION;
    private Integer selectedWordCount;
    private List<String> selectedWords;
    //endregion

    //region Event handlers
    @FXML
    protected void initialize() {
        this.btnClue.setDisable(true);
        initializeLabels();
        updateCardCounter(0);
        initializeCards();
        startCountdown();
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
                GameHolder.GAMESTATE = (GameState) in.readObject();
                in.close();
                fs.close();
                if(GameHolder.GAMESTATE.isOperativeTurn()){
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
    private void highscoreClicked(ActionEvent actionEvent) {
        try {
            FXMLLoaderUtils.loadScene(new Stage(), GameSettings.HIGHSCORE_TITLE, GameSettings.HIGHSCORE_VIEW_PATH);
        } catch (IOException e) {
            DialogUtils.showFatalErrorDialog();
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
    private void giveClueClick() {
        goToOperative();
    }

    @FXML
    private void clueTyped() {
        checkClueEnabled();
    }

    private void cardSelected(ActionEvent actionEvent) {
        Button selectedCard = (Button) actionEvent.getSource();
        //Spymaster selected card that is already guessed
        if (selectedCard.getText().isBlank())
            return;
        //Spymaster has selected killer or passanger
        if (cardsMap.get(selectedCard.getText()).getColor() == CardColor.Killer
                || cardsMap.get(selectedCard.getText()).getColor() == CardColor.Passanger)
            return;
        //Spymaster has selected opposite team's card
        if (cardsMap.get(selectedCard.getText()).getColor() != GameHolder.GAMESTATE.getCurrentTeam().getTeamColor())
            return;
        //Spymaster has selected already selected card (he unselects it)
        if (selectedCard.getStyleClass().contains(GameSettings.CARD_SELECTED_CSS)) {
            selectedCard.getStyleClass().removeAll(GameSettings.CARD_SELECTED_CSS);
            this.updateCardCounter(-1);
            this.selectedWords.remove(selectedCard.getText());
            checkClueEnabled();
            return;
        }
        //Spymaster selects a valid card
        selectedCard.getStyleClass().add(GameSettings.CARD_SELECTED_CSS);
        this.selectedWords.add(selectedCard.getText());
        this.updateCardCounter(1);
        checkClueEnabled();
    }
    //endregion

    //region Initialization
    private void initializeLabels() {
        this.lblRedOperative.setText(GameHolder.GAMESTATE.getRedTeam().getOperative().getName());
        this.lblRedSpymaster.setText(GameHolder.GAMESTATE.getRedTeam().getSpymaster().getName());
        this.lblBlueOperative.setText(GameHolder.GAMESTATE.getBlueTeam().getOperative().getName());
        this.lblBlueSpymaster.setText(GameHolder.GAMESTATE.getBlueTeam().getSpymaster().getName());

        if (GameHolder.GAMESTATE.getCurrentTeam().getTeamColor() == CardColor.Red)
            this.lblRedSpymaster.setText(this.lblRedSpymaster.getText() + " (YOU)");
        else
            this.lblBlueSpymaster.setText(this.lblBlueSpymaster.getText() + " (YOU)");

        this.lblRedPoints.setText(String.valueOf(GameHolder.GAMESTATE.getRedTeam().getPoints()));
        this.lblBluePoints.setText(String.valueOf(GameHolder.GAMESTATE.getBlueTeam().getPoints()));
    }

    private void initializeCards() {
        List<Button> physicalCards = new ArrayList<>();
        for (Node component : this.cardsGrid.getChildren()) {
            if (component instanceof Button) {
                physicalCards.add((Button) component);
                ((Button) component).setOnAction(this::cardSelected);
            }
        }
        this.cardsMap = GameHolder.GAMESTATE.getCardsMap();
        int i = 0;
        for (Card card : cardsMap.values()) {
            Button physicalCard = physicalCards.get(i);
            physicalCard.setText(card.getWord());
            physicalCard.getStyleClass().clear();
            if (card.getIsGuessed()) {
                physicalCard.setText("");
                if (card.getColor() == CardColor.Red)
                    physicalCard.getStyleClass().addAll(GameSettings.CARD_RED_GUESSED_CSS);
                else if (card.getColor() == CardColor.Blue)
                    physicalCard.getStyleClass().addAll(GameSettings.CARD_BLUE_GUESSED_CSS);
                else if (card.getColor() == CardColor.Passanger)
                    physicalCard.getStyleClass().addAll(GameSettings.CARD_PASSANGER_GUESSED_CSS);
                else if (card.getColor() == CardColor.Killer)
                    physicalCard.getStyleClass().addAll(GameSettings.CARD_KILLER_GUESSED_CSS);
            } else {
                if (card.getColor() == CardColor.Red)
                    physicalCard.getStyleClass().addAll(GameSettings.CARD_RED_CSS);
                else if (card.getColor() == CardColor.Blue)
                    physicalCard.getStyleClass().addAll(GameSettings.CARD_BLUE_CSS);
                else if (card.getColor() == CardColor.Passanger)
                    physicalCard.getStyleClass().addAll(GameSettings.CARD_PASSANGER_CSS);
                else if (card.getColor() == CardColor.Killer)
                    physicalCard.getStyleClass().addAll(GameSettings.CARD_KILLER_CSS);
            }
            physicalCard.setTextAlignment(TextAlignment.CENTER);
            i++;
        }
    }
    //endregion

    public SpymasterGameController() {
        this.selectedWords = new ArrayList<>();
        this.selectedWordCount = 0;
    }

    private void goToOperative() {
        try {
            this.setSecondsLeftToZero();
            GameHolder.GAMESTATE.toOperatorTurn(this.tfClue.getText(), this.selectedWordCount);
            FXMLLoaderUtils.loadScene(CodenamesApplication.getMainStage(),
                    GameSettings.GAME_TITLE,
                    GameSettings.OPERATIVE_VIEW_PATH);
        } catch (IOException e) {
            DialogUtils.showFatalErrorDialog();
        }
    }

    private void goToNextTurnTimeout() {
        this.setSecondsLeftToZero();
        GameHolder.GAMESTATE.toNextTeamTurn(new ArrayList<>());
        try {
            FXMLLoaderUtils.loadScene(CodenamesApplication.getMainStage(),
                    GameSettings.GAME_TITLE,
                    GameSettings.SPYMASTER_VIEW_PATH);
        } catch (IOException e) {
            DialogUtils.showFatalErrorDialog();
        }
    }

    private void setSecondsLeftToZero() {
        this.secondsLeft = 0;
    }

    private void checkClueEnabled() {
        String clue = this.tfClue.getText().trim();
        this.btnClue.setDisable(clue.isBlank() || this.selectedWordCount == 0);
    }

    private void updateCardCounter(Integer delta) {
        this.selectedWordCount += delta;
        this.lblCardCount.setText(this.selectedWordCount.toString());
    }

    private void updateCountdownLabel() {
        lblCountdown.setText(secondsLeft + "s");
        if (secondsLeft <= 15)
            lblCountdown.getStyleClass().add(GameSettings.RED_TEXT_CSS);
        if (secondsLeft == 0) {
            goToNextTurnTimeout();
        }
    }

    private void startCountdown() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    if (secondsLeft == 0)
                        cancel();
                    secondsLeft--;
                    updateCountdownLabel();
                });
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask, 0, 1000);
    }
}
