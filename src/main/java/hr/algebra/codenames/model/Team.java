package hr.algebra.codenames.model;

import hr.algebra.codenames.model.enums.PlayerRole;
import hr.algebra.codenames.model.enums.CardColor;
import hr.algebra.codenames.model.singleton.GlobalGameSettings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Team {
    private final CardColor cardType;
    private Boolean startedFirst;
    private final IntegerProperty points;
    private Player spymaster;
    private Player operative;

    public Team(CardColor cardType, Boolean isStartingFirst, String spymasterName, String operativeName) {
        this.cardType = cardType;
        this.startedFirst = isStartingFirst;
        this.points = new SimpleIntegerProperty(this.startedFirst ? GlobalGameSettings.STARTING_FIRST_CARD_COUNT : GlobalGameSettings.STARTING_LAST_CARD_COUNT);
        this.spymaster = new Player(spymasterName, this.cardType, PlayerRole.Spymaster);
        this.operative = new Player(operativeName, this.cardType, PlayerRole.OPERATIVE);
    }

    public void resetForNewGame() {
        this.startedFirst = !this.startedFirst;
        points.setValue(startedFirst ? GlobalGameSettings.STARTING_FIRST_CARD_COUNT : GlobalGameSettings.STARTING_LAST_CARD_COUNT);
        Player temp = spymaster;
        spymaster = operative;
        operative = temp;
        spymaster.setCurrentRole(PlayerRole.Spymaster);
        operative.setCurrentRole(PlayerRole.OPERATIVE);
    }

    public CardColor getTeamColor(){
        return this.cardType;
    }

    public Player getOperative() {
        return operative;
    }

    public Player getSpymaster() {
        return spymaster;
    }

    public int getPoints() {
        return points.get();
    }

    public void setPoints(int points) {
        this.points.set(points);
    }

    public void decrementPoints() { this.points.set(points.get() - 1); }
}
