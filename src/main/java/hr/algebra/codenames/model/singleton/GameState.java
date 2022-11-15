package hr.algebra.codenames.model.singleton;

import hr.algebra.codenames.gameutils.CardColorGenerator;
import hr.algebra.codenames.model.Card;
import hr.algebra.codenames.model.Team;
import hr.algebra.codenames.model.TurnLog;
import hr.algebra.codenames.model.enums.CardColor;
import hr.algebra.codenames.repository.factories.RepositoryFactory;

import java.io.*;
import java.util.*;

public class GameState implements Serializable {

    //@Serial
    //private static final long serialVersionUID = -3086391508939941693L;
    //private static final GameState instance = new GameState();

    /*public static GameState getInstance() {
        //if (instance == null)
        //    instance = new GameState();
        return instance;
    }*/

    /*@Serial
    public Object readResolve() {
        return getInstance();
    }*/

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
    //endregion

    public void initialize(String redSpymasterName, String redOperativeName, String blueSpymasterName, String blueOperativeName) throws IOException {
        hasWinner = false;
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

    public void toOperatorTurn(String clue, Integer givenWordCount) {
        currentClue = clue;
        currentGivenWordCount = givenWordCount;
    }

    public boolean isOperativeTurn() {
        return this.currentClue != null && this.currentGivenWordCount != null;
    }

    public void toNextTeamTurn(List<String> guessedWords) {
        List<String> correctWords = new ArrayList<>();
        List<String> opponentWords = new ArrayList<>();
        List<String> passangerWords = new ArrayList<>();
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
        TurnLog turnLog = new TurnLog(currentTeam,
                currentClue,
                guessedWords,
                correctWords,
                opponentWords,
                passangerWords,
                killerWord,
                winnerTeam);
        GameLogger.getInstance().addTurnLog(turnLog);
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
    public boolean getHasWinner() {
        return hasWinner;
    }

    public String getCurrentClue() {
        return currentClue;
    }

    public Integer getCurrentGivenWordCount() {
        return currentGivenWordCount;
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