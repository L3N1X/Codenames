package hr.algebra.codenames.controller;

import hr.algebra.codenames.CodenamesApplication;
import hr.algebra.codenames.model.Card;
import hr.algebra.codenames.model.GameHolder;
import hr.algebra.codenames.model.enums.CardColor;
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
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;
import java.util.Timer;

public class OperativeGameController {

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
    private Label lblSelectedWordCount;
    @FXML
    private Label lblClue;
    @FXML
    private Button btnConfirm;
    @FXML
    private GridPane cardsGrid;
    //endregion

    //region Private variables
    private HashMap<String, Card> cards;
    private int secondsLeft = GameSettings.OPERATIVE_TURN_DURATION;;
    private Integer selectedCardCount;
    private List<String> selectedWords;
    //endregion

    //region Event handlers

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
                GameHolder.GAMESTATE =  (GameState) in.readObject();
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
    private void confirmSelection(ActionEvent actionEvent){
        goToNextTurn();
    }

    private void cardSelected(ActionEvent actionEvent){
        Button selectedCard = (Button)actionEvent.getSource();
        //Operative selected already guessed card, nothing happens
        if(selectedCard.getText().isEmpty())
            return;
        //Operative clicked on already selected card, so he unselects is
        if(selectedCard.getStyleClass().contains(GameSettings.CARD_SELECTED_CSS)){
            selectedCard.getStyleClass().removeAll(GameSettings.CARD_SELECTED_CSS);
            this.updateCardCounter(-1);
            this.selectedWords.remove(selectedCard.getText());
            this.lblClue.getStyleClass().remove(GameSettings.GREEN_TEXT_CSS);
            this.lblCardCount.getStyleClass().remove(GameSettings.GREEN_TEXT_CSS);
            this.lblSelectedWordCount.getStyleClass().remove(GameSettings.GREEN_TEXT_CSS);
            this.btnConfirm.setDisable(selectedCardCount == 0);
            return;
        }
        //Operative selected same amount of cards as given, he can only unselect them
        if(selectedWordCountEqualToGiven())
            return;
        //Operative selects unselected card
        selectedCard.getStyleClass().add(GameSettings.CARD_SELECTED_CSS);
        this.selectedWords.add(selectedCard.getText());
        this.updateCardCounter(1);
        this.btnConfirm.setDisable(selectedCardCount == 0);
        //Operative selected same amount as given, turn text green
        if(selectedWordCountEqualToGiven()){
            this.lblClue.getStyleClass().add(GameSettings.GREEN_TEXT_CSS);
            this.lblCardCount.getStyleClass().add(GameSettings.GREEN_TEXT_CSS);
            this.lblSelectedWordCount.getStyleClass().add(GameSettings.GREEN_TEXT_CSS);
        }
    }

    @FXML
    protected void initialize() {
        this.selectedWords = new ArrayList<>();
        this.btnConfirm.setDisable(true);
        initializeLabels();
        updateCardCounter(0);
        initializeCards();
        startCountdown();
    }
    @FXML
    private void highscoreClicked(ActionEvent actionEvent){
        try {
            FXMLLoaderUtils.loadScene(new Stage(), GameSettings.HIGHSCORE_TITLE, GameSettings.HIGHSCORE_VIEW_PATH);
        } catch (IOException e) {
            DialogUtils.showFatalErrorDialog();
        }
    }
    @FXML
    public void logsClicked(ActionEvent actionEvent){
        try {
            FXMLLoaderUtils.loadScene(new Stage(), GameSettings.LOGS_TITLE, GameSettings.LOGS_VIEW_PATH);
        } catch (IOException e) {
            DialogUtils.showFatalErrorDialog();
        }
    }

    //endregion

    public OperativeGameController() {
        this.selectedCardCount = 0;
    }
    private boolean selectedWordCountEqualToGiven(){
        return Objects.equals(this.selectedCardCount, GameHolder.GAMESTATE.getCurrentGivenWordCount());
    }

