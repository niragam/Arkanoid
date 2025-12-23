package arkanoid.entity;

import javafx.scene.paint.Color;

import arkanoid.core.GameConfig;
import arkanoid.geometry.Point;

/**
 * A temporary text sprite that floats up and fades out.
 */
public class FloatingText implements Sprite {

    // Store coordinates as primitives to reduce GC pressure
    private double posX;
    private double posY;
    private final String text;
    private final Color color;
    private double lifeTime;
    private final double maxLifeTime = GameConfig.FLOATING_TEXT_LIFETIME;
    private boolean stop;

    /**
     * Create a floating text.
     *
     * @param position start position
     * @param text     text to display
     * @param color    text color
     */
    public FloatingText(Point position, String text, Color color) {
        this.posX = position.getX();
        this.posY = position.getY();
        this.text = text;
        this.color = color;
        this.lifeTime = 0;
    }

    @Override
    public void update(double dt) {
        lifeTime += dt;
        if (lifeTime >= maxLifeTime) {
            stop = true;
        }
        // Float up
        posY -= GameConfig.FLOATING_TEXT_SPEED * dt;
    }

    /**
     * Check if text should be removed (lifetime expired).
     *
     * @return true if should remove
     */
    public boolean shouldRemove() {
        return stop;
    }

    /**
     * Get the current position.
     *
     * @return position
     */
    public Point getPosition() {
        return new Point(posX, posY);
    }

    /**
     * Get the text content.
     *
     * @return text
     */
    public String getText() {
        return text;
    }

    /**
     * Get the text color.
     *
     * @return color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Get the current lifetime (how long it has been alive).
     *
     * @return lifetime in seconds
     */
    public double getLifeTime() {
        return lifeTime;
    }

    /**
     * Get the maximum lifetime.
     *
     * @return max lifetime in seconds
     */
    public double getMaxLifeTime() {
        return maxLifeTime;
    }
}
