package arkanoid.screen;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.geometry.VPos;

import arkanoid.animation.Animation;
import arkanoid.core.GameConfig;
import arkanoid.core.UIStrings;
import arkanoid.util.Counter;
import arkanoid.util.InputState;

/**
 * End screen showing final result and score, waits for SPACE to return to menu.
 */
public class EndScreen implements Animation {

    private final InputState input;
    private final Counter score;
    private final Counter lives;
    private boolean stop;
    private boolean spaceWasPressed;
    private boolean initialized = false;
    private boolean isWin;
    private String message;
    private Runnable onComplete;
    private boolean exitRequested;

    /**
     * Check if exit was requested.
     * 
     * @return true if exit requested
     */
    public boolean isExitRequested() {
        return exitRequested;
    }

    /**
     * Create an end screen.
     *
     * @param input shared input state
     * @param score final score
     * @param lives remaining lives (to determine win/loss)
     */
    public EndScreen(InputState input, Counter score, Counter lives) {
        this.input = input;
        this.score = score;
        this.lives = lives;
        this.spaceWasPressed = input.isPressed(KeyCode.SPACE);
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
        // Lazy initialization - determine win/lose when animation actually starts
        if (!initialized) {
            initialized = true;
            isWin = lives.getValue() > 0;
            message = UIStrings.END_FINAL_SCORE + score.getValue();
        }

        double width = gc.getCanvas().getWidth();
        double height = gc.getCanvas().getHeight();

        // Draw gradient background based on win/lose
        if (isWin) {
            gc.setFill(new LinearGradient(
                    0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                    new Stop(0, Color.rgb(20, 80, 20)),
                    new Stop(1, Color.rgb(10, 40, 10))));
        } else {
            gc.setFill(new LinearGradient(
                    0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                    new Stop(0, Color.rgb(80, 20, 20)),
                    new Stop(1, Color.rgb(40, 10, 10))));
        }
        gc.fillRect(0, 0, width, height);

        // Set alignment to center
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);

        // Draw title
        gc.setFont(Font.font(GameConfig.DEFAULT_FONT_FAMILY, FontWeight.BOLD, GameConfig.END_TITLE_FONT_SIZE));
        if (isWin) {
            gc.setFill(Color.GOLD);
            gc.fillText(UIStrings.END_WIN, width / 2, height / 3);
        } else {
            gc.setFill(Color.RED);
            gc.fillText(UIStrings.END_GAME_OVER, width / 2, height / 3);
        }

        // Draw score message
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(GameConfig.DEFAULT_FONT_FAMILY, FontWeight.NORMAL, GameConfig.END_MESSAGE_FONT_SIZE));
        gc.fillText(message, width / 2, height / 2);

        // Draw prompt (no longer blinking)
        gc.setFill(Color.LIGHTGRAY);
        gc.setFont(Font.font(GameConfig.DEFAULT_FONT_FAMILY, FontWeight.NORMAL, GameConfig.END_PROMPT_FONT_SIZE));
        gc.fillText(UIStrings.END_PROMPT_MENU, width / 2, height * 2 / 3);
        gc.fillText(UIStrings.END_PROMPT_QUIT, width / 2, height * 2 / 3 + 30);

        // Check for SPACE press (edge detection)
        boolean spacePressedNow = input.isPressed(KeyCode.SPACE);
        if (spacePressedNow && !spaceWasPressed) {
            stop = true;
        }
        spaceWasPressed = spacePressedNow;

        if (input.isPressed(KeyCode.Q)) {
            stop = true;
            exitRequested = true;
        }
    }

    @Override
    public boolean shouldStop() {
        return stop;
    }
}
