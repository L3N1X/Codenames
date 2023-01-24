package hr.algebra.codenames.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.List;

@Root(name = "game-turn")
public class SerializableTurnLog implements Serializable {

    @Element(name = "team")
    private final Team team;
    @Element(name = "clue")
    private final String clue;
    @Element(name = "guessed-words", required = false)
    private final String guessedWords;
    @Element(name = "correct-words", required = false)
    private final String correctWords;
    @Element(name = "opponent-words",required = false)
    private final String opponentWords;
    @Element(name = "passanger-words",required = false)
    private final String passangerWords;
    @Element(name = "killer-word",required = false)
    private final String killerWord;
    @Element(name = "winner-team", required = false)
    private final Team winnerTeam;

    public SerializableTurnLog(Team team,
                               String clue,
                               List<String> guessedWords,
                               List<String> correctWords,
                               List<String> opponentWords,
                               List<String> passangerWords,
                               String killerWord,
                               Team winnerTeam) {
        this.team = team;
        this.clue = clue;
        this.guessedWords = String.join(", ", guessedWords);
        this.correctWords = String.join(", ", correctWords);
        this.opponentWords = String.join(", ", opponentWords);
        this.passangerWords = String.join(", ", passangerWords);
        this.killerWord = killerWord != null ? killerWord : "";
        this.winnerTeam = winnerTeam;
    }

    public SerializableTurnLog(@Element(name = "team") Team team,
                               @Element(name = "clue") String clue,
                               @Element(name = "guessed-words") String guessedWords,
                               @Element(name = "correct-words") String correctWords,
                               @Element(name = "opponent-words") String opponentWords,
                               @Element(name = "passanger-words") String passangerWords,
                               @Element(name = "killer-word") String killerWord,
                               @Element(name = "winner-team") Team winnerTeam) {
        this.team = team;
        this.clue = clue;
        this.guessedWords = guessedWords != null ? guessedWords : "";
        this.correctWords = correctWords != null ? correctWords : "";
        this.opponentWords = opponentWords!= null ? opponentWords : "";
        this.passangerWords = passangerWords != null ? passangerWords : "";
        this.killerWord = killerWord != null ? killerWord : "";
        this.winnerTeam = winnerTeam;
    }

    public Team getTeam() {
        return team;
    }

    public String getOpponentWords() {
        return opponentWords;
    }

    public String getPassangerWords() {
        return passangerWords;
    }

    public String getGuessedWords() {
        return guessedWords;
    }

    public String getCorrectWords() {
        return correctWords;
    }

    public String getKillerWord() {
        return killerWord;
    }

    public String getClue() {
        return clue;
    }

    public Team getWinnerTeam() {
        return winnerTeam;
    }
}
