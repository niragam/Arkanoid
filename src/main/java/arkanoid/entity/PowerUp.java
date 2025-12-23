package arkanoid.entity;

import javafx.scene.paint.Color;

import arkanoid.core.Game;
import arkanoid.core.GameConfig;
import arkanoid.geometry.Point;
import arkanoid.geometry.Rectangle;

/**
 * A power-up that falls from destroyed blocks and can be collected by the
 * paddle.
 */
public class PowerUp implements Sprite {

    /**
     * Power-up types with their visual properties and effects.
     * Each type encapsulates its own effect logic (Open/Closed Principle).
     */
    public enum Type {
        EXTRA_LIFE(Color.RED, game -> game.getLivesCounter().increase(1)),
        WIDE_PADDLE(Color.LIGHTBLUE, game -> game.getPaddle().widen(GameConfig.PADDLE_WIDEN_FACTOR)),
        MULTI_BALL(Color.ORANGE, game -> game.spawnExtraBalls(GameConfig.MULTI_BALL_COUNT));

        private final Color color;
        private final PowerUpEffect effect;

        Type(Color color, PowerUpEffect effect) {
            this.color = color;
            this.effect = effect;
        }

        public Color getColor() {
            return color;
        }

        /**
         * Apply this power-up's effect to the game.
         *
         * @param game the game instance
         */
        public void applyEffect(Game game) {
            effect.apply(game);
        }
    }

    // Store coordinates as primitives to reduce GC pressure
    private double centerX;
    private double centerY;
    private final double size = GameConfig.POWERUP_SIZE;
    private final double fallSpeed = GameConfig.POWERUP_FALL_SPEED;
    private final Type type;
    private boolean collected;

    /**
     * Create a power-up.
     *
     * @param center center position
     * @param type   type of power-up
     */
    public PowerUp(Point center, Type type) {
        this.centerX = center.getX();
        this.centerY = center.getY();
        this.type = type;
    }

    /**
     * Get the power-up type.
     *
     * @return type
     */
    public Type getType() {
        return type;
    }

    /**
     * Check if collected.
     *
     * @return true if collected
     */
    public boolean isCollected() {
        return collected;
    }

    /**
     * Mark as collected.
     */
    public void collect() {
        collected = true;
    }

    /**
     * Get center position.
     *
     * @return center
     */
    public Point getCenter() {
        return new Point(centerX, centerY);
    }

    /**
     * Get size.
     *
     * @return size
     */
    public double getSize() {
        return size;
    }

    /**
     * Get bounding rectangle for collision detection.
     *
     * @return bounding rectangle
     */
    public Rectangle getBounds() {
        return new Rectangle(
                new Point(centerX - size / 2, centerY - size / 2),
                size, size);
    }

    /**
     * Apply the effect of this power-up to the game.
     * Delegates to the Type's encapsulated effect (Open/Closed Principle).
     *
     * @param game the game instance
     */
    public void applyEffect(Game game) {
        type.applyEffect(game);
    }

    @Override
    public void update(double dt) {
        if (collected) {
            return;
        }

        // Move down
        centerY += fallSpeed * dt;

        // Check if collected by paddle
        // Collision logic is handled by Game.java
    }

}
