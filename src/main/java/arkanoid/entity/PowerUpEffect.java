package arkanoid.entity;

import arkanoid.core.Game;

/**
 * Functional interface for power-up effects.
 * Allows adding new power-up types without modifying existing code (Open/Closed Principle).
 */
@FunctionalInterface
public interface PowerUpEffect {
    /**
     * Apply the power-up effect to the game.
     *
     * @param game the game instance
     */
    void apply(Game game);
}

