package arkanoid.core;

import java.util.ArrayList;
import java.util.List;

import arkanoid.entity.Paddle;
import arkanoid.entity.PowerUp;
import arkanoid.geometry.Point;

/**
 * Manages the lifecycle of power-ups: spawning, updating, collision detection,
 * and removal.
 */
public class PowerUpManager {

    private final Game game;
    private final List<PowerUp> activePowerUps;

    /**
     * Create a new PowerUpManager.
     *
     * @param game the game instance (needed to apply effects and add/remove
     *             sprites)
     */
    public PowerUpManager(Game game) {
        this.game = game;
        this.activePowerUps = new ArrayList<>();
    }

    /**
     * Spawn a power-up at the given position.
     *
     * @param center position to spawn at
     * @param type   type of power-up
     */
    public void spawnPowerUp(Point center, PowerUp.Type type) {
        PowerUp powerUp = new PowerUp(center, type);
        activePowerUps.add(powerUp);
        game.addSprite(powerUp);
    }

    /**
     * Update all active power-ups and check for collisions.
     *
     * @param paddle the player's paddle
     */
    public void update(Paddle paddle) {
        activePowerUps.removeIf(p -> {
            if (p.isCollected()) {
                game.removeSprite(p);
                return true;
            }

            // Remove power-ups that fell off the bottom of the screen
            if (p.getCenter().getY() > game.getHeight()) {
                game.removeSprite(p);
                return true;
            }

            // Check collision with paddle
            if (paddle != null && p.getBounds().intersects(paddle.getCollisionRectangle())) {
                p.collect();
                p.applyEffect(game);
                game.removeSprite(p);
                return true;
            }

            return false;
        });
    }

    /**
     * Clear all active power-ups.
     */
    public void clear() {
        for (PowerUp p : activePowerUps) {
            game.removeSprite(p);
        }
        activePowerUps.clear();
    }
}
