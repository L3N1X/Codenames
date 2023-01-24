package hr.algebra.codenames.model;

import hr.algebra.codenames.model.enums.PlayerRole;
import hr.algebra.codenames.model.enums.CardColor;

import java.io.Serializable;

public class Player implements Serializable {
    private final String name;
    private final CardColor cardColor;
    private PlayerRole role;

    public Player(String name, CardColor color, PlayerRole role) {
        this.name = name;
        this.cardColor = color;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public PlayerRole getRole() {
        return role;
    }

    public CardColor getCardColor() {
        return cardColor;
    }

    @Override
    public String toString() {
        return "(" + this.role.toString() + ") " + this.name;
    }
}
