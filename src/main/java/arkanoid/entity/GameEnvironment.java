package arkanoid.entity;

import arkanoid.geometry.Line;
import arkanoid.geometry.Point;
import arkanoid.geometry.Rectangle;

import java.util.ArrayList;
import java.util.List;

/**
 * The game environment holds all collidable objects and can answer collision
 * queries for trajectories.
 */
public class GameEnvironment {

    private final List<Collidable> collidables = new ArrayList<>();

    /**
     * Add a collidable to the environment.
     *
     * @param c collidable to add
     */
    public void addCollidable(Collidable c) {
        java.util.Objects.requireNonNull(c, "Collidable cannot be null");
        this.collidables.add(c);
    }

    /**
     * Remove a collidable from the environment.
     *
     * @param c collidable to remove
     */
    public void removeCollidable(Collidable c) {
        java.util.Objects.requireNonNull(c, "Collidable cannot be null");
        this.collidables.remove(c);
    }

    /**
     * Given a trajectory, return info about the closest collision that is going to
     * occur,
     * or null if no collision occurs.
     *
     * @param trajectory line representing movement
     * @return closest collision info or null
     */
    public CollisionInfo getClosestCollision(Line trajectory) {
        Point closestPoint = null;
        Collidable closestObject = null;
        double minDistance = Double.POSITIVE_INFINITY;

        for (Collidable collidable : collidables) {
            Rectangle rect = collidable.getCollisionRectangle();
            Point intersection = trajectory.closestIntersectionToStartOfLine(rect);
            if (intersection != null) {
                double distance = trajectory.start().distance(intersection);
                if (distance < minDistance) {
                    minDistance = distance;
                    closestPoint = intersection;
                    closestObject = collidable;
                }
            }
        }

        if (closestPoint == null) {
            return null;
        }
        return new CollisionInfo(closestPoint, closestObject);
    }
}
