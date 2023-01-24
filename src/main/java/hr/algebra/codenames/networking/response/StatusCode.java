package hr.algebra.codenames.networking.response;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public enum StatusCode implements Serializable {
    INITIAL_GAME_LOGS(700),
    CHAT_MESSAGE(600),
    DEBUG_MESSAGE(900),
    GAME_MOVE(500),
    REFUSE_REGISTRATION (400),
    ACCEPT_REGISTRATION(300),
    REQUEST_REGISTRATION(200),
    START_GAME(100);
    private final int value;
    private static final Map<Integer, StatusCode> map = new HashMap<>();
    static {
        for (StatusCode statusCode : StatusCode.values()){
            map.put(statusCode.value, statusCode);
        }
    }
    private StatusCode(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static StatusCode fromInt(int value){
        return map.get(value);
    }
}
