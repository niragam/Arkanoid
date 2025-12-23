package arkanoid.core;

import java.util.List;
import javafx.scene.paint.Color;

/**
 * Centralized configuration constants for the Arkanoid game.
 * Eliminates magic numbers scattered throughout the codebase.
 */
public final class GameConfig {

    private GameConfig() {
        // Utility class - no instantiation
    }

    // ==================== Screen ====================
    public static final int SCREEN_WIDTH = 1200;
    public static final int SCREEN_HEIGHT = 800;

    // ==================== Borders ====================
    public static final int BORDER_THICKNESS = 20;
    public static final Color BORDER_COLOR = Color.GREY;

    // ==================== Blocks ====================
    public static final double BLOCK_HEIGHT = 25;
    public static final int DEFAULT_ROWS = 5;
    public static final int BLOCKS_PER_ROW = 10;
    public static final double BLOCKS_START_Y_OFFSET = 80;

    /** Row colors from top to bottom */
    public static final List<Color> ROW_COLORS = List.of(
            Color.RED, Color.ORANGE, Color.YELLOW,
            Color.GREEN, Color.CYAN, Color.BLUE);

    /** Hit points per row (top rows are harder) */
    public static final List<Integer> ROW_HIT_POINTS = List.of(3, 3, 2, 2, 1, 1);

    // ==================== Paddle ====================
    public static final double PADDLE_WIDTH = 100;
    public static final double PADDLE_HEIGHT = 20;
    public static final double PADDLE_SPEED = 350;
    public static final double PADDLE_Y_OFFSET = 0;

    // ==================== Ball ====================
    public static final double BALL_RADIUS = 7.0;
    public static final double BALL_SPEED = 400;
    public static final double BALL_LAUNCH_ANGLE = 20;
    public static final Color BALL_COLOR = Color.WHITE;

    // ==================== Power-ups ====================
    public static final double POWERUP_SIZE = 30;
    public static final double POWERUP_FALL_SPEED = 150.0;
    /** Probability (0.0-1.0) of spawning a power-up when a block is destroyed */
    public static final double POWERUP_SPAWN_CHANCE = 0.35;
    public static final double PADDLE_WIDEN_FACTOR = 1.5;
    public static final int MULTI_BALL_COUNT = 2;

    // Collision & Physics Constants
    public static final double BALL_COLLISION_OFFSET = 1.0;
    public static final double PADDLE_BOUNCE_ANGLE_MAX = 60.0;
    public static final double PADDLE_EDGE_BUFFER = 2.0;

    public static final double BLOCK_EPSILON = 1e-3;

    // ==================== Scoring ====================
    public static final int POINTS_PER_BLOCK = 50;

    // ==================== Lives ====================
    public static final int STARTING_LIVES = 3;

    // ==================== HUD ====================
    public static final double HUD_HEIGHT = 40;
    public static final int HUD_FONT_SIZE = 20;
    public static final String HUD_FONT_NAME = "Verdana";
    public static final double HUD_PADDING_X = 20;

    public static final int HUD_LEVEL_NAME_FONT_SIZE = 22;

    public static final int HUD_MAX_INDIVIDUAL_HEARTS = 3;
    public static final double HUD_HEART_SIZE = 20;
    public static final double HUD_HEART_SPACING = 25;

    public static final int HUD_SCORE_FONT_SIZE = 20;
    public static final String HUD_SCORE_FONT_NAME = "Verdana";

    // ==================== Floating Text ====================
    public static final double FLOATING_TEXT_LIFETIME = 1.0;
    // ==================== Main Menu ====================
    public static final double MENU_TITLE_Y_PERCENT = 0.15;
    public static final double MENU_SUN_Y_PERCENT = 0.6;
    public static final double MENU_SUN_SIZE = 150;
    public static final double MENU_BUTTON_START_Y_PERCENT = 0.7;
    public static final double MENU_BUTTON_GAP = 60;
    public static final double MENU_BUTTON_WIDTH = 200;
    public static final double MENU_BUTTON_HEIGHT = 40;
    public static final double MENU_BUTTON_CORNER = 10;
    public static final double MENU_INSTRUCTIONS_Y_PERCENT = 0.3;
    public static final double MENU_INSTRUCTIONS_GAP_Y = 50;
    public static final double MENU_INSTRUCTIONS_X_DIVISOR = 2.1;
    public static final int MENU_TITLE_FONT_SIZE = 80;
    public static final int MENU_BUTTON_FONT_SIZE = 20;
    public static final int MENU_INSTRUCTION_ACTION_FONT_SIZE = 18;
    public static final int MENU_INSTRUCTION_KEY_FONT_SIZE = 16;

    public static final double FLOATING_TEXT_SPEED = 50.0;

    // ==================== Pause Screen ====================
    public static final double PAUSE_BOX_WIDTH = 400;
    public static final double PAUSE_BOX_HEIGHT = 300;
    public static final double PAUSE_BOX_ARC = 30;
    public static final double PAUSE_TITLE_Y_OFFSET = 60;
    public static final double PAUSE_OPTIONS_START_Y_OFFSET = 140;
    public static final double PAUSE_OPTIONS_GAP_Y = 60;
    public static final int PAUSE_TITLE_FONT_SIZE = 60;
    public static final int PAUSE_OPTION_ACTION_FONT_SIZE = 20;
    public static final int PAUSE_OPTION_KEY_FONT_SIZE = 20;

    // ==================== End Screen ====================
    public static final int END_TITLE_FONT_SIZE = 50;
    public static final int END_MESSAGE_FONT_SIZE = 24;
    public static final int END_PROMPT_FONT_SIZE = 18;

    // ==================== Rendering ====================
    /** Block bevel size for 3D effect */
    public static final double BLOCK_BEVEL_SIZE = 4;
    /** Block hit points font size */
    public static final int BLOCK_HP_FONT_SIZE = 14;

    /** Floating text font size */
    public static final int FLOATING_TEXT_FONT_SIZE = 14;

    public static final String DEFAULT_FONT_FAMILY = "Arial";
}
