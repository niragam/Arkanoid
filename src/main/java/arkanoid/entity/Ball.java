package arkanoid.entity;

import javafx.scene.paint.Color;

import arkanoid.core.GameConfig;
import arkanoid.geometry.Line;
import arkanoid.geometry.Point;
import arkanoid.util.Velocity;

/**
 * A ball with position, radius, color, and velocity.
 */
public class Ball implements Sprite {

    // Store coordinates as primitives to reduce GC pressure
    private double centerX;
    private double centerY;
    private double radius;
    private Color color;
    private double velocityDx;
    private double velocityDy;
    private GameEnvironment environment;

    // For ball-on-paddle launch mechanic
    private Paddle attachedPaddle;
    private double paddleOffset; // horizontal offset from paddle center

    /**
     * Construct a ball.
     *
     * @param center center point
     * @param r      radius
     * @param color  color
     * @throws IllegalArgumentException if parameters are invalid
     */
    public Ball(Point center, double r, Color color) {
        if (center == null) {
            throw new IllegalArgumentException("Center cannot be null");
        }
        if (r <= 0) {
            throw new IllegalArgumentException("Radius must be positive");
        }
        if (color == null) {
            throw new IllegalArgumentException("Color cannot be null");
        }
        this.centerX = center.getX();
        this.centerY = center.getY();
        this.radius = r;
        this.color = color;
        this.velocityDx = 0;
        this.velocityDy = 0;
    }

    /**
     * Get the radius of the ball.
     *
     * @return radius
     */
    public double getRadius() {
        return this.radius;
    }

    /**
     * Get the color of the ball.
     *
     * @return color
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Set the velocity of the ball.
     *
     * @param v new velocity
     */
    public void setVelocity(Velocity v) {
        if (v == null) {
            this.velocityDx = 0;
            this.velocityDy = 0;
        } else {
            this.velocityDx = v.getDx();
            this.velocityDy = v.getDy();
        }
    }

    /**
     * Set the velocity of the ball given dx and dy.
     *
     * @param dx change in x
     * @param dy change in y
     */
    public void setVelocity(double dx, double dy) {
        this.velocityDx = dx;
        this.velocityDy = dy;
    }

    /**
     * Set the game environment for collision detection.
     *
     * @param environment game environment
     */
    public void setEnvironment(GameEnvironment environment) {
        java.util.Objects.requireNonNull(environment, "GameEnvironment cannot be null");
        this.environment = environment;
    }

    /**
     * Get the center point of the ball.
     *
     * @return center point
     */
    public Point getCenter() {
        return new Point(this.centerX, this.centerY);
    }

    /**
     * Set the center point of the ball.
     *
     * @param center new center point
     */
    public void setCenter(Point center) {
        java.util.Objects.requireNonNull(center, "Center cannot be null");
        this.centerX = center.getX();
        this.centerY = center.getY();
    }

    /**
     * Attach ball to paddle (ball will follow paddle until launched).
     *
     * @param paddle the paddle to attach to
     */
    public void attachToPaddle(Paddle paddle) {
        this.attachedPaddle = paddle;
        // Calculate offset from paddle center
        double paddleCenterX = paddle.getCollisionRectangle().getUpperLeft().getX()
                + paddle.getCollisionRectangle().getWidth() / 2;
        this.paddleOffset = this.centerX - paddleCenterX;
    }

    /**
     * Launch ball from paddle with given velocity.
     *
     * @param launchVelocity velocity to launch with
     */
    public void launch(Velocity launchVelocity) {
        this.attachedPaddle = null;
        this.velocityDx = launchVelocity.getDx();
        this.velocityDy = launchVelocity.getDy();
    }

    /**
     * Check if ball is attached to paddle.
     *
     * @return true if attached
     */
    public boolean isAttached() {
        return this.attachedPaddle != null;
    }

    /**
     * Move the ball one step according to its velocity, taking collisions into
     * account if a GameEnvironment is set.
     */
    public void moveOneStep(double dt) {

        // If attached to paddle, follow it
        if (attachedPaddle != null) {
            double paddleCenterX = attachedPaddle.getCollisionRectangle().getUpperLeft().getX()
                    + attachedPaddle.getCollisionRectangle().getWidth() / 2;
            double paddleTop = attachedPaddle.getCollisionRectangle().getUpperLeft().getY();
            this.centerX = paddleCenterX + paddleOffset;
            this.centerY = paddleTop - this.radius;
            return;
        }

        // If no environment is set, we cannot check collisions.
        // This is likely an error in game setup.
        if (this.environment == null) {
            throw new IllegalStateException("Ball environment not set. Cannot move with collision detection.");
        }

        double dx = this.velocityDx * dt;
        double dy = this.velocityDy * dt;
        double nextX = this.centerX + dx;
        double nextY = this.centerY + dy;

        // Create trajectory line for collision detection
        Point currentPos = new Point(this.centerX, this.centerY);
        Point nextPos = new Point(nextX, nextY);
        Line trajectory = new Line(currentPos, nextPos);

        CollisionInfo info = environment.getClosestCollision(trajectory);
        if (info == null) {
            // No collision; move normally.
            this.centerX = nextX;
            this.centerY = nextY;
        } else {
            Point collisionPoint = info.collisionPoint();
            Collidable collidable = info.collisionObject();

            // Move the ball to just before the collision point along the trajectory.
            // Use radius + small epsilon to ensure the ball's edge doesn't penetrate
            double speed = Math.sqrt(dx * dx + dy * dy);
            double offset = this.radius + GameConfig.BALL_COLLISION_OFFSET; // Use radius to prevent penetration

            if (speed > 0) {
                // Offset along the normalized trajectory direction (away from collision)
                this.centerX = collisionPoint.getX() - (dx / speed) * offset;
                this.centerY = collisionPoint.getY() - (dy / speed) * offset;
            } else {
                // Zero velocity edge case - stay at collision point
                this.centerX = collisionPoint.getX();
                this.centerY = collisionPoint.getY();
            }

            // Let the collidable compute new velocity.
            Velocity currentVel = new Velocity(this.velocityDx, this.velocityDy);
            Velocity newVel = collidable.hit(this, collisionPoint, currentVel);
            this.velocityDx = newVel.getDx();
            this.velocityDy = newVel.getDy();
        }
    }

    @Override
    public void update(double dt) {
        moveOneStep(dt);
    }
}
