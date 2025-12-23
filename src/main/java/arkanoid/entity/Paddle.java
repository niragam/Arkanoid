package arkanoid.entity;

import javafx.scene.input.KeyCode;

import arkanoid.core.Game;
import arkanoid.core.GameConfig;
import arkanoid.geometry.Point;
import arkanoid.geometry.Rectangle;
import arkanoid.util.InputState;
import arkanoid.util.Velocity;

/**
 * The paddle is controlled by the player and moves horizontally.
 */
public class Paddle implements Sprite, Collidable {

    private final double speed;
    private final double leftBound;
    private final double rightBound;
    private final KeyCode moveLeftKey;
    private final KeyCode moveRightKey;
    private final InputState input;

    // Store coordinates as primitives to reduce GC pressure
    private double x;
    private double y;
    private double width;
    private double height;
    private Runnable onHit;

    /**
     * Set callback for when paddle is hit.
     * 
     * @param onHit callback
     */
    public void setOnHit(Runnable onHit) {
        this.onHit = onHit;
    }

    /**
     * Create a paddle.
     *
     * @param rect         initial rectangle
     * @param speed        movement speed in pixels per second
     * @param leftBound    minimum x the paddle can reach
     * @param rightBound   maximum x the paddle can reach (right edge)
     * @param moveLeftKey  key for moving left
     * @param moveRightKey key for moving right
     * @param input        input state
     */
    public Paddle(Rectangle rect, double speed, double leftBound, double rightBound,
            KeyCode moveLeftKey, KeyCode moveRightKey, InputState input) {
        this.x = rect.getUpperLeft().getX();
        this.y = rect.getUpperLeft().getY();
        this.width = rect.getWidth();
        this.height = rect.getHeight();
        this.speed = speed;
        this.leftBound = leftBound;
        this.rightBound = rightBound;
        this.moveLeftKey = moveLeftKey;
        this.moveRightKey = moveRightKey;
        this.input = input;
    }

    /**
     * Move the paddle to the left.
     *
     * @param dt time passed
     */
    public void moveLeft(double dt) {
        this.x -= speed * dt;
        if (this.x < leftBound) {
            this.x = leftBound;
        }
    }

    /**
     * Move the paddle to the right.
     *
     * @param dt time passed
     */
    public void moveRight(double dt) {
        this.x += speed * dt;
        if (this.x + this.width > rightBound) {
            this.x = rightBound - this.width;
        }
    }

    @Override
    public void update(double dt) {
        if (input.isPressed(moveLeftKey)) {
            moveLeft(dt);
        }
        if (input.isPressed(moveRightKey)) {
            moveRight(dt);
        }
    }

    @Override
    public Rectangle getCollisionRectangle() {
        return new Rectangle(new Point(x, y), width, height);
    }

    @Override
    public Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity) {
        if (onHit != null) {
            onHit.run();
        }
        double speedMagnitude = Math.sqrt(
                currentVelocity.getDx() * currentVelocity.getDx()
                        + currentVelocity.getDy() * currentVelocity.getDy());

        // If hit is on the sides (not the top), reflect horizontally
        if (collisionPoint.getY() > this.y + 1) {
            // Push the ball out of the paddle to avoid getting stuck
            double ballRadius = hitter.getRadius();
            double newX;
            if (collisionPoint.getX() < this.x + this.width / 2) {
                // Left side hit - push to left
                newX = this.x - ballRadius - GameConfig.PADDLE_EDGE_BUFFER;
            } else {
                // Right side hit - push to right
                newX = this.x + this.width + ballRadius + GameConfig.PADDLE_EDGE_BUFFER;
            }
            hitter.setCenter(new Point(newX, hitter.getCenter().getY()));

            return new Velocity(-currentVelocity.getDx(), currentVelocity.getDy());
        }

        // If ball is moving upward (already bouncing up), just reflect vertically
        if (currentVelocity.getDy() < 0) {
            return new Velocity(currentVelocity.getDx(), -currentVelocity.getDy());
        }

        // Normal top hit - use linear interpolation based on hit position
        double hitX = collisionPoint.getX();
        double paddleCenter = this.x + this.width / 2;
        double distanceFromCenter = hitX - paddleCenter;

        // Normalize distance to [-1, 1] relative to half-width
        double normalizedHit = distanceFromCenter / (this.width / 2);

        // Clamp to [-1, 1] just in case
        if (normalizedHit < -1) {
            normalizedHit = -1;
        }
        if (normalizedHit > 1) {
            normalizedHit = 1;
        }

        // Map to angle: 0 (center) -> 0 degrees (up)
        // -1 (left) -> -60 degrees (left)
        // +1 (right) -> +60 degrees (right)
        double bounceAngle = normalizedHit * GameConfig.PADDLE_BOUNCE_ANGLE_MAX; // Max angle 60 degrees

        return Velocity.fromAngleAndSpeed(bounceAngle, speedMagnitude);
    }

    /**
     * Add the paddle to the game.
     *
     * @param g game
     */
    public void addToGame(Game g) {
        g.addSprite(this);
        g.addCollidable(this);
    }

    /**
     * Widen the paddle by a factor.
     *
     * @param factor multiplication factor for width
     */
    public void widen(double factor) {
        double newWidth = this.width * factor;
        // Cap max width
        if (newWidth > rightBound - leftBound - 20) {
            newWidth = rightBound - leftBound - 20;
        }
        // Center the wider paddle
        double centerX = this.x + this.width / 2;
        double newX = centerX - newWidth / 2;
        // Keep within bounds
        if (newX < leftBound) {
            newX = leftBound;
        }
        if (newX + newWidth > rightBound) {
            newX = rightBound - newWidth;
        }
        this.x = newX;
        this.width = newWidth;
    }

}
