package arkanoid.entity;

import arkanoid.geometry.Point;
import arkanoid.geometry.Rectangle;
import arkanoid.util.Velocity;

/**
 * An object that can be collided with by a Ball.
 */
public interface Collidable {

    /**
     * Return the "collision shape" of the object.
     *
     * @return collision rectangle
     */
    Rectangle getCollisionRectangle();

    /**
     * Notify the object that a collision occurred at collisionPoint with a ball
     * moving at currentVelocity. Return the new velocity expected after the hit.
     *
     * @param hitter          the ball that hit this object
     * @param collisionPoint  point of collision
     * @param currentVelocity current velocity before collision
     * @return new velocity after the hit
     */
    Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity);
}
