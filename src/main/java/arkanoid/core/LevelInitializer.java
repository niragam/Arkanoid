package arkanoid.core;

import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import arkanoid.entity.Ball;
import arkanoid.entity.Block;
import arkanoid.entity.Paddle;
import arkanoid.event.BallRemover;
import arkanoid.event.BlockRemover;
import arkanoid.event.PowerUpSpawner;
import arkanoid.event.ScoreTrackingListener;
import arkanoid.geometry.Point;
import arkanoid.geometry.Rectangle;

import java.util.List;

/**
 * Handles the initialization of a game level.
 * Creates borders, blocks, paddle, and initial ball.
 */
public class LevelInitializer {

    /**
     * Initialize the game level.
     *
     * @param game the game instance to initialize
     */
    public static void initialize(Game game) {
        int width = game.getWidth();
        int height = game.getHeight();
        int borderThickness = GameConfig.BORDER_THICKNESS;

        // Borders (as indestructible blocks)
        // Top border matches HUD height to prevent ball from going behind HUD
        Color borderColor = GameConfig.BORDER_COLOR;
        int topBorderHeight = (int) GameConfig.HUD_HEIGHT;
        Block top = new Block(
                new Rectangle(new Point(0, 0), width, topBorderHeight),
                borderColor, 1, true);
        Block left = new Block(
                new Rectangle(new Point(0, topBorderHeight),
                        borderThickness, height - topBorderHeight),
                borderColor, 1, true);
        Block right = new Block(
                new Rectangle(new Point(width - borderThickness, topBorderHeight),
                        borderThickness, height - topBorderHeight),
                borderColor, 1, true);
        Block bottom = new Block(
                new Rectangle(new Point(0, height - borderThickness),
                        width, borderThickness),
                borderColor, 1, true);

        top.addToGame(game);
        left.addToGame(game);
        right.addToGame(game);
        bottom.addToGame(game);

        // Rows of blocks with varying hit points
        int levelBlocksPerRow = game.getLevelBlocksPerRow();
        int levelRows = game.getLevelRows();
        List<Color> levelColors = game.getLevelColors();
        List<Integer> levelHitPoints = game.getLevelHitPoints();

        double blockWidth = (width - 2 * borderThickness) / (double) levelBlocksPerRow;
        double blockHeight = GameConfig.BLOCK_HEIGHT;
        double startY = borderThickness + GameConfig.BLOCKS_START_Y_OFFSET;
        double startX = borderThickness;

        BlockRemover blockRemover = new BlockRemover(game, game.getRemainingBlocksCounter());
        ScoreTrackingListener scoreListener = new ScoreTrackingListener(game, game.getScoreCounter(),
                GameConfig.POINTS_PER_BLOCK);
        PowerUpSpawner powerUpSpawner = new PowerUpSpawner(game, GameConfig.POWERUP_SPAWN_CHANCE);

        for (int row = 0; row < levelRows; row++) {
            Color rowColor = levelColors.get(row % levelColors.size());
            int hitPoints = levelHitPoints.get(row % levelHitPoints.size());
            double y = startY + row * blockHeight;
            for (int col = 0; col < levelBlocksPerRow; col++) {
                double x = startX + col * blockWidth;
                Block block = new Block(
                        new Rectangle(new Point(x, y), blockWidth, blockHeight),
                        rowColor,
                        hitPoints);
                block.addHitListener(blockRemover);
                block.addHitListener(scoreListener);
                block.addHitListener(powerUpSpawner);
                block.addToGame(game);
                game.getRemainingBlocksCounter().increase(1);
            }
        }

        // Paddle
        double paddleWidth = GameConfig.PADDLE_WIDTH;
        double paddleHeight = GameConfig.PADDLE_HEIGHT;
        double paddleX = (width - paddleWidth) / 2.0;
        double paddleY = height - borderThickness - paddleHeight - GameConfig.PADDLE_Y_OFFSET;
        Rectangle paddleRect = new Rectangle(
                new Point(paddleX, paddleY), paddleWidth, paddleHeight);
        Paddle paddle = new Paddle(paddleRect, GameConfig.PADDLE_SPEED,
                borderThickness,
                width - borderThickness,
                KeyCode.LEFT, KeyCode.RIGHT,
                game.getInput());
        paddle.setOnHit(game::resetScoreMultiplier);
        paddle.addToGame(game);
        game.setPaddle(paddle);

        // Single ball attached to paddle - press SPACE to launch
        Ball ball = new Ball(
                new Point(width / 2.0, paddleY - GameConfig.BALL_RADIUS),
                GameConfig.BALL_RADIUS, GameConfig.BALL_COLOR);
        ball.setEnvironment(game.getEnvironment());
        ball.attachToPaddle(paddle);
        game.setAttachedBall(ball);
        game.addBall(ball);

        // Bottom "death" block to remove balls that fall below the paddle area.
        BallRemover ballRemover = new BallRemover(game, game.getRemainingBallsCounter());
        bottom.addHitListener(ballRemover);
    }
}
