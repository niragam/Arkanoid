package arkanoid.core;

/**
 * Centralized UI strings for the game.
 * Facilitates potential localization and maintains consistency.
 */
public final class UIStrings {

    private UIStrings() {
        // Utility class - no instantiation
    }

    // ==================== Game Title ====================
    public static final String GAME_TITLE = "ARKANOID";

    // ==================== Main Menu ====================
    public static final String MENU_START_GAME = "START GAME";
    public static final String MENU_QUIT = "QUIT";
    public static final String MENU_ACTION_MOVE = "Move";
    public static final String MENU_ACTION_PAUSE = "Pause";
    public static final String MENU_ACTION_SKIP_LEVEL = "Skip Level";

    // ==================== Pause Screen ====================
    public static final String PAUSE_TITLE = "PAUSED";
    public static final String PAUSE_RESUME = "Resume Game";
    public static final String PAUSE_QUIT_TO_MENU = "Quit to Menu";
    public static final String KEY_SPACE = "SPACE";
    public static final String KEY_Q = "Q";

    // ==================== End Screen ====================
    public static final String END_WIN = "YOU WIN!";
    public static final String END_GAME_OVER = "GAME OVER";
    public static final String END_FINAL_SCORE = "Final score: ";
    public static final String END_PROMPT_MENU = "Press SPACE to go back to main menu";
    public static final String END_PROMPT_QUIT = "Press Q to Quit";

    // ==================== In-Game ====================
    public static final String LAUNCH_HINT = "Press SPACE to launch";
    public static final String HUD_SCORE_PREFIX = "Score: ";
    public static final String HUD_LIVES_LABEL = "Lives:";
}
