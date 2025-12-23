package arkanoid.level;

import javafx.scene.canvas.GraphicsContext;

import arkanoid.core.Game;
import arkanoid.util.Counter;
import arkanoid.util.InputState;

/**
 * Minimal level descriptor interface.
 *
 * For now it is only a thin wrapper used to build a Game instance.
 * You can extend it later with ball count, paddle speed/size, and block layout.
 */
public interface LevelInformation {

    /**
     * Get the background for this level.
     *
     * @return background
     */

    Background getBackground();

    /**
     * Build a Game instance for this level using the given graphics context.
     *
     * @param gc    graphics context
     * @param input shared input state
     * @param score global score counter
     * @param lives global lives counter
     * @return game instance for this level
     */
    Game createGame(GraphicsContext gc, InputState input, Counter score, Counter lives);
}
