package arkanoid.event;

import arkanoid.core.Game;
import arkanoid.entity.Ball;
import arkanoid.entity.Block;
import arkanoid.util.Counter;

/**
 * Updates score when blocks are hit/removed.
 */
public class ScoreTrackingListener implements HitListener {

    private final Game game;
    private final Counter score;
    private final int pointsPerBlock;

    /**
     * Create a score tracking listener.
     *
     * @param game           game to spawn floating text in
     * @param score          score counter
     * @param pointsPerBlock points to award per block
     */
    public ScoreTrackingListener(Game game, Counter score, int pointsPerBlock) {
        this.game = game;
        this.score = score;
        this.pointsPerBlock = pointsPerBlock;
    }

    @Override
    public void hitEvent(Block beingHit, Ball hitter) {
        // Only award points when block is destroyed (not indestructible and no hit
        // points left)
        if (!beingHit.isIndestructible() && beingHit.getHitPoints() <= 0) {
            int multiplier = game.getScoreMultiplier();
            score.increase(pointsPerBlock * multiplier);
            game.spawnFloatingText(beingHit.getCollisionRectangle().getUpperLeft(),
                    "+" + (pointsPerBlock * multiplier) + (multiplier > 1 ? " x" + multiplier : ""));
            game.increaseScoreMultiplier();
        }
    }
}
