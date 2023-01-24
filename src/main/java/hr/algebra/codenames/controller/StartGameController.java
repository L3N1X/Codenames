package hr.algebra.codenames.controller;

import hr.algebra.codenames.CodenamesApplication;
import hr.algebra.codenames.gameutils.CardColorGenerator;
import hr.algebra.codenames.model.*;
import hr.algebra.codenames.model.enums.CardColor;
import hr.algebra.codenames.model.enums.PlayerRole;
import hr.algebra.codenames.model.singleton.GameLogger;
import hr.algebra.codenames.model.singleton.GameSettings;
import hr.algebra.codenames.model.singleton.GameState;
import hr.algebra.codenames.networking.messaging.Message;
import hr.algebra.codenames.networking.response.StatusCode;
import hr.algebra.codenames.networking.utils.NetworkUtils;
import hr.algebra.codenames.repository.factories.RepositoryFactory;
import hr.algebra.codenames.repository.implementations.FileRepository;
import hr.algebra.codenames.utils.DialogUtils;
import hr.algebra.codenames.utils.FXMLLoaderUtils;
import hr.algebra.codenames.utils.Ldocs.LDocs;
import hr.algebra.codenames.utils.Ldocs.LDocsFormattingUtils;
import hr.algebra.codenames.xml.model.XmlTurnListWrapper;
import hr.algebra.jndi.ServerConfigurationKey;
import hr.algebra.jndi.helper.JndiHelper;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.naming.NamingException;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;

public class StartGameController implements Initializable {

    @FXML
    private TextField tfPlayerName;
    @FXML
    private Button btnRedTeam;
    @FXML
    private Button btnBlueTeam;
    @FXML
    private Button btnSpymaster;
    @FXML
    private Button btnOperative;
    @FXML
    private Button btnStartGame;
    @FXML
    private Label lbInformation;

    private CardColor selectedColor;
    private PlayerRole selectedRole;

    //region Selection

    private void toggleStartButtonDisabled() {
        if (this.selectedColor == null && this.selectedRole == null)
            this.btnStartGame.setDisable(true);
        else if (this.selectedColor != null && this.selectedRole != null)
            this.btnStartGame.setDisable(false);
    }

    @FXML
    private void redTeamSelected(ActionEvent actionEvent){
        this.btnRedTeam.getStyleClass().removeAll(GameSettings.GREY_BUTTON_CSS);
        this.btnRedTeam.getStyleClass().add(GameSettings.RED_BUTTON_CSS);
        this.btnBlueTeam.getStyleClass().removeAll(GameSettings.BLUE_BUTTON_CSS);
        this.btnBlueTeam.getStyleClass().add(GameSettings.GREY_BUTTON_CSS);
        this.selectedColor = CardColor.Red;
        toggleStartButtonDisabled();
    }

    @FXML
    private void blueTeamSelected(ActionEvent actionEvent){
        this.btnBlueTeam.getStyleClass().removeAll(GameSettings.GREY_BUTTON_CSS);
        this.btnBlueTeam.getStyleClass().add(GameSettings.BLUE_BUTTON_CSS);
        this.btnRedTeam.getStyleClass().removeAll(GameSettings.RED_BUTTON_CSS);
        this.btnRedTeam.getStyleClass().add(GameSettings.GREY_BUTTON_CSS);
        this.selectedColor = CardColor.Blue;
        toggleStartButtonDisabled();
    }

    @FXML
    private void spymasterSelected(ActionEvent actionEvent){
        this.btnSpymaster.getStyleClass().removeAll(GameSettings.GREY_BUTTON_CSS);
        this.btnOperative.getStyleClass().add(GameSettings.GREY_BUTTON_CSS);
        this.selectedRole = PlayerRole.Spymaster;
        toggleStartButtonDisabled();
    }

    @FXML
    private void operativeSelected(ActionEvent actionEvent){
        this.btnOperative.getStyleClass().removeAll(GameSettings.GREY_BUTTON_CSS);
        this.btnSpymaster.getStyleClass().add(GameSettings.GREY_BUTTON_CSS);
        this.selectedRole = PlayerRole.Operative;
        toggleStartButtonDisabled();
    }

    //endregion


