package hr.algebra.codenames.model.singleton;

import hr.algebra.codenames.model.TurnLog;

import java.util.LinkedList;
import java.util.List;

public class GameLogger {
    private final List<TurnLog> turnLogs;
    private static GameLogger instance;
    private GameLogger(){
        this.turnLogs = new LinkedList<TurnLog>();
    }

    public static GameLogger getInstance(){
        if(instance == null)
            instance = new GameLogger();
        return instance;
    }

    public List<TurnLog> getTurnLogs() {
        return new LinkedList<>(this.turnLogs);
    }

    public void addTurnLog(TurnLog turnLog){
        this.turnLogs.add(turnLog);
    }
}
