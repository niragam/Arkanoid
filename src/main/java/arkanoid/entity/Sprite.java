package arkanoid.entity;

/**
 * A sprite is a game object that can be updated.
 * Drawing is now handled by the GameRenderer.
 */
public interface Sprite {
    /**
     * Update the sprite's state.
     *
     * @param dt time passed in seconds since last frame
     */
    void update(double dt);
}