    @FXML
    private void logsClicked(ActionEvent actionEvent) {
        try {
            FXMLLoaderUtils.loadScene(new Stage(), GameSettings.LOGS_TITLE, GameSettings.LOGS_VIEW_PATH);
        } catch (IOException e) {
            e.printStackTrace();
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
                GameHolder.GAMESTATE.setValue((GameState) in.readObject());
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

    public void btnStartGameClick() {
        register();
    }

    public void register() {
        this.lbInformation.setText("");
        Player newUnregisteredPlayer = new Player(this.tfPlayerName.getText(), selectedColor, selectedRole);
        GameHolder.YOU_PLAYER = newUnregisteredPlayer; //This is okay, because game will start only when all are registered!
        Message requestRegistrationMessage = new Message(StatusCode.REQUEST_REGISTRATION, newUnregisteredPlayer);
        try {
            NetworkUtils.SendMessage(requestRegistrationMessage, GameHolder.SERVER_OUTPUT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connectToServer();
        toggleStartButtonDisabled();
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
    }
    private void connectToServer() {
        try {
            int serverPort = Integer.parseInt(JndiHelper.getConfigurationParameter(ServerConfigurationKey.GAMESERVER_PORT.getKey()));
            Socket socket = new Socket("localhost", serverPort);
            GameHolder.SERVER_OUTPUT = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            new Thread(new ServerHandler(socket, in, this.lbInformation, this.btnStartGame)).start();
        } catch (IOException | NamingException e) {
            throw new RuntimeException(e);
        }
    }

    static class ServerHandler implements Runnable {
        private final Socket socket;
        private final ObjectInputStream currentIn;

        //Label is here only because FXML scope is not visible from Platform.runLater(); Button also!
        private final Label lbInformation;
        private final Button btnStart;
        public ServerHandler(Socket socket, ObjectInputStream in, Label lbInformation, Button btnStart) {
            System.out.println("[Client]: Successfully connected to the server!");
            this.socket = socket;
            this.currentIn = in;
            this.lbInformation = lbInformation;
            this.btnStart = btnStart;
        }

        @Override
        public void run() {
            try {
                while (true) {

                    //Default communication that works!
                    /*Object response = currentIn.readObject();
                    if(response instanceof String){
                        String message = (String) response;
                        System.out.println("Received message: " + message);
                    }*/

                    Object responseObject = currentIn.readObject();

                    if(responseObject instanceof Message message){
                        if (message.getStatusCode() == StatusCode.DEBUG_MESSAGE) {
                            System.out.println("[Server]: " + message.getStatusCode() + " - " + "You got a broadcast debug message!");
                        } else if (message.getStatusCode() == StatusCode.REFUSE_REGISTRATION){
                            System.out.println("[Server]: " + message.getStatusCode() + " - " + "Registration refused! Position already in use.");
                            Platform.runLater(() -> {
                                this.lbInformation.getStyleClass().add(GameSettings.RED_TEXT_CSS);
                                lbInformation.setText("Position already taken!");
                            });
                        } else if (message.getStatusCode() == StatusCode.ACCEPT_REGISTRATION) {
                            System.out.println("[Server]: " + message.getStatusCode() + " - " + "Registration accepted! Waiting for other players...");
                            Platform.runLater(() -> {
                                this.btnStart.setDisable(true);
                                this.lbInformation.getStyleClass().removeAll(GameSettings.RED_TEXT_CSS);
                                this.lbInformation.setText("Waiting for others...");
                            });
                        } else if (message.getStatusCode() == StatusCode.START_GAME) {
                            System.out.println("[Server]: " + message.getStatusCode() + " - " + "All players registered! Starting the game...");
                            GameState newGameState = (GameState) message.getContent();
                            if(newGameState != null) {
                                GameHolder.GAMESTATE.setValue(newGameState);
                                Platform.runLater(() -> {
                                    try {
                                        if (GameHolder.YOU_PLAYER.getRole() == PlayerRole.Spymaster) {
                                            FXMLLoaderUtils.loadScene(CodenamesApplication.getMainStage(), GameSettings.GAME_TITLE, GameSettings.SPYMASTER_VIEW_PATH);
                                        } else {
                                            FXMLLoaderUtils.loadScene(CodenamesApplication.getMainStage(), GameSettings.GAME_TITLE, GameSettings.OPERATIVE_VIEW_PATH);
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        System.out.println("[Client]: Error... Can't open the Game screen");
                                    }
                                });
                            }
                        } else if (message.getStatusCode() == StatusCode.GAME_MOVE) {
                            System.out.println("[Server]: " + message.getStatusCode() + " - Obtained new game state, updating UI");
                            GameState newGameState = (GameState) message.getContent();
                            if (newGameState != null) {
                                //UI will update because GAMESTATE is observable!
                                Platform.runLater(() -> {
                                    GameHolder.GAMESTATE.setValue(newGameState);
                                });
                            }
                        } else if (message.getStatusCode() == StatusCode.CHAT_MESSAGE) {
                            System.out.println("[Server]: " + message.getStatusCode() + " - Got a new chat message!");
                            Platform.runLater(() -> {
                                GameHolder.CHAT_FLAG.setValue(UUID.randomUUID().toString());
                            });
                        } else if (message.getStatusCode() == StatusCode.INITIAL_GAME_LOGS) {
                            System.out.println("[SERVER]: " + message.getStatusCode() + " - Received existing game logs.");
                            XmlTurnListWrapper logsWrapper = (XmlTurnListWrapper) message.getContent();
                            for (SerializableTurnLog turnLog : logsWrapper.getTurnLogs()) {
                                GameLogger.getInstance().addTurnLog(turnLog);
                            }
                            System.out.println("[CLIENT]: Initialized logs!");
                        }
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    currentIn.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
