package arkanoid.animation;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.geometry.VPos;

import arkanoid.core.Game;
import arkanoid.core.UIStrings;
import arkanoid.util.Counter;
import arkanoid.util.InputState;
import arkanoid.screen.PauseScreen;

/**
 * Per-level animation wrapper: runs a Game and handles pause and life loss.
 * Extracted from GameFlow for better separation of concerns.
 */
public class LevelAnimation implements Animation {

    private final Game game;
    private final InputState input;
    private final Counter lives;
    private boolean stop;
    private boolean pauseWasPressed;
    private boolean paused;
    private boolean spaceWasPressed;
    private boolean kWasPressed;

    private final PauseScreen pauseScreen;
    private boolean quitToMenu;
    private Runnable onComplete;

    /**
     * Create a level animation wrapper.
     *
     * @param game  the game to run
     * @param input shared input state
     * @param lives lives counter
     */
    public LevelAnimation(Game game, InputState input, Counter lives) {
        this.game = game;
        this.input = input;
        this.lives = lives;
        this.pauseScreen = new PauseScreen(input);
        // Prevent immediate skip if key is held from previous level
        this.kWasPressed = input.isPressed(KeyCode.K);
    }

    @Override
    public void setOnComplete(Runnable callback) {
        this.onComplete = callback;
    }

    @Override
    public Runnable getOnComplete() {
        return onComplete;
    }

    @Override
    public void doOneFrame(GraphicsContext gc, double dt) {
        boolean pausePressedNow = input.isPressed(KeyCode.P);
        boolean spacePressedNow = input.isPressed(KeyCode.SPACE);
        boolean kPressedNow = input.isPressed(KeyCode.K);

        // Toggle pause on P key press (edge detection)
        if (pausePressedNow && !pauseWasPressed) {
            paused = !paused;
            if (paused) {
                pauseScreen.reset();
            }
        }
        pauseWasPressed = pausePressedNow;

        // Skip level on K key press (edge detection)
        if (kPressedNow && !kWasPressed) {
            stop = true;
        }
        kWasPressed = kPressedNow;

        if (paused) {
            pauseScreen.doOneFrame(gc, dt);
            if (pauseScreen.shouldStop()) {
                paused = false;
                if (pauseScreen.isQuitRequested()) {
                    stop = true;
                    quitToMenu = true;
                }
            }
            return;
        }

        // Handle SPACE key
        if (spacePressedNow && !spaceWasPressed) {
            if (game.hasBallToLaunch()) {
                // Launch ball if one is waiting on paddle
                game.launchBall();
            }
        }
        spaceWasPressed = spacePressedNow;

        game.doOneFrame(dt);

        // Show launch hint if ball is waiting
        if (game.hasBallToLaunch()) {
            drawLaunchHint(gc);
        }

        checkLevelEnd();
    }

    /**
     * Check if the user requested to quit to the main menu.
     *
     * @return true if quit requested
     */
    public boolean isQuitRequested() {
        return quitToMenu;
    }

    /**
     * Draw the launch hint.
     */
    private void drawLaunchHint(GraphicsContext gc) {
        gc.save();
        gc.setFill(Color.WHITE);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.fillText(UIStrings.LAUNCH_HINT,
                gc.getCanvas().getWidth() / 2,
                gc.getCanvas().getHeight() / 2);
        gc.restore();
    }

    /**
     * Check if level should end (cleared or out of lives).
     */
    private void checkLevelEnd() {
        if (game.isLevelCleared()) {
            stop = true;
        } else if (game.isOutOfBalls()) {
            lives.decrease(1);
            game.clearPowerUps();
            if (lives.getValue() > 0) {
                // Spawn a new ball attached to paddle
                game.spawnNewBallOnPaddle();
            } else {
                stop = true;
            }
        }
    }

    @Override
    public boolean shouldStop() {
        return stop;
    }
}
