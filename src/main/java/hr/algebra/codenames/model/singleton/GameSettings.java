package hr.algebra.codenames.model.singleton;

public class GameSettings {
    private GameSettings() {}

    //region Resource paths
    public static final String RED_TEAM_IMAGE_PATH = "..\\assets\\red_player.png";
    public static final String BLUE_TEAM_IMAGE_PATH = "..\\assets\\blue_player.png";
    //endregion

    //region FXML paths
    public static final String SPYMASTER_VIEW_PATH = "view/spymasterGameScreen.fxml";
    public static final String OPERATIVE_VIEW_PATH = "view/operativeGameScreen.fxml";
    public static final String START_VIEW_PATH = "view/startGameScreen.fxml";
    public static final String SCORE_VIEW_PATH = "view/turnsGameScreen.fxml";
    public static final String GAME_LOGS_VIEW_PATH = "view/logsGameScreen.fxml";
    public static final String GAME_WINNER_VIEW_PATH = "view/winnerGameScreen.fxml";
    //endregion

    //region Game texts

    public static final String GAME_TITLE = "Codenames Java Edition v0.321";

    //endregion

    //region Game logic constants
    public static final int STARTING_FIRST_CARD_COUNT = 9;
    public static final int STARTING_LAST_CARD_COUNT = 8;
    public static final int PASSANGER_CARD_COUNT = 7;
    public static final int KILLER_CARD_COUNT = 1;
    public static final int SPYMASTER_TURN_DURATION = 60;
    public static final int OPERATIVE_TURN_DURATION = 60;
    //endregion

    //region Game CSS classes constants
    public static final String CARD_KILLER_CSS = "card_black";
    public static final String CARD_KILLER_GUESSED_CSS = "card_black_guessed";
    public static final String CARD_RED_CSS = "card_red";
    public static final String CARD_RED_GUESSED_CSS = "card_red_guessed";
    public static final String CARD_BLUE_CSS = "card_blue";
    public static final String CARD_BLUE_GUESSED_CSS = "card_blue_guessed";
    public static final String CARD_PASSANGER_CSS = "card_passanger";
    public static final String CARD_PASSANGER_GUESSED_CSS = "card_passanger_guessed";
    public static final String CARD_DEFAULT_CSS = "card";
    public static final String CARD_SELECTED_CSS = "card_selected";
    public static final String GREEN_TEXT_CSS = "green";
    public static final String RED_TEXT_CSS = "red";
    public static final String BLUE_TEXT_CSS = "blue";
    public static final String BLUE_PANE_CSS = "blue_pane";
    public static final String RED_PANE_CSS = "red_pane";
    //endregion
}
