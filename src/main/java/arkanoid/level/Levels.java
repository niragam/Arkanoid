package arkanoid.level;

import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.List;

import arkanoid.core.GameConfig;

/**
 * Factory class that creates all game levels.
 * Centralizes level definitions for easy management.
 */
public final class Levels {

    private Levels() {
        // Utility class - no instantiation
    }

    /**
     * Get all game levels in order of increasing difficulty.
     *
     * @return list of all levels
     */
    public static List<LevelInformation> all() {
        int width = GameConfig.SCREEN_WIDTH;
        int height = GameConfig.SCREEN_HEIGHT;

        return Arrays.asList(
                level1(width, height),
                level2(width, height),
                level3(width, height),
                level4(width, height));
    }

    /**
     * Level 1: Super easy - just 1 row of 5 blocks, all 1 hit.
     */
    public static LevelInformation level1(int width, int height) {
        Background bg = new StarBackground(width, height);
        return new BasicLevel(width, height, 1, 5,
                List.of(1),
                List.of(Color.LIGHTGREEN),
                "Easy Start", bg);
    }

    /**
     * Level 2: Still easy - 7 rows of 10 blocks.
     */
    public static LevelInformation level2(int width, int height) {
        Background bg = new SunBackground(width, height);

        return new BasicLevel(width, height, 7, 10,
                List.of(1, 1, 1, 1, 1, 1, 1),
                List.of(Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.INDIGO,
                        Color.VIOLET),
                "Rainbow", bg);
    }

    /**
     * Level 3: Medium - 3 rows with some 2-hit blocks.
     */
    public static LevelInformation level3(int width, int height) {
        Background bg = new GridBackground();

        return new BasicLevel(width, height, 3, 8,
                List.of(1, 2, 1),
                List.of(Color.CYAN, Color.BLUE, Color.PURPLE),
                "Getting Tough", bg);
    }

    /**
     * Level 4: Hard - full layout with multi-hit blocks.
     */
    public static LevelInformation level4(int width, int height) {
        Background bg = new FireBackground(width, height);

        return new BasicLevel(width, height, 5, 10,
                List.of(3, 2, 2, 1, 1),
                List.of(Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.CYAN),
                "The Challenge", bg);
    }

}
