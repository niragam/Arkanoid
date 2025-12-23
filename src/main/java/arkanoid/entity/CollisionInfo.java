package arkanoid.entity;

import arkanoid.geometry.Point;

/**
 * Holds information about a collision: the point and the object hit.
 */
public record CollisionInfo(Point collisionPoint, Collidable collisionObject) {
}