    private void initializeLabels() {
        this.lblCardCount.setText(GameHolder.GAMESTATE.getCurrentGivenWordCount().toString());
        this.lblClue.setText(GameHolder.GAMESTATE.getCurrentTeam().getSpymaster().getName() + " gave clue:   '" + GameHolder.GAMESTATE.getCurrentClue().toUpperCase() + "'");
        this.lblRedOperative.setText(GameHolder.GAMESTATE.getRedTeam().getOperative().getName());
        this.lblRedSpymaster.setText(GameHolder.GAMESTATE.getRedTeam().getSpymaster().getName());
        this.lblBlueOperative.setText(GameHolder.GAMESTATE.getBlueTeam().getOperative().getName());
        this.lblBlueSpymaster.setText(GameHolder.GAMESTATE.getBlueTeam().getSpymaster().getName());
        if(GameHolder.GAMESTATE.getCurrentTeam().getTeamColor() == CardColor.Red)
            this.lblRedOperative.setText(this.lblRedOperative.getText() + " (YOU)");
        else
            this.lblBlueOperative.setText(this.lblBlueOperative.getText() + " (YOU)");
        this.lblRedPoints.setText(String.valueOf(GameHolder.GAMESTATE.getRedTeam().getPoints()));
        this.lblBluePoints.setText(String.valueOf(GameHolder.GAMESTATE.getBlueTeam().getPoints()));
    }

    /**
     * Initializes physical card grid
     */
    private void initializeCards() {
        List<Button> physicalCards = new ArrayList<>();
        for(Node component : this.cardsGrid.getChildren()){
            if(component instanceof Button){
                physicalCards.add((Button)component);
                ((Button)component).setOnAction(this::cardSelected);
            }
        }
        this.cards = GameHolder.GAMESTATE.getCardsMap();
        int i = 0;
        for (Card card : cards.values()) {
            Button physicalCard = physicalCards.get(i);
            physicalCard.setText(card.getWord());
            physicalCard.getStyleClass().clear();
            if(card.getIsGuessed()){
                physicalCard.setText("");
                if(card.getColor() == CardColor.Red)
                    physicalCard.getStyleClass().addAll(GameSettings.CARD_RED_GUESSED_CSS);
                else if (card.getColor() == CardColor.Blue)
                    physicalCard.getStyleClass().addAll(GameSettings.CARD_BLUE_GUESSED_CSS);
                else if (card.getColor() == CardColor.Passanger)
                    physicalCard.getStyleClass().addAll(GameSettings.CARD_PASSANGER_GUESSED_CSS);
                else if (card.getColor() == CardColor.Killer)
                    physicalCard.getStyleClass().addAll(GameSettings.CARD_KILLER_GUESSED_CSS);
            } else {
                physicalCard.getStyleClass().add(GameSettings.CARD_DEFAULT_CSS);
            }
            physicalCard.setTextAlignment(TextAlignment.CENTER);
            i++;
        }
    }
    private void updateCardCounter(Integer delta) {
        this.selectedCardCount += delta;
        this.lblSelectedWordCount.setText(this.selectedCardCount.toString());
    }

    private void updateCountdownLabel() {
        lblCountdown.setText(secondsLeft + "s");
        if(secondsLeft <= 15)
            lblCountdown.getStyleClass().add(GameSettings.RED_TEXT_CSS);
        if(secondsLeft == 0){
            goToNextTurn();
        }
    }
    private void goToNextTurn(){
        this.secondsLeft = 0;
        GameHolder.GAMESTATE.toNextTeamTurn(this.selectedWords);
        if(GameHolder.GAMESTATE.getHasWinner()){
            this.loadWinnerView();
        } else {
            this.loadSpymasterView();
        }
    }

    private void startCountdown() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    if(secondsLeft == 0)
                        cancel();
                    secondsLeft--;
                    updateCountdownLabel();
                });
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask, 0 , 1000);
    }

    /**
     * Loads spymaster view (if there is no winner)
     */
    private void loadSpymasterView() {
        try {
            FXMLLoaderUtils.loadScene(CodenamesApplication.getMainStage(), GameSettings.GAME_TITLE, GameSettings.SPYMASTER_VIEW_PATH);
        } catch (IOException e) {
            DialogUtils.showFatalErrorDialog();
        }
    }

    /**
     * Loads winner view (if there is a winner)
     */
    private void loadWinnerView() {
        try {
            FXMLLoaderUtils.loadScene(CodenamesApplication.getMainStage(),
                    GameSettings.GAME_TITLE,
                    GameSettings.GAME_WINNER_VIEW_PATH);
        } catch (IOException e) {
            DialogUtils.showFatalErrorDialog();
        }
    }
}
