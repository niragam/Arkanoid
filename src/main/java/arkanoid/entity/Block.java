package arkanoid.entity;

import javafx.scene.paint.Color;

import arkanoid.core.Game;
import arkanoid.core.GameConfig;
import arkanoid.event.HitListener;
import arkanoid.event.HitNotifier;
import arkanoid.geometry.Point;
import arkanoid.geometry.Rectangle;
import arkanoid.util.Velocity;

import java.util.ArrayList;
import java.util.List;

/**
 * A rectangular block that can be drawn, collided with, and act as a game
 * object.
 */
public class Block implements Collidable, Sprite, HitNotifier {

    private static final double EPSILON = GameConfig.BLOCK_EPSILON;

    private final Rectangle rect;
    private Color color; // Mutable: changes as block takes damage
    private int hitPoints;
    private final boolean indestructible;
    private final List<HitListener> hitListeners = new ArrayList<>();

    /**
     * Create a block from a rectangle and color (1 hit point, destructible).
     *
     * @param rect  underlying rectangle
     * @param color color
     */
    public Block(Rectangle rect, Color color) {
        this(rect, color, 1, false);
    }

    /**
     * Create a block with specified hit points.
     *
     * @param rect      underlying rectangle
     * @param color     color
     * @param hitPoints number of hits required to destroy
     */
    public Block(Rectangle rect, Color color, int hitPoints) {
        this(rect, color, hitPoints, false);
    }

    /**
     * Create a block with full customization.
     *
     * @param rect           underlying rectangle
     * @param color          color
     * @param hitPoints      number of hits required to destroy
     * @param indestructible if true, block cannot be destroyed
     * @throws IllegalArgumentException if parameters are invalid
     */
    public Block(Rectangle rect, Color color, int hitPoints, boolean indestructible) {
        if (rect == null) {
            throw new IllegalArgumentException("Rectangle cannot be null");
        }
        if (color == null) {
            throw new IllegalArgumentException("Color cannot be null");
        }
        if (hitPoints < 1) {
            throw new IllegalArgumentException("Hit points must be at least 1");
        }
        this.rect = rect;
        this.color = color;
        this.hitPoints = hitPoints;
        this.indestructible = indestructible;
    }

    @Override
    public Rectangle getCollisionRectangle() {
        return rect;
    }

    /**
     * Get remaining hit points.
     *
     * @return hit points remaining
     */
    public int getHitPoints() {
        return hitPoints;
    }

    /**
     * Check if block is indestructible.
     *
     * @return true if indestructible
     */
    public boolean isIndestructible() {
        return indestructible;
    }

    @Override
    public Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity) {
        double dx = currentVelocity.getDx();
        double dy = currentVelocity.getDy();

        double x = collisionPoint.getX();
        double y = collisionPoint.getY();

        double left = rect.getUpperLeft().getX();
        double right = left + rect.getWidth();
        double top = rect.getUpperLeft().getY();
        double bottom = top + rect.getHeight();

        boolean hitLeft = Math.abs(x - left) < EPSILON;
        boolean hitRight = Math.abs(x - right) < EPSILON;
        boolean hitTop = Math.abs(y - top) < EPSILON;
        boolean hitBottom = Math.abs(y - bottom) < EPSILON;

        // Handle corner hits: prioritize the side that the ball was approaching from
        // based on its velocity direction to avoid double-negation
        boolean hitHorizontalEdge = hitLeft || hitRight;
        boolean hitVerticalEdge = hitTop || hitBottom;

        if (hitHorizontalEdge && hitVerticalEdge) {
            // Corner hit: determine which edge to bounce off based on velocity
            // If moving more horizontally, bounce off vertical edge (reverse dx)
            // If moving more vertically, bounce off horizontal edge (reverse dy)
            if (Math.abs(currentVelocity.getDx()) > Math.abs(currentVelocity.getDy())) {
                dx = -dx;
            } else {
                dy = -dy;
            }
        } else {
            if (hitHorizontalEdge) {
                dx = -dx;
            }
            if (hitVerticalEdge) {
                dy = -dy;
            }
        }

        Velocity newVelocity = new Velocity(dx, dy);

        // Reduce hit points if not indestructible
        if (!indestructible && hitPoints > 0) {
            hitPoints--;
            updateColorForHitPoints();
        }

        // Notify listeners after the hit is processed.
        notifyHit(hitter);
        return newVelocity;
    }

    /**
     * Update block color based on remaining hit points.
     */
    private void updateColorForHitPoints() {
        // Darken the color each time block is hit (while still alive)
        if (hitPoints > 0) {
            this.color = color.darker();
        }
        // hitPoints == 0 means block will be removed
    }

    /**
     * Get the color of the block.
     *
     * @return color
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Update the block state.
     * Blocks are static, so this method is currently empty.
     *
     * @param dt time passed
     */
    @Override
    public void update(double dt) {
        // Blocks are static, nothing to update for now
    }

    /**
     * Add this block to the given game as both a sprite and a collidable.
     *
     * @param game game to add to
     */
    public void addToGame(Game game) {
        game.addSprite(this);
        game.addCollidable(this);
    }

    /**
     * Remove this block from both sprite and collidable collections.
     *
     * @param game game to remove from
     */
    public void removeFromGame(Game game) {
        game.removeSprite(this);
        game.removeCollidable(this);
    }

    @Override
    public void addHitListener(HitListener hl) {
        hitListeners.add(hl);
    }

    @Override
    public void removeHitListener(HitListener hl) {
        hitListeners.remove(hl);
    }

    /**
     * Notify all registered listeners of a hit event.
     *
     * @param hitter the ball that hit this block
     */
    private void notifyHit(Ball hitter) {
        List<HitListener> listeners = new ArrayList<>(hitListeners);
        for (HitListener hl : listeners) {
            hl.hitEvent(this, hitter);
        }
    }
}
