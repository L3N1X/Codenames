package hr.algebra.codenames.model;

import hr.algebra.codenames.model.enums.CardColor;

import java.io.Serializable;

public class Card implements Serializable {
    private final String word;
    private final CardColor color;
    private boolean isGuessed;

    public Card(String word, CardColor color){
        this.word = word;
        this.color = color;
    }
    public String getWord() {
        return word;
    }
    public CardColor getColor() {
        return color;
    }
    public void markAsGuessed(){
        this.isGuessed = true;
    }
    public boolean getIsGuessed(){
        return isGuessed;
    }
}
