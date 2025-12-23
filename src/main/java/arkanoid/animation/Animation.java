package arkanoid.animation;

import javafx.scene.canvas.GraphicsContext;

/**
 * A generic animation: something that can draw a frame and knows when to stop.
 */
public interface Animation {

    /**
     * Draw one frame of the animation.
     *
     * @param gc graphics context
     * @param dt time passed in seconds since last frame
     */
    void doOneFrame(GraphicsContext gc, double dt);

    /**
     * @return true if the animation should stop.
     */
    boolean shouldStop();

    /**
     * Set a callback to run when the animation completes.
     *
     * @param callback the callback to run
     */
    default void setOnComplete(Runnable callback) {
        // Default implementation does nothing - subclasses can override
    }

    /**
     * Get the completion callback.
     *
     * @return the callback to run when animation completes, or null
     */
    default Runnable getOnComplete() {
        return null;
    }
}
