package hr.algebra.codenames.controller;

import hr.algebra.codenames.model.Card;
import hr.algebra.codenames.model.Team;
import hr.algebra.codenames.model.enums.CardColor;
import hr.algebra.codenames.model.singleton.GameState;
import hr.algebra.codenames.model.singleton.GlobalGameSettings;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

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
    private GridPane cardsGrid;
    //endregion
    private HashMap<String, Card> cards;
    private int secondsLeft = GlobalGameSettings.SPYMASTER_TURN_DURATION;
    private Integer cardCount;

    public SpymasterGameController() {
        this.cardCount = 0;
    }
    private void updateCardCounter(Integer delta) {
        this.cardCount += delta;
        this.lblCardCount.setText(this.cardCount.toString());
    }

    private void cardClicked(ActionEvent actionEvent){
        Button selectedCard = (Button)actionEvent.getSource();
        if(cards.get(selectedCard.getText()).getColor() == CardColor.Killer
                || cards.get(selectedCard.getText()).getColor() == CardColor.Passanger)
            return;
        if(cards.get(selectedCard.getText()).getColor() != GameState.getCurrentTeam().getTeamColor())
            return;
        if(selectedCard.getStyleClass().contains(GlobalGameSettings.CARD_SELECTED_CSS)){
            selectedCard.getStyleClass().removeAll(GlobalGameSettings.CARD_SELECTED_CSS);
            this.updateCardCounter(-1);
            return;
        }
        selectedCard.getStyleClass().add(GlobalGameSettings.CARD_SELECTED_CSS);
        this.updateCardCounter(1);
    }

    @FXML
    protected void initialize() {
        initializeLabels();
        updateCardCounter(0);
        initializeCards();
        startCountdown();
    }

    private void initializeLabels() {
        this.lblRedOperative.setText(GameState.getRedTeam().getOperative().getName());
        this.lblRedSpymaster.setText(GameState.getRedTeam().getSpymaster().getName());
        this.lblBlueOperative.setText(GameState.getBlueTeam().getSpymaster().getName());
        this.lblBlueSpymaster.setText(GameState.getBlueTeam().getSpymaster().getName());

        if(GameState.getCurrentTeam().getTeamColor() == CardColor.Red)
            this.lblRedSpymaster.setText(this.lblRedSpymaster.getText() + " (YOU)");
        else
            this.lblBlueSpymaster.setText(this.lblBlueSpymaster.getText() + " (YOU)");

        this.lblRedPoints.setText(String.valueOf(GameState.getRedTeam().getPoints()));
        this.lblBluePoints.setText(String.valueOf(GameState.getBlueTeam().getPoints()));
    }

    private void initializeCards() {
        List<Button> physicalCards = new ArrayList<>();
        for(Node component : this.cardsGrid.getChildren()){
            if(component instanceof Button){
                physicalCards.add((Button)component);
                ((Button)component).setOnAction(this::cardClicked);
            }
        }
        this.cards = GameState.getCardsMap();
        int i = 0;
        for (Card card : cards.values()) {
            physicalCards.get(i).setText(card.getWord());
            physicalCards.get(i).getStyleClass().clear();
            if(card.getColor() == CardColor.Red)
                physicalCards.get(i).getStyleClass().addAll(GlobalGameSettings.CARD_RED_CSS);
            else if (card.getColor() == CardColor.Blue)
                physicalCards.get(i).getStyleClass().addAll(GlobalGameSettings.CARD_BLUE_CSS);
            else if (card.getColor() == CardColor.Passanger)
                physicalCards.get(i).getStyleClass().addAll(GlobalGameSettings.CARD_PASSANGER_CSS);
            else if (card.getColor() == CardColor.Killer)
                physicalCards.get(i).getStyleClass().addAll(GlobalGameSettings.CARD_KILLER_CSS);
            physicalCards.get(i).setTextAlignment(TextAlignment.CENTER);
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
