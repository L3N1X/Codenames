package hr.algebra.codenames.model;

import hr.algebra.codenames.model.enums.PlayerRole;
import hr.algebra.codenames.model.enums.CardColor;

public class Player {
    private final String name;
    private final CardColor cardType;
    private PlayerRole currentRole;

    public Player(String name, CardColor cardType, PlayerRole startingRole) {
        this.name = name;
        this.cardType = cardType;
        this.currentRole = startingRole;
    }

    public String getName() {
        return name;
    }

    public CardColor getTeamColor() {
        return cardType;
    }

    public PlayerRole getCurrentRole() {
        return currentRole;
    }

    public void setCurrentRole(PlayerRole currentRole) {
        this.currentRole = currentRole;
    }

    @Override
    public String toString() {
        return "(" + this.currentRole.toString() + ") " + this.name;
    }
}
