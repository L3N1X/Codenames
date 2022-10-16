package hr.algebra.codenames.gameutils;

import hr.algebra.codenames.model.enums.CardColor;
import hr.algebra.codenames.model.singleton.GameSettings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class CardColorGenerator {
    private CardColorGenerator(){}
    public static List<CardColor> getCardColors(CardColor startingFirstColor){
        int numberOfRedCards = startingFirstColor == CardColor.Red ?
                GameSettings.STARTING_FIRST_CARD_COUNT : GameSettings.STARTING_LAST_CARD_COUNT;
        int numberOfBlueCards = startingFirstColor == CardColor.Blue ?
                GameSettings.STARTING_FIRST_CARD_COUNT : GameSettings.STARTING_LAST_CARD_COUNT;
        List<CardColor> redCards = new ArrayList<>();
        List<CardColor> blueCards = new ArrayList<>();
        List<CardColor> passangerCards = new ArrayList<>();
        List<CardColor> killerCards = new ArrayList<>();
        for (int i = 0; i < numberOfRedCards; i++)
            redCards.add(CardColor.Red);
        for (int i = 0; i < numberOfBlueCards; i++)
            blueCards.add(CardColor.Blue);
        for (int i = 0; i < GameSettings.PASSANGER_CARD_COUNT; i++)
            passangerCards.add(CardColor.Passanger);
        for (int i = 0; i < GameSettings.KILLER_CARD_COUNT; i++)
            killerCards.add(CardColor.Killer);
        List<CardColor> redBlueCards = Stream.concat(redCards.stream(), blueCards.stream()).toList();
        List<CardColor> passangerKillerCards = Stream.concat(passangerCards.stream(), killerCards.stream()).toList();
        List<CardColor> cards = new ArrayList<>(Stream.concat(redBlueCards.stream(), passangerKillerCards.stream()).toList());
        Collections.shuffle(cards);
        return cards;
    }
}
