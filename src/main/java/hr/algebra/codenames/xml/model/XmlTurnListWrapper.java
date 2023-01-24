package hr.algebra.codenames.xml.model;

import hr.algebra.codenames.model.SerializableTurnLog;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.List;

@Root(name = "game-turns")
public class XmlTurnListWrapper implements Serializable {
    @ElementList(name = "game-turn", inline = true)
    private List<SerializableTurnLog> turnLogs;
    public XmlTurnListWrapper(@ElementList(name = "game-turn", inline = true) List<SerializableTurnLog> xmlTurnLogs) {
        this.turnLogs = xmlTurnLogs;
    }
    public List<SerializableTurnLog> getTurnLogs() {
        return turnLogs;
    }
}
