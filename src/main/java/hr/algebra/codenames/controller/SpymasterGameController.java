package hr.algebra.codenames.controller;

import hr.algebra.codenames.CodenamesApplication;
import hr.algebra.codenames.model.Card;
import hr.algebra.codenames.model.GameHolder;
import hr.algebra.codenames.model.actions.ToOperativeActionObject;
import hr.algebra.codenames.model.enums.CardColor;
import hr.algebra.codenames.model.singleton.GameLogger;
import hr.algebra.codenames.model.singleton.GameState;
import hr.algebra.codenames.model.singleton.GameSettings;
import hr.algebra.codenames.networking.messaging.Message;
import hr.algebra.codenames.networking.response.StatusCode;
import hr.algebra.codenames.networking.utils.NetworkUtils;
import hr.algebra.codenames.rmi.server.ChatService;
import hr.algebra.codenames.utils.DialogUtils;
import hr.algebra.codenames.utils.FXMLLoaderUtils;
import hr.algebra.jndi.ServerConfigurationKey;
import hr.algebra.jndi.helper.JndiHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.naming.NamingException;
import java.io.*;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;


public class SpymasterGameController implements Initializable {

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
    @FXML
    private Button btnSendMessage;
    @FXML
    private TextField tfMessage;
    @FXML
    private TextArea taChat;
    @FXML
    private TextArea taInfo;

    //endregion

    //region Private variables
    private HashMap<String, Card> cardsMap;
    private Integer selectedWordCount;
    private List<String> selectedWords;
    //endregion

    //region Event handlers

    //RMI
    private ChatService stub = null;

