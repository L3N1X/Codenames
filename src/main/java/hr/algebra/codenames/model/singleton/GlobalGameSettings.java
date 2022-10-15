package hr.algebra.codenames.model.singleton;

public class GlobalGameSettings {
    private GlobalGameSettings() {}
    //region Game logic constants
    public static final int STARTING_FIRST_CARD_COUNT = 9;
    public static final int STARTING_LAST_CARD_COUNT = 8;
    public static final int PASSANGER_CARD_COUNT = 7;
    public static final int KILLER_CARD_COUNT = 1;
    public static final int SPYMASTER_TURN_DURATION = 30;
    public static final int OPERATIVE_TURN_DURATION = 30;
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
    public static final String GREEN_TEXT = "green";
    //endregion
}
