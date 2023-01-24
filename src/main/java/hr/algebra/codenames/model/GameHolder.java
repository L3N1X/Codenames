package hr.algebra.codenames.model;

import hr.algebra.codenames.model.singleton.GameState;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;

import java.io.ObjectOutputStream;

public class GameHolder {
    public static final Property<GameState> GAMESTATE = new SimpleObjectProperty<>(new GameState());
    public  static Player YOU_PLAYER;
    public static ObjectOutputStream SERVER_OUTPUT;
    public static Property<String> CHAT_FLAG = new SimpleObjectProperty<>("");

    private GameHolder() {
    }
}
