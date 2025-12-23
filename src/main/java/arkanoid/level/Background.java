package arkanoid.level;

import javafx.scene.canvas.GraphicsContext;

/**
 * Interface for drawing a level background with time-based animation.
 */
public interface Background {
    /**
     * Draw the background.
     *
     * @param gc     graphics context
     * @param width  screen width
     * @param height screen height
     * @param dt     delta time in seconds since last frame
     */
    void draw(GraphicsContext gc, int width, int height, double dt);
}
