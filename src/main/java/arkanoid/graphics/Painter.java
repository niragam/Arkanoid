package arkanoid.graphics;

import javafx.scene.canvas.GraphicsContext;

/**
 * Interface for painting a specific type of object.
 *
 * @param <T> the type of object this painter can draw
 */
public interface Painter<T> {
    /**
     * Draw the object.
     *
     * @param gc     graphics context
     * @param object object to draw
     */
    void paint(GraphicsContext gc, T object);
}
