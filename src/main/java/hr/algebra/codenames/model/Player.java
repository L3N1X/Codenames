package hr.algebra.codenames.model;

import hr.algebra.codenames.model.enums.PlayerRole;
import hr.algebra.codenames.model.enums.CardColor;

public class Player {
    private final String name;
    private final CardColor cardColor;
    private PlayerRole currentRole;

    public Player(String name, CardColor cardType, PlayerRole startingRole) {
        this.name = name;
        this.cardColor = cardType;
        this.currentRole = startingRole;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "(" + this.currentRole.toString() + ") " + this.name;
    }
}
