package hr.algebra.codenames.model;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TurnLog {

    //region private variables
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    private final LocalDateTime dateTime;
    private final Team team;
    private final String clue;
    private final ArrayList<String> guessedWords;
    private final ArrayList<String> correctWords;
    private final ArrayList<String> opponentWords;
    private final ArrayList<String> passangerWords;
    private final String killerWord;
    //endregion

    // TODO: 13.10.2022. Make a method in controller which takes team and generates TeamTurn while searching card field

    public TurnLog(Team team,
                   String clue,
                   List<String> guessedWords,
                   List<String> correctWords,
                   List<String> opponentWords,
                   List<String> passangerWords,
                   String killerWord) {
        this.team = team;
        this.clue = clue;
        this.guessedWords = (ArrayList<String>) guessedWords;
        this.correctWords = (ArrayList<String>) correctWords;
        this.opponentWords = (ArrayList<String>) opponentWords;
        this.passangerWords = (ArrayList<String>) passangerWords;
        this.killerWord = killerWord;
        this.dateTime = LocalDateTime.now();
    }

    public String getLog(){
        String guessedWordsString = String.join(",", this.guessedWords);
        String correctWordsString = String.join(",", this.correctWords);
        String opponentWordsString = String.join(",", this.opponentWords);
        String passangerWordsString = String.join(",", this.passangerWords);
        return MessageFormat.format(
                "{0} Team {1} | {2} (spymaster) gave clue '{3}'\n {4} (operative) guessed: {5}\n{6}\n{7}\n{8}\n{9}",
                this.dateTime.format(this.dateTimeFormatter),
                team.getTeamColor(),
                team.getSpymaster().getName(),
                this.clue,
                this.team.getOperative().getName(),
                guessedWordsString, this.correctWords.size() == 0 ? "None of the words were correct" : correctWordsString,
                this.opponentWords.size() == 0 ? "" : opponentWordsString,
                this.passangerWords.size() == 0 ? "" : passangerWordsString,
                this.killerWord == null ? "" : "Guessed the killer word: '" + this.killerWord + "'");
    }
}
