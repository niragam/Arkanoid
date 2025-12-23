package arkanoid.event;

import arkanoid.entity.Ball;
import arkanoid.entity.Block;

/**
 * Listener for hit events - called when a block is hit by a ball.
 */
public interface HitListener {
    /**
     * Called whenever a block is hit by a ball.
     *
     * @param beingHit the block that was hit
     * @param hitter   the ball that hit the block
     */
    void hitEvent(Block beingHit, Ball hitter);
}
