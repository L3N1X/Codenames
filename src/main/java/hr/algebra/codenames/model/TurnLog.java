package hr.algebra.codenames.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TurnLog implements Serializable {

    //region private variables
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    private final LocalDateTime dateTime;
    private final StringProperty team;
    private final StringProperty clue;
    private final StringProperty guessedWords;
    private final StringProperty correctWords;
    private final StringProperty opponentWords;
    private final StringProperty passangerWords;
    private final StringProperty killerWord;
    private final StringProperty victory;
    private final Team winnerTeam;
    //endregion

    public TurnLog(Team team,
                   String clue,
                   List<String> guessedWords,
                   List<String> correctWords,
                   List<String> opponentWords,
                   List<String> passangerWords,
                   String killerWord,
                   Team winnerTeam) {
        this.team = new SimpleStringProperty(team.getTeamColor().toString() + " | " + team.getSpymaster().getName()
                + " (s) " + team.getOperative().getName() + " (o)");
        this.clue = new SimpleStringProperty(clue);
        this.guessedWords = new SimpleStringProperty(String.join(", ", guessedWords));
        this.correctWords = new SimpleStringProperty(String.join(", ", correctWords));
        this.opponentWords = new SimpleStringProperty(String.join(", ", opponentWords));
        this.passangerWords = new SimpleStringProperty(String.join(", ", passangerWords));
        this.killerWord = new SimpleStringProperty(killerWord != null ? killerWord : "");
        this.victory = new SimpleStringProperty(winnerTeam != null ? winnerTeam.getTeamColor() + " Team VICTORY" : "");
        this.winnerTeam = winnerTeam;
        this.dateTime = LocalDateTime.now();
    }
    public Team getWinnerTeam() { return winnerTeam; }
    public StringProperty dateTimeProperty() {
        return new SimpleStringProperty(this.dateTime.toString());
    }

    public final StringProperty teamProperty() {
        return team;
    }

    public final StringProperty clueProperty() {
        return clue;
    }

    public final StringProperty guessedWordsProperty() {
        return guessedWords;
    }

    public final StringProperty correctWordsProperty() {
        return correctWords;
    }

    public final StringProperty opponentWordsProperty() {
        return opponentWords;
    }

    public final StringProperty passangerWordsProperty() {
        return passangerWords;
    }

    public final StringProperty killerWordProperty() {
        return killerWord;
    }

    public final StringProperty victoryProperty() {
        return victory;
    }
}
