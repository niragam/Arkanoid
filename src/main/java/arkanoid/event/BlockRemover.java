package arkanoid.event;

import arkanoid.core.Game;
import arkanoid.entity.Ball;
import arkanoid.entity.Block;
import arkanoid.util.Counter;

/**
 * A HitListener that removes blocks from the game and keeps count.
 */
public class BlockRemover implements HitListener {

    private final Game game;
    private final Counter remainingBlocks;

    /**
     * Create a block remover.
     *
     * @param game            game to remove blocks from
     * @param remainingBlocks counter for remaining blocks
     */
    public BlockRemover(Game game, Counter remainingBlocks) {
        this.game = game;
        this.remainingBlocks = remainingBlocks;
    }

    @Override
    public void hitEvent(Block beingHit, Ball hitter) {
        // Only remove block when hit points reach 0 and it's not indestructible
        if (beingHit.isIndestructible()) {
            return;
        }
        if (beingHit.getHitPoints() <= 0) {
            beingHit.removeFromGame(game);
            remainingBlocks.decrease(1);

        }
    }
}
