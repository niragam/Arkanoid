package arkanoid.event;

import arkanoid.core.Game;
import arkanoid.entity.Ball;
import arkanoid.entity.Block;
import arkanoid.util.Counter;

/**
 * A HitListener that removes balls from the game when they hit a specific
 * region (typically the bottom "death" block).
 */
public class BallRemover implements HitListener {

    private final Game game;
    private final Counter remainingBalls;

    /**
     * Create a ball remover.
     *
     * @param game           game to remove balls from
     * @param remainingBalls counter for remaining balls
     */
    public BallRemover(Game game, Counter remainingBalls) {
        this.game = game;
        this.remainingBalls = remainingBalls;
    }

    @Override
    public void hitEvent(Block beingHit, Ball hitter) {
        game.removeSprite(hitter);
        game.removeBall(hitter);
        remainingBalls.decrease(1);
    }
}
