package hr.algebra.codenames.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;


public class TurnLog implements Serializable {

    //region private variables
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

    private TurnLog(Team team,
                   String clue,
                   String guessedWords,
                   String correctWords,
                   String opponentWords,
                   String passangerWords,
                   String killerWord,
                   Team winnerTeam) {
        this.team = new SimpleStringProperty("(" + team.getTeamColor() + ") "
                + team.getSpymasterName() + " (s)"
                + team.getOperativeName() + " (o)");
        this.clue = new SimpleStringProperty(clue);
        this.guessedWords = new SimpleStringProperty(String.join(", ", guessedWords));
        this.correctWords = new SimpleStringProperty(String.join(", ", correctWords));
        this.opponentWords = new SimpleStringProperty(String.join(", ", opponentWords));
        this.passangerWords = new SimpleStringProperty(String.join(", ", passangerWords));
        this.killerWord = new SimpleStringProperty(killerWord != null ? killerWord : "");
        this.victory = new SimpleStringProperty(winnerTeam != null ? winnerTeam.getTeamColor() + " Team VICTORY" : "");
        this.winnerTeam = winnerTeam;
    }

    public static TurnLog fromSerializableTurnLog(SerializableTurnLog serializableTurnLog){
        return new TurnLog(serializableTurnLog.getTeam(),
                serializableTurnLog.getClue(),
                serializableTurnLog.getGuessedWords(),
                serializableTurnLog.getCorrectWords(),
                serializableTurnLog.getOpponentWords(),
                serializableTurnLog.getPassangerWords(),
                serializableTurnLog.getKillerWord(),
                serializableTurnLog.getWinnerTeam());
    }

    public Team getWinnerTeam() { return winnerTeam; }

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
