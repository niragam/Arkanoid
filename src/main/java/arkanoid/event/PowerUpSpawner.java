package arkanoid.event;

import arkanoid.core.Game;
import arkanoid.entity.Ball;
import arkanoid.entity.Block;
import arkanoid.entity.PowerUp;
import arkanoid.geometry.Point;

import java.util.Random;

/**
 * A HitListener that spawns power-ups when blocks are destroyed.
 */
public class PowerUpSpawner implements HitListener {

    private final Game game;
    private final double spawnChance;
    private final Random random = new Random();

    /**
     * Create a power-up spawner.
     *
     * @param game        the game to spawn power-ups in
     * @param spawnChance probability (0.0-1.0) of spawning a power-up
     */
    public PowerUpSpawner(Game game, double spawnChance) {
        this.game = game;
        this.spawnChance = spawnChance;
    }

    @Override
    public void hitEvent(Block beingHit, Ball hitter) {
        // Only spawn if block is destroyed (hitPoints <= 0) and not indestructible
        if (beingHit.isIndestructible() || beingHit.getHitPoints() > 0) {
            return;
        }

        // Random chance to spawn
        if (random.nextDouble() > spawnChance) {
            return;
        }

        // Random power-up type
        PowerUp.Type[] types = PowerUp.Type.values();
        PowerUp.Type type = types[random.nextInt(types.length)];

        // Spawn at block center
        Point blockCenter = beingHit.getCollisionRectangle().getCenter();

        game.spawnPowerUp(blockCenter, type);
    }
}
