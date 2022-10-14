package hr.algebra.codenames.model.singleton;

import hr.algebra.codenames.gameutils.CardColorGenerator;
import hr.algebra.codenames.model.Card;
import hr.algebra.codenames.model.Team;
import hr.algebra.codenames.model.enums.CardColor;
import hr.algebra.codenames.repository.factories.RepositoryFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class GameState {
    // TODO: 13.10.2022. This class is only used before implementing networking, gamestate will not be saved this way

    private GameState(){}

    private static CardColor currentColorTurn;
    private static Team currentTeam;
    private static  Team redTeam;
    private static Team blueTeam;

    private static HashMap<String, Card> cardsMap;

    private static CardColor getStartingFirstColor() {
        return (new Random()).nextBoolean() ? CardColor.Red : CardColor.Blue;
    }

    public static void initialize(String redSpymasterName, String redOperativeName, String blueSpymasterName, String blueOperativeName){
        CardColor startingFirstColor = getStartingFirstColor();
        if(startingFirstColor == CardColor.Red){
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

    private static void generateCards(CardColor startingFirstColor) {
        List<CardColor> cardColors = CardColorGenerator.getCardColors(startingFirstColor);
        List<String> words = RepositoryFactory.getWordRepository().GetWords();
        Collections.shuffle(words);
        int count = 0;
        for (CardColor cardType : cardColors) {
            String word = words.get(count).toUpperCase();
            cardsMap.put(word, new Card(word, cardType));
            count++;
        }
    }

    public static void toOperatorTurn(String clue, int givenWordCount) {
        currentClue = clue;
        currentGivenWordCount = givenWordCount;
    }

    private static String currentClue;
    private static int currentGivenWordCount;
    private static boolean gameOver;
    private static Team winnerTeam;

    public static void toNextTeamTurn(List<String> guessedWords) {
        for (String word : guessedWords) {
            Card card = cardsMap.get(word);
            if(card.getColor() == currentTeam.getTeamColor()) {
                currentTeam.decrementPoints();
                card.markAsGuessed();
            }
            else if (card.getColor() == CardColor.Passanger){
                card.markAsGuessed();
            }
            else if (card.getColor() == CardColor.Killer){
                gameOver = true;
                if(currentTeam.getTeamColor() == CardColor.Red)
                    winnerTeam = blueTeam;
                else
                    winnerTeam = redTeam;
            }
            else {
                card.markAsGuessed();
                if(currentTeam.getTeamColor() == CardColor.Red)
                    blueTeam.decrementPoints();
                else
                    redTeam.decrementPoints();
            }
        }
        if(redTeam.getPoints() == 0){
            gameOver = true;
            winnerTeam = redTeam;
        }
        else if (blueTeam.getPoints() == 0){
            gameOver = true;
            winnerTeam = blueTeam;
        }
        swapCurrentColor();
        currentClue = null;
        currentGivenWordCount = 0;
    }

    private static void swapCurrentColor() {
        if(currentColorTurn == CardColor.Red)
            currentColorTurn = CardColor.Blue;
        else
            currentColorTurn = CardColor.Red;
        if(currentColorTurn == CardColor.Red)
            currentTeam = redTeam;
        else
            currentTeam = blueTeam;
    }

    public static Team getCurrentTeam(){
        return currentTeam;
    }

    public static Team getRedTeam() {
        return redTeam;
    }

    public static Team getBlueTeam() {
        return blueTeam;
    }

    public static HashMap<String, Card> getCardsMap() {
        return new HashMap<>(cardsMap);
    }
}