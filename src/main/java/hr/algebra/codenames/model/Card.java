package hr.algebra.codenames.model;

import hr.algebra.codenames.model.enums.CardColor;

public class Card {
    private final String word;
    private final CardColor type;
    private boolean isGuessed;

    public Card(String word, CardColor type){
        this.word = word;
        this.type = type;
    }
    public String getWord() {
        return word;
    }
    public CardColor getColor() {
        return type;
    }
    public void markAsGuessed(){
        this.isGuessed = true;
    }
    public boolean getIsGuessed(){
        return isGuessed;
    }
}
