package hr.algebra.codenames.controller;

import hr.algebra.codenames.CodenamesApplication;
import hr.algebra.codenames.model.Card;
import hr.algebra.codenames.model.enums.CardColor;
import hr.algebra.codenames.model.singleton.GameState;
import hr.algebra.codenames.model.singleton.GlobalGameSettings;
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

import java.io.IOException;
import java.util.*;

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
    private HashMap<String, Card> cards;
    private int secondsLeft = GlobalGameSettings.SPYMASTER_TURN_DURATION;
    private Integer selectedCardCount;
    private List<String> selectedWords;

    public OperativeGameController() {
        this.selectedCardCount = 0;
    }
    private void updateCardCounter(Integer delta) {
        this.selectedCardCount += delta;
        this.lblSelectedWordCount.setText(this.selectedCardCount.toString());
    }
    private boolean selectedWordCountEqualToGiven(){
        return Objects.equals(this.selectedCardCount, GameState.getCurrentGivenWordCount());
    }

    @FXML
    private void confirmSelection(ActionEvent actionEvent){
        GameState.toNextTeamTurn(this.selectedWords);
        try {
            FXMLLoaderUtils.loadScreen(CodenamesApplication.getMainStage(), "Codenames Java Edition", "view/spymasterGameScreen.fxml");
        } catch (IOException e) {
            DialogUtils.showErrorDialog("Error", "Fatal application error", "Please restart the application");
        }
    }

    private void cardSelected(ActionEvent actionEvent){
        Button selectedCard = (Button)actionEvent.getSource();
        //Operative clicked on already selected card, so he unselects is
        if(selectedCard.getStyleClass().contains(GlobalGameSettings.CARD_SELECTED_CSS)){
            selectedCard.getStyleClass().removeAll(GlobalGameSettings.CARD_SELECTED_CSS);
            this.updateCardCounter(-1);
            this.selectedWords.remove(selectedCard.getText());
            this.lblClue.getStyleClass().remove(GlobalGameSettings.GREEN_TEXT);
            this.lblCardCount.getStyleClass().remove(GlobalGameSettings.GREEN_TEXT);
            this.lblSelectedWordCount.getStyleClass().remove(GlobalGameSettings.GREEN_TEXT);
            this.btnConfirm.setDisable(selectedCardCount == 0);
            return;
        }
        //Operative selected same amount of cards as given, he can only unselect them
        if(selectedWordCountEqualToGiven())
            return;
        //Operative selects unselected card
        selectedCard.getStyleClass().add(GlobalGameSettings.CARD_SELECTED_CSS);
        this.selectedWords.add(selectedCard.getText());
        this.updateCardCounter(1);
        this.btnConfirm.setDisable(selectedCardCount == 0);
        //Operative selected same amount as given, turn text green
        if(selectedWordCountEqualToGiven()){
            this.lblClue.getStyleClass().add(GlobalGameSettings.GREEN_TEXT);
            this.lblCardCount.getStyleClass().add(GlobalGameSettings.GREEN_TEXT);
            this.lblSelectedWordCount.getStyleClass().add(GlobalGameSettings.GREEN_TEXT);
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

    private void initializeLabels() {
        this.lblCardCount.setText(GameState.getCurrentGivenWordCount().toString());
        this.lblClue.setText("'" + GameState.getCurrentClue().toUpperCase() + "'");

        this.lblRedOperative.setText(GameState.getRedTeam().getOperative().getName());
        this.lblRedSpymaster.setText(GameState.getRedTeam().getSpymaster().getName());
        this.lblBlueOperative.setText(GameState.getBlueTeam().getOperative().getName());
        this.lblBlueSpymaster.setText(GameState.getBlueTeam().getSpymaster().getName());

        if(GameState.getCurrentTeam().getTeamColor() == CardColor.Red)
            this.lblRedOperative.setText(this.lblRedOperative.getText() + " (YOU)");
        else
            this.lblBlueOperative.setText(this.lblBlueOperative.getText() + " (YOU)");

        this.lblRedPoints.setText(String.valueOf(GameState.getRedTeam().getPoints()));
        this.lblBluePoints.setText(String.valueOf(GameState.getBlueTeam().getPoints()));
    }

    private void initializeCards() {
        List<Button> physicalCards = new ArrayList<>();
        for(Node component : this.cardsGrid.getChildren()){
            if(component instanceof Button){
                physicalCards.add((Button)component);
                ((Button)component).setOnAction(this::cardSelected);
            }
        }
        this.cards = GameState.getCardsMap();
        int i = 0;
        for (Card card : cards.values()) {
            Button physicalCard = physicalCards.get(i);
            physicalCard.setText(card.getWord());
            physicalCard.getStyleClass().clear();
            if(card.getIsGuessed()){
                physicalCard.setText("");
                if(card.getColor() == CardColor.Red)
                    physicalCard.getStyleClass().addAll(GlobalGameSettings.CARD_RED_GUESSED_CSS);
                else if (card.getColor() == CardColor.Blue)
                    physicalCard.getStyleClass().addAll(GlobalGameSettings.CARD_BLUE_GUESSED_CSS);
                else if (card.getColor() == CardColor.Passanger)
                    physicalCard.getStyleClass().addAll(GlobalGameSettings.CARD_PASSANGER_GUESSED_CSS);
                else if (card.getColor() == CardColor.Killer)
                    physicalCard.getStyleClass().addAll(GlobalGameSettings.CARD_KILLER_GUESSED_CSS);
            } else {
                physicalCard.getStyleClass().add(GlobalGameSettings.CARD_DEFAULT_CSS);
            }
            physicalCard.setTextAlignment(TextAlignment.CENTER);
            i++;
        }
    }

    private void startCountdown() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    secondsLeft--;
                    lblCountdown.setText(secondsLeft + "s");
                });
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask, 0 , 1000);
    }
}
