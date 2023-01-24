package hr.algebra.codenames.controller;

import hr.algebra.codenames.model.SerializableTurnLog;
import hr.algebra.codenames.model.TurnLog;
import hr.algebra.codenames.model.singleton.GameLogger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class LogsGameController implements Initializable {

    //region FXML controls
    @FXML
    private TableView<TurnLog> tblvLogs;
    @FXML
    private TableColumn<TurnLog, String> tcTeam;
    @FXML
    private TableColumn<TurnLog, String> tcClue;
    @FXML
    private TableColumn<TurnLog, String> tcGuessedWords;
    @FXML
    private TableColumn<TurnLog, String> tcCorrectWords;
    @FXML
    private TableColumn<TurnLog, String> tcOpponentWords;
    @FXML
    private TableColumn<TurnLog, String> tcPAssangerWords;
    @FXML
    private TableColumn<TurnLog, String> tcKillerWord;
    @FXML
    private TableColumn<TurnLog, String> tcVictory;
    @FXML
    private Button btnBackToGame;
    //endregion

    private ObservableList<TurnLog> turnLogs;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.initializeTableCells();
        List<SerializableTurnLog> serializableTurnLogs =  GameLogger.getInstance().getTurnLogs();
        turnLogs = FXCollections.observableArrayList(
                serializableTurnLogs.stream()
                        .map(TurnLog::fromSerializableTurnLog)
                        .collect(Collectors.toList()));
        this.showLogs();
        if(!turnLogs.isEmpty())
            tblvLogs.getSelectionModel().select(0);
    }

    @FXML
    public void backToGame(){
        Stage stage = (Stage) btnBackToGame.getScene().getWindow();
        stage.close();
    }

    private void initializeTableCells() {
        this.tcTeam.setCellValueFactory(new PropertyValueFactory<>("team"));
        this.tcClue.setCellValueFactory(new PropertyValueFactory<>("clue"));
        this.tcGuessedWords.setCellValueFactory(new PropertyValueFactory<>("guessedWords"));
        this.tcCorrectWords.setCellValueFactory(new PropertyValueFactory<>("correctWords"));
        this.tcOpponentWords.setCellValueFactory(new PropertyValueFactory<>("opponentWords"));
        this.tcPAssangerWords.setCellValueFactory(new PropertyValueFactory<>("passangerWords"));
        this.tcKillerWord.setCellValueFactory(new PropertyValueFactory<>("killerWord"));
        this.tcVictory.setCellValueFactory(new PropertyValueFactory<>("victory"));
    }

    private void showLogs() {
        this.tblvLogs.setItems(this.turnLogs);
    }
}
