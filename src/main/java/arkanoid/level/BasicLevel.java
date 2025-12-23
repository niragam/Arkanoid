package arkanoid.level;

import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import arkanoid.core.Game;
import arkanoid.core.GameConfig;
import arkanoid.util.Counter;
import arkanoid.util.InputState;

/**
 * A configurable level implementation with customizable block layout.
 */
public class BasicLevel implements LevelInformation {

    private final int width;
    private final int height;
    private final int rows;
    private final int blocksPerRow;
    private final List<Integer> hitPointsPerRow;
    private final List<Color> rowColors;
    private final String name;
    private final Background background;

    /**
     * Create a level with default configuration.
     */
    public BasicLevel(int width, int height, Background background) {
        this(width, height, GameConfig.DEFAULT_ROWS, GameConfig.BLOCKS_PER_ROW,
                GameConfig.ROW_HIT_POINTS, GameConfig.ROW_COLORS, "Level", background);
    }

    /**
     * Create a level with custom block layout.
     *
     * @param width           screen width
     * @param height          screen height
     * @param rows            number of block rows
     * @param blocksPerRow    blocks per row
     * @param hitPointsPerRow hit points for each row (cycled if fewer than rows)
     * @param rowColors       colors for each row (cycled if fewer than rows)
     * @param name            level name
     * @param background      level background
     * @throws IllegalArgumentException if parameters are invalid
     */
    public BasicLevel(int width, int height, int rows, int blocksPerRow,
            List<Integer> hitPointsPerRow, List<Color> rowColors, String name, Background background) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Width and height must be positive");
        }
        if (rows <= 0 || blocksPerRow <= 0) {
            throw new IllegalArgumentException("Rows and blocksPerRow must be positive");
        }
        if (hitPointsPerRow == null || hitPointsPerRow.isEmpty()) {
            throw new IllegalArgumentException("hitPointsPerRow cannot be null or empty");
        }
        if (rowColors == null || rowColors.isEmpty()) {
            throw new IllegalArgumentException("rowColors cannot be null or empty");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Level name cannot be null or blank");
        }
        
        this.width = width;
        this.height = height;
        this.rows = rows;
        this.blocksPerRow = blocksPerRow;
        this.hitPointsPerRow = hitPointsPerRow;
        this.rowColors = rowColors;
        this.name = name;
        this.background = background;
    }

    @Override
    public Game createGame(GraphicsContext gc, InputState input, Counter score, Counter lives) {
        return new Game(width, height, gc, input, score, lives,
                rows, blocksPerRow, hitPointsPerRow, rowColors, background, name);
    }

    @Override
    public Background getBackground() {
        return background;
    }
}
