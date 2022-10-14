package hr.algebra.codenames.model.singleton;

public class ScoreManager {
    private final GameLogger gameLogger;
    private static ScoreManager instance;
    private ScoreManager(){
        this.gameLogger = GameLogger.getInstance();
    }
    public static ScoreManager getInstance(){
        if(instance == null)
            instance = new ScoreManager();
        return instance;
    }
}
