package hr.algebra.codenames.model.singleton;

import hr.algebra.codenames.gameutils.CardColorGenerator;
import hr.algebra.codenames.model.*;
import hr.algebra.codenames.model.actions.ToOperativeActionObject;
import hr.algebra.codenames.model.actions.ToSpymasterActionObject;
import hr.algebra.codenames.model.enums.CardColor;
import hr.algebra.codenames.model.enums.PlayerRole;
import hr.algebra.codenames.repository.factories.RepositoryFactory;

import java.io.*;
import java.util.*;

public class GameState implements Serializable {

    public GameState() {
    }

    //region Private variables
    private String currentClue;
    private Integer currentGivenWordCount;
    private boolean hasWinner;
    private Team winnerTeam;
    private CardColor currentColorTurn;
    private Team currentTeam;
    private Team redTeam;
    private Team blueTeam;
    private HashMap<String, Card> cardsMap;
    private List<String> infoHistory = new ArrayList<>();
    private boolean isFirstTurn;
    //endregion

    public void initialize(String redSpymasterName, String redOperativeName, String blueSpymasterName, String blueOperativeName) throws IOException {
        hasWinner = false;
        isFirstTurn = true;
        CardColor startingFirstColor = getStartingFirstColor();
        if (startingFirstColor == CardColor.Red) {
            redTeam = new Team(CardColor.Red, true, redSpymasterName, redOperativeName);
            blueTeam = new Team(CardColor.Blue, false, blueSpymasterName, blueOperativeName);
            currentTeam = redTeam;
        } else {
            redTeam = new Team(CardColor.Red, false, redSpymasterName, redOperativeName);
            blueTeam = new Team(CardColor.Blue, true, blueSpymasterName, blueOperativeName);
            currentTeam = blueTeam;
        }
        cardsMap = new HashMap<>();
        generateCards(startingFirstColor);
        currentColorTurn = startingFirstColor;
    }

    public void reinitialize() throws IOException {
        //Purposely put in different order, so that in next game current spymaster is operative and vice versa
        initialize(redTeam.getOperative().getName(),
                redTeam.getSpymaster().getName(),
                blueTeam.getOperative().getName(),
                blueTeam.getSpymaster().getName());
    }

    private void generateCards(CardColor startingFirstColor) throws IOException {
        List<CardColor> cardColors = CardColorGenerator.getCardColors(startingFirstColor);
        List<String> words = RepositoryFactory.getRepository().GetWords();
        Collections.shuffle(words);
        int count = 0;
        for (CardColor cardType : cardColors) {
            String word = words.get(count).toUpperCase();
            cardsMap.put(word, new Card(word, cardType));
            count++;
        }
    }

    private CardColor getStartingFirstColor() {
        return (new Random()).nextBoolean() ? CardColor.Red : CardColor.Blue;
    }

    public boolean isOperativeTurn() {
        return this.currentClue != null && this.currentGivenWordCount != null && !isFirstTurn;
    }

    public boolean isYourTurn(Player player) {
        boolean yourTurn = false;
        if (player.getRole() == PlayerRole.Operative && isOperativeTurn() && player.getCardColor() == currentColorTurn)
            yourTurn = true;
        else if (player.getRole() == PlayerRole.Spymaster && !isOperativeTurn() && player.getCardColor() == currentColorTurn)
            yourTurn = true;
        return yourTurn;
    }

    private SerializableTurnLog lastTurnLog;

    public SerializableTurnLog getLastTurnLog() {
        return lastTurnLog;
    }

    //This is 1/2 Game action
    public void toOperativeAction(ToOperativeActionObject toOperativeActionObject) {
        this.lastTurnLog = null;
        isFirstTurn = false;
        this.currentClue = toOperativeActionObject.getClue();
        this.currentGivenWordCount = toOperativeActionObject.getGivenWordCount();
        this.infoHistory.add(this.currentTeam.getSpymaster().getName()
                + " gave clue: '"
                + this.currentClue.toUpperCase()
                + "' " + this.currentGivenWordCount);
    }

    //This is 2/2 Game action
    public void toSpymasterAction(ToSpymasterActionObject toSpymasterActionObject) {
        List<String> correctWords = new ArrayList<>();
        List<String> opponentWords = new ArrayList<>();
        List<String> passangerWords = new ArrayList<>();
        List<String> guessedWords = toSpymasterActionObject.getGuessedWords();
        String killerWord = null;
        for (String word : guessedWords) {
            Card card = cardsMap.get(word);
            if (card.getColor() == currentTeam.getTeamColor()) {
                correctWords.add(word);
                currentTeam.decrementPoints();
                card.markAsGuessed();
            } else if (card.getColor() == CardColor.Passanger) {
                passangerWords.add(word);
                card.markAsGuessed();
            } else if (card.getColor() == CardColor.Killer) {
                killerWord = word;
                hasWinner = true;
                if (currentTeam.getTeamColor() == CardColor.Red)
                    winnerTeam = blueTeam;
                else
                    winnerTeam = redTeam;
            } else {
                opponentWords.add(word);
                card.markAsGuessed();
                if (currentTeam.getTeamColor() == CardColor.Red)
                    blueTeam.decrementPoints();
                else
                    redTeam.decrementPoints();
            }
        }
        if (redTeam.getPoints() == 0) {
            hasWinner = true;
            winnerTeam = redTeam;
        } else if (blueTeam.getPoints() == 0) {
            hasWinner = true;
            winnerTeam = blueTeam;
        }

        this.lastTurnLog = new SerializableTurnLog(currentTeam,
                currentClue,
                guessedWords,
                correctWords,
                opponentWords,
                passangerWords,
                killerWord,
                winnerTeam);

        String guessedWordsString = String.join(", ", guessedWords);
        this.infoHistory.add(this.currentTeam.getOperative().getName()
                + " guessed: '"
                + guessedWordsString
                + "'");

        swapCurrentColor();
        currentClue = null;
        currentGivenWordCount = 0;

    }

    private void swapCurrentColor() {
        if (currentColorTurn == CardColor.Red)
            currentColorTurn = CardColor.Blue;
        else
            currentColorTurn = CardColor.Red;
        if (currentColorTurn == CardColor.Red)
            currentTeam = redTeam;
        else
            currentTeam = blueTeam;
    }

    //region getters

    public String getInfoHistory() {
        return String.join("\n", infoHistory);
    }
    public boolean getHasWinner() {
        return hasWinner;
    }

    public String getCurrentClue() {
        return currentClue;
    }

    public Integer getCurrentGivenWordCount() {
        return currentGivenWordCount;
    }

    public Player getCurrentPlayer () {
        if(!isOperativeTurn())
            return currentTeam.getSpymaster();
        return currentTeam.getOperative();
    }

    public Team getCurrentTeam() {
        return currentTeam;
    }

    public Team getRedTeam() {
        return redTeam;
    }

    public Team getBlueTeam() {
        return blueTeam;
    }

    public Team getWinnerTeam() {
        return winnerTeam;
    }

    public HashMap<String, Card> getCardsMap() {
        return new HashMap<>(cardsMap);
    }
    //endregion
}