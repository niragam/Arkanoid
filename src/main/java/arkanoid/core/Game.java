package arkanoid.core;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import arkanoid.entity.FloatingText;
import arkanoid.level.Background;

import java.util.ArrayList;
import java.util.List;

import arkanoid.entity.Ball;
import arkanoid.entity.Collidable;
import arkanoid.entity.GameEnvironment;
import arkanoid.entity.Paddle;
import arkanoid.entity.PowerUp;
import arkanoid.entity.Sprite;
import arkanoid.entity.SpriteCollection;
import arkanoid.geometry.Point;
import arkanoid.util.Counter;
import arkanoid.util.InputState;
import arkanoid.util.Velocity;

import arkanoid.graphics.GameRenderer;
import arkanoid.graphics.HUD;

/**
 * Main game logic: holds sprites and environment for a single level.
 */
public class Game {

    private final int width;
    private final int height;
    private final GraphicsContext gc;
    private final InputState input;
    private final SpriteCollection sprites;
    private final GameEnvironment environment;
    private final GameRenderer gameRenderer;
    private final HUD hud;

    private Paddle paddle;
    private Ball attachedBall; // Ball waiting to be launched
    private final List<Ball> activeBalls = new ArrayList<>();
    private final PowerUpManager powerUpManager;
    private final List<FloatingText> activeFloatingTexts = new ArrayList<>();
    private final Counter remainingBlocks = new Counter();
    private final Counter remainingBalls = new Counter();
    private final Counter score;
    private final Counter lives;

    // Level configuration
    private final int levelRows;
    private final int levelBlocksPerRow;
    private final List<Integer> levelHitPoints;
    private final List<Color> levelColors;
    private final Background background;

    private boolean levelCleared;
    private boolean outOfBalls;
    private int scoreMultiplier = 1;

    /**
     * Get the current score multiplier.
     * 
     * @return multiplier
     */
    public int getScoreMultiplier() {
        return scoreMultiplier;
    }

    /**
     * Increase the score multiplier by 1.
     */
    public void increaseScoreMultiplier() {
        scoreMultiplier++;
    }

    /**
     * Reset the score multiplier to 1.
     */
    public void resetScoreMultiplier() {
        scoreMultiplier = 1;
    }

    /**
     * Create a game with default level configuration.
     */
    public Game(int width, int height, GraphicsContext gc, InputState input, Counter score, Counter lives,
            Background background, String levelName) {
        this(width, height, gc, input, score, lives,
                GameConfig.DEFAULT_ROWS, GameConfig.BLOCKS_PER_ROW,
                GameConfig.ROW_HIT_POINTS, GameConfig.ROW_COLORS, background, levelName);
    }

    /**
     * Create a game with custom level configuration.
     */
    public Game(int width, int height, GraphicsContext gc, InputState input, Counter score, Counter lives,
            int rows, int blocksPerRow, List<Integer> hitPointsPerRow, List<Color> rowColors, Background background,
            String levelName) {
        if (hitPointsPerRow == null || rowColors == null) {
            throw new IllegalArgumentException("Level configuration lists cannot be null");
        }
        if (hitPointsPerRow.isEmpty() || rowColors.isEmpty()) {
            throw new IllegalArgumentException("Level configuration lists cannot be empty");
        }

        this.width = width;
        this.height = height;
        this.gc = gc;
        this.input = input;
        this.sprites = new SpriteCollection();
        this.environment = new GameEnvironment();
        this.gameRenderer = new GameRenderer(gc);
        this.powerUpManager = new PowerUpManager(this);
        this.hud = new HUD(gc, width, score, lives, levelName);
        this.score = score;
        this.lives = lives;
        this.levelRows = rows;
        this.levelBlocksPerRow = blocksPerRow;
        this.levelHitPoints = hitPointsPerRow;
        this.levelColors = rowColors;

        this.background = background;
    }

    // ... (skipped methods)

    /**
     * Perform one frame: clear, update, and draw all sprites.
     *
     * @param dt time passed in seconds since last frame
     */
    public void doOneFrame(double dt) {
        // Draw gradient background
        drawBackground(dt);

        sprites.updateAll(dt);
        gameRenderer.renderAll(sprites.getSprites());

        // Delegate power-up management
        powerUpManager.update(paddle);

        // Remove expired floating texts
        activeFloatingTexts.removeIf(ft -> {
            if (ft.shouldRemove()) {
                removeSprite(ft);
                return true;
            }
            return false;
        });

        // Draw HUD
        hud.draw(scoreMultiplier);

        if (remainingBlocks.getValue() == 0) {
            levelCleared = true;
        }
        if (remainingBalls.getValue() == 0) {
            outOfBalls = true;
        }
    }

    /**
     * Draw gradient background.
     *
     * @param dt delta time in seconds
     */
    private void drawBackground(double dt) {
        if (background != null) {
            background.draw(gc, width, height, dt);
        } else {
            // Fallback default
            gc.setFill(Color.BLACK);
            gc.fillRect(0, 0, width, height);
        }
    }

    // ... (skipped methods)

    /**
     * Add a collidable object to the game environment.
     *
     * @param c the collidable to add
     */
    public void addCollidable(Collidable c) {
        environment.addCollidable(c);
    }

    /**
     * Add a sprite to the game.
     *
     * @param s the sprite to add
     */
    public void addSprite(Sprite s) {
        sprites.addSprite(s);
    }

    /**
     * Get the paddle.
     *
     * @return the paddle
     */
    public Paddle getPaddle() {
        return paddle;
    }

    /**
     * Initialize game objects: borders, blocks, paddle, balls.
     * Delegates to LevelInitializer for level setup.
     */
    public void initialize() {
        LevelInitializer.initialize(this);
    }

    /**
     * Remove a sprite from the game.
     *
     * @param s the sprite to remove
     */
    public void removeSprite(Sprite s) {
        sprites.removeSprite(s);
    }

    /**
     * Remove a collidable from the game environment.
     *
     * @param c the collidable to remove
     */
    public void removeCollidable(Collidable c) {
        environment.removeCollidable(c);
    }

    /**
     * Check if the level is cleared (no blocks remaining).
     *
     * @return true if level is cleared
     */
    public boolean isLevelCleared() {
        return levelCleared;
    }

    /**
     * Check if the player is out of balls.
     *
     * @return true if out of balls
     */
    public boolean isOutOfBalls() {
        return outOfBalls;
    }

    /**
     * Spawn a new ball attached to the paddle (used when player loses a ball but
     * has lives left).
     */
    public void spawnNewBallOnPaddle() {
        double paddleY = paddle.getCollisionRectangle().getUpperLeft().getY();
        double paddleX = paddle.getCollisionRectangle().getUpperLeft().getX();
        double paddleWidth = paddle.getCollisionRectangle().getWidth();

        Ball newBall = new Ball(
                new Point(paddleX + paddleWidth / 2, paddleY - GameConfig.BALL_RADIUS),
                GameConfig.BALL_RADIUS, GameConfig.BALL_COLOR);
        newBall.setEnvironment(environment);
        newBall.attachToPaddle(paddle);
        attachedBall = newBall;
        activeBalls.add(newBall);
        addSprite(newBall);
        remainingBalls.increase(1);
        outOfBalls = false;
    }

    /**
     * Get the score counter.
     *
     * @return score counter
     */
    public Counter getScoreCounter() {
        return score;
    }

    /**
     * Get the lives counter.
     *
     * @return lives counter
     */
    public Counter getLivesCounter() {
        return lives;
    }

    /**
     * Get the remaining blocks counter.
     *
     * @return remaining blocks counter
     */
    public Counter getRemainingBlocksCounter() {
        return remainingBlocks;
    }

    /**
     * Get the remaining balls counter.
     *
     * @return remaining balls counter
     */
    public Counter getRemainingBallsCounter() {
        return remainingBalls;
    }

    /**
     * Check if there's a ball waiting to be launched.
     *
     * @return true if a ball is attached to paddle
     */
    public boolean hasBallToLaunch() {
        return attachedBall != null && attachedBall.isAttached();
    }

    /**
     * Launch the attached ball.
     */
    public void launchBall() {
        if (attachedBall != null && attachedBall.isAttached()) {
            attachedBall.launch(Velocity.fromAngleAndSpeed(
                    GameConfig.BALL_LAUNCH_ANGLE, GameConfig.BALL_SPEED));
            attachedBall = null;
        }
    }

    /**
     * Get the game environment.
     *
     * @return game environment
     */
    public GameEnvironment getEnvironment() {
        return environment;
    }

    /**
     * Spawn extra balls (for multi-ball power-up).
     *
     * @param count number of balls to spawn
     */
    public void spawnExtraBalls(int count) {
        for (int i = 0; i < count; i++) {
            // Spawn from center of screen
            Ball newBall = new Ball(
                    new Point(width / 2.0, height / 2.0),
                    GameConfig.BALL_RADIUS, GameConfig.BALL_COLOR);
            // Random angle between [0, 60] and [300, 360]
            double angle;
            if (Math.random() < 0.5) {
                angle = Math.random() * 60; // 0 to 60
            } else {
                angle = 300 + Math.random() * 60; // 300 to 360
            }
            newBall.setVelocity(Velocity.fromAngleAndSpeed(angle, GameConfig.BALL_SPEED));
            newBall.setEnvironment(environment);
            activeBalls.add(newBall);
            addSprite(newBall);
            remainingBalls.increase(1);
        }
    }

    /**
     * Spawn a power-up at the given position.
     *
     * @param center position to spawn at
     * @param type   type of power-up
     */
    public void spawnPowerUp(Point center, PowerUp.Type type) {
        powerUpManager.spawnPowerUp(center, type);
    }

    /**
     * Remove a ball from tracking (called when ball is removed from game).
     *
     * @param ball ball to remove
     */
    public void removeBall(Ball ball) {
        activeBalls.remove(ball);
    }

    /**
     * Spawn floating text at a specific location.
     *
     * @param center center point for the text
     * @param text   the text to display
     */
    public void spawnFloatingText(Point center, String text) {
        FloatingText ft = new FloatingText(center, text, Color.WHITE);
        activeFloatingTexts.add(ft);
        addSprite(ft);
    }

    /**
     * Clear all active power-ups from the game.
     */
    public void clearPowerUps() {
        powerUpManager.clear();
    }

    /**
     * Set the paddle for this game.
     *
     * @param paddle the paddle to set
     */
    public void setPaddle(Paddle paddle) {
        this.paddle = paddle;
    }

    /**
     * Set the ball that is attached to the paddle.
     *
     * @param ball the ball to attach
     */
    public void setAttachedBall(Ball ball) {
        this.attachedBall = ball;
    }

    /**
     * Add a ball to the game.
     *
     * @param ball the ball to add
     */
    public void addBall(Ball ball) {
        activeBalls.add(ball);
        addSprite(ball);
        remainingBalls.increase(1);
    }

    /**
     * Get the width of the game area.
     *
     * @return width in pixels
     */
    public int getWidth() {
        return width;
    }

    /**
     * Get the height of the game area.
     *
     * @return height in pixels
     */
    public int getHeight() {
        return height;
    }

    /**
     * Get the input state handler.
     *
     * @return input state
     */
    public InputState getInput() {
        return input;
    }

    /**
     * Get the number of rows in this level.
     *
     * @return number of rows
     */
    public int getLevelRows() {
        return levelRows;
    }

    /**
     * Get the number of blocks per row in this level.
     *
     * @return blocks per row
     */
    public int getLevelBlocksPerRow() {
        return levelBlocksPerRow;
    }

    /**
     * Get the colors for each row.
     *
     * @return list of colors
     */
    public List<Color> getLevelColors() {
        return levelColors;
    }

    /**
     * Get the hit points for each row.
     *
     * @return list of hit points
     */
    public List<Integer> getLevelHitPoints() {
        return levelHitPoints;
    }
}