    //RMI

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Registry registry = LocateRegistry.getRegistry(
                    "localhost", Integer.parseInt(JndiHelper.getConfigurationParameter(ServerConfigurationKey.RMI_SERVER_PORT.getKey())));
            stub = (ChatService) registry.lookup(ChatService.REMOTE_OBJECT_NAME);
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
            System.err.println("[Client]: RMI service is not active...");
        } catch (NamingException | IOException e) {
            throw new RuntimeException(e);
        }
        refreshUI();
    }

    @FXML
    private void sendMessage(ActionEvent actionEvent){
        try {
            this.stub.sendMessage(GameHolder.YOU_PLAYER.getName() + " > " + this.tfMessage.getText());
            NetworkUtils.SendMessage(new Message(StatusCode.CHAT_MESSAGE, null), GameHolder.SERVER_OUTPUT);
            this.tfMessage.setText("");
        } catch (IOException e) {
            DialogUtils.showErrorDialog("[ERROR]", "RMI Service not operational", "Can't set messages");
        }
    }

    private void refreshUI() {

        this.selectedWords = new ArrayList<>();
        this.selectedWordCount = 0;
        this.btnClue.setDisable(true);
        initializeLabels();
        updateCardCounter(0);
        initializeCards();

        this.taInfo.setText(GameHolder.GAMESTATE.getValue().getInfoHistory());

        //If it's not your turn, you can't type a clue
        this.tfClue.setDisable(GameHolder.GAMESTATE.getValue().isYourTurn(GameHolder.YOU_PLAYER));

        this.tfClue.setDisable(!GameHolder.GAMESTATE.getValue().isYourTurn(GameHolder.YOU_PLAYER));

        if (GameHolder.GAMESTATE.getValue().getHasWinner()) {
            this.loadWinnerView();
        }
    }

    private void loadWinnerView() {
        try {
            FXMLLoaderUtils.loadScene(CodenamesApplication.getMainStage(),
                    GameSettings.GAME_TITLE,
                    GameSettings.GAME_WINNER_VIEW_PATH);
        } catch (IOException e) {
            DialogUtils.showFatalErrorDialog();
        }
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
                GameHolder.GAMESTATE.setValue((GameState) in.readObject());;
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

        //If it's not your turn, don't do anything!
        if(!GameHolder.GAMESTATE.getValue().isYourTurn(GameHolder.YOU_PLAYER)){
            return;
        }
        //Spymaster selected card that is already guessed
        if (selectedCard.getText().isBlank())
            return;
        //Spymaster has selected killer or passanger
        if (cardsMap.get(selectedCard.getText()).getColor() == CardColor.Killer
                || cardsMap.get(selectedCard.getText()).getColor() == CardColor.Passanger)
            return;
        //Spymaster has selected opposite team's card
        /*if (cardsMap.get(selectedCard.getText()).getColor() != GameHolder.GAMESTATE.getValue().getCurrentTeam().getTeamColor())
            return;*/
        if(cardsMap.get(selectedCard.getText()).getColor() != GameHolder.YOU_PLAYER.getCardColor()){
            return;
        }
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
        this.lblRedOperative.setText(GameHolder.GAMESTATE.getValue().getRedTeam().getOperative().getName());
        this.lblRedSpymaster.setText(GameHolder.GAMESTATE.getValue().getRedTeam().getSpymaster().getName());
        this.lblBlueOperative.setText(GameHolder.GAMESTATE.getValue().getBlueTeam().getOperative().getName());
        this.lblBlueSpymaster.setText(GameHolder.GAMESTATE.getValue().getBlueTeam().getSpymaster().getName());

        if (GameHolder.YOU_PLAYER.getCardColor() == CardColor.Red) {
            this.lblRedSpymaster.setText(this.lblRedSpymaster.getText() + " (YOU)");
        } else {
            this.lblBlueSpymaster.setText(this.lblBlueSpymaster.getText() + " (YOU)");
        }

        this.lblRedPoints.setText(String.valueOf(GameHolder.GAMESTATE.getValue().getRedTeam().getPoints()));
        this.lblBluePoints.setText(String.valueOf(GameHolder.GAMESTATE.getValue().getBlueTeam().getPoints()));
    }

    private void initializeCards() {
        List<Button> cardButtons = new ArrayList<>();
        for (Node component : this.cardsGrid.getChildren()) {
            if (component instanceof Button) {
                cardButtons.add((Button) component);
                ((Button) component).setOnAction(this::cardSelected);
            }
        }
        this.cardsMap = GameHolder.GAMESTATE.getValue().getCardsMap();
        int i = 0;
        for (Card card : cardsMap.values()) {
            Button cardButton = cardButtons.get(i);

            cardButton.setText(card.getWord());
            cardButton.getStyleClass().clear();
            if (card.getIsGuessed()) {
                cardButton.setText("");
                if (card.getColor() == CardColor.Red)
                    cardButton.getStyleClass().addAll(GameSettings.CARD_RED_GUESSED_CSS);
                else if (card.getColor() == CardColor.Blue)
                    cardButton.getStyleClass().addAll(GameSettings.CARD_BLUE_GUESSED_CSS);
                else if (card.getColor() == CardColor.Passanger)
                    cardButton.getStyleClass().addAll(GameSettings.CARD_PASSANGER_GUESSED_CSS);
                else if (card.getColor() == CardColor.Killer)
                    cardButton.getStyleClass().addAll(GameSettings.CARD_KILLER_GUESSED_CSS);
            } else {
                if (card.getColor() == CardColor.Red)
                    cardButton.getStyleClass().addAll(GameSettings.CARD_RED_CSS);
                else if (card.getColor() == CardColor.Blue)
                    cardButton.getStyleClass().addAll(GameSettings.CARD_BLUE_CSS);
                else if (card.getColor() == CardColor.Passanger)
                    cardButton.getStyleClass().addAll(GameSettings.CARD_PASSANGER_CSS);
                else if (card.getColor() == CardColor.Killer)
                    cardButton.getStyleClass().addAll(GameSettings.CARD_KILLER_CSS);
            }
            cardButton.setTextAlignment(TextAlignment.CENTER);
            i++;
        }
    }
    //endregion

    public SpymasterGameController() {
        GameHolder.GAMESTATE.addListener((observable, oldValue, newValue) -> {
            System.out.println("UI refreshed!");
            if(GameHolder.GAMESTATE.getValue().getLastTurnLog() != null){
                GameLogger.getInstance().addTurnLog(GameHolder.GAMESTATE.getValue().getLastTurnLog());
            }
            refreshUI();
        });
        GameHolder.CHAT_FLAG.addListener((observable, oldValue, newValue) -> {
            System.out.println("Chat area refreshed!");
            refreshChat();
        });
    }

    private void refreshChat() {
        try {
            String chatHistory = String.join("\n", stub.getChatHistory());
            this.taChat.setText(chatHistory);
        } catch (RemoteException e) {
            System.out.println("Failed to load chat....");
        }
    }

    private void goToOperative() {
        if(GameHolder.GAMESTATE.getValue().isYourTurn(GameHolder.YOU_PLAYER)){
            GameHolder.GAMESTATE.getValue().toOperativeAction(new ToOperativeActionObject(this.tfClue.getText(), this.selectedWordCount));
            try {
                NetworkUtils.SendMessage(new Message(StatusCode.GAME_MOVE, GameHolder.GAMESTATE.getValue()), GameHolder.SERVER_OUTPUT);
            } catch (IOException e) {
                DialogUtils.showErrorDialog("Fatal application error", "Failed to contact the server", "Closing Codenames...");
                System.exit(1);
            }
        }
    }

    private void checkClueEnabled() {
        String clue = this.tfClue.getText().trim();
        this.btnClue.setDisable(clue.isBlank() || this.selectedWordCount == 0);
    }

    private void updateCardCounter(Integer delta) {
        this.selectedWordCount += delta;
        this.lblCardCount.setText(this.selectedWordCount.toString());
    }
}
