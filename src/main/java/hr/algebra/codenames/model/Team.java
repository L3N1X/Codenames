package hr.algebra.codenames.model;

import hr.algebra.codenames.model.enums.CardColor;
import hr.algebra.codenames.model.enums.PlayerRole;
import hr.algebra.codenames.model.singleton.GameSettings;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

@Root(name = "team")
public class Team implements Serializable {
    @Element(name = "color")
    private final CardColor cardType;
    private final Boolean startedFirst;
    private Integer points;
    private final Player spymaster;
    private final Player operative;
    @Element(name = "spymaster")
    private final String spymasterName;
    @Element(name = "operative")
    private final String operativeName;

    public Team(CardColor cardType, Boolean isStartingFirst, String spymasterName, String operativeName) {
        this.cardType = cardType;
        this.startedFirst = isStartingFirst;
        this.points = this.startedFirst ? GameSettings.STARTING_FIRST_CARD_COUNT : GameSettings.STARTING_LAST_CARD_COUNT;
        this.spymaster = new Player(spymasterName, this.cardType, PlayerRole.Spymaster);
        this.operative = new Player(operativeName, this.cardType, PlayerRole.Operative);

        this.spymasterName = this.spymaster.getName();
        this.operativeName = this.operative.getName();
    }

    //This constructor is only used for XML
    public Team(@Element(name="color") CardColor cardType,
                @Element(name="spymaster") String spymasterName,
                @Element(name = "operative") String operativeName){
        this.cardType = cardType;
        this.spymasterName = spymasterName;
        this.operativeName = operativeName;
        this.spymaster = null;
        this.operative = null;
        this.startedFirst = false;
    }

    public String getSpymasterName() {
        return spymasterName;
    }

    public String getOperativeName() {
        return operativeName;
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

    public Integer getPoints() {
        return points;
    }

    public void decrementPoints() {
        //this.points.set(points.get() - 1);
        this.points--;
    }
}
