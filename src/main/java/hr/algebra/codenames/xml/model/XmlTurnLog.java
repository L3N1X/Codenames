package hr.algebra.codenames.xml.model;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

//TEST
@Root(name = "game-turn")
public class XmlTurnLog {
    @Element(name = "date-time")
    private final String dateTime;
    @Element(name = "team")
    private final String team;
    @Element(name = "clue")
    private final String clue;
    @Element(name = "guessed-words")
    private final String guessedWords;
    @Element(name = "correct-words")
    private final String correctWords;
    @Element(name = "opponent-words")
    private final String opponentWords;
    @Element(name = "passanger-words")
    private final String passangerWords;
    @Element(name = "killer-word")
    private final String killerWord;
    @Element(name = "victory")
    private final String victory;
    @Element(name = "winner-team")
    private final String winnerTeam;

    public XmlTurnLog(String dateTime,
                      String team,
                      String clue,
                      String guessedWords,
                      String correctWords,
                      String opponentWords,
                      String passangerWords,
                      String killerWord,
                      String victory,
                      String winnerTeam) {
        this.dateTime = dateTime;
        this.team = team;
        this.guessedWords = guessedWords;
        this.correctWords = correctWords;
        this.clue = clue;
        this.opponentWords = opponentWords;
        this.passangerWords = passangerWords;
        this.killerWord = killerWord;
        this.victory = victory;
        this.winnerTeam = winnerTeam;
    }

    public String getClue() {
        return clue;
    }

    public String getCorrectWords() {
        return correctWords;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getGuessedWords() {
        return guessedWords;
    }

    public String getKillerWord() {
        return killerWord;
    }

    public String getOpponentWords() {
        return opponentWords;
    }

    public String getPassangerWords() {
        return passangerWords;
    }

    public String getTeam() {
        return team;
    }

    public String getVictory() {
        return victory;
    }

    public String getWinnerTeam() {
        return winnerTeam;
    }
}
