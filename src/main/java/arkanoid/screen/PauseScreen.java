package arkanoid.screen;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import arkanoid.animation.Animation;
import arkanoid.core.GameConfig;
import arkanoid.core.UIStrings;
import arkanoid.util.InputState;

/**
 * Pause screen animation: waits for the SPACE key to resume.
 */
public class PauseScreen implements Animation {

    private final InputState input;

    private enum State {
        RUNNING,
        RESUME,
        QUIT_TO_MENU
    }

    private State state = State.RUNNING;

    /**
     * Create a pause screen.
     *
     * @param input shared input state
     */
    public PauseScreen(InputState input) {
        this.input = input;
    }

    @Override
    public void doOneFrame(GraphicsContext gc, double dt) {
        double width = gc.getCanvas().getWidth();
        double height = gc.getCanvas().getHeight();

        // 1. Semi-transparent dark background
        gc.setFill(Color.rgb(10, 10, 30, 0.85));
        gc.fillRect(0, 0, width, height);

        // 2. Container Box
        double boxWidth = GameConfig.PAUSE_BOX_WIDTH;
        double boxHeight = GameConfig.PAUSE_BOX_HEIGHT;
        double boxX = (width - boxWidth) / 2;
        double boxY = (height - boxHeight) / 2;

        gc.save();
        // Glow effect for the box
        DropShadow borderGlow = new DropShadow();
        borderGlow.setColor(Color.MAGENTA);
        borderGlow.setRadius(20);
        gc.setEffect(borderGlow);

        gc.setStroke(Color.MAGENTA);
        gc.setLineWidth(4);
        gc.strokeRoundRect(boxX, boxY, boxWidth, boxHeight, GameConfig.PAUSE_BOX_ARC, GameConfig.PAUSE_BOX_ARC);
        gc.restore();

        // 3. Title "PAUSED"
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);

        gc.save();
        DropShadow textGlow = new DropShadow();
        textGlow.setColor(Color.CYAN);
        textGlow.setRadius(15);
        gc.setEffect(textGlow);

        gc.setFont(Font.font(GameConfig.DEFAULT_FONT_FAMILY, FontWeight.BLACK, GameConfig.PAUSE_TITLE_FONT_SIZE));
        gc.setFill(Color.WHITE);
        gc.fillText(UIStrings.PAUSE_TITLE, width / 2, boxY + GameConfig.PAUSE_TITLE_Y_OFFSET);
        gc.restore();

        // 4. Options
        double startY = boxY + GameConfig.PAUSE_OPTIONS_START_Y_OFFSET;
        double gapY = GameConfig.PAUSE_OPTIONS_GAP_Y;

        drawOption(gc, width / 2, startY, UIStrings.PAUSE_RESUME, UIStrings.KEY_SPACE);
        drawOption(gc, width / 2, startY + gapY, UIStrings.PAUSE_QUIT_TO_MENU, UIStrings.KEY_Q);

        // Logic
        if (input.isPressed(KeyCode.SPACE)) {
            state = State.RESUME;
        }
        if (input.isPressed(KeyCode.Q)) {
            state = State.QUIT_TO_MENU;
        }
    }

    private void drawOption(GraphicsContext gc, double x, double y, String action, String key) {
        gc.setTextAlign(TextAlignment.RIGHT);
        gc.setFont(Font.font(GameConfig.DEFAULT_FONT_FAMILY, FontWeight.NORMAL, GameConfig.PAUSE_OPTION_ACTION_FONT_SIZE));
        gc.setFill(Color.LIGHTGRAY);
        gc.fillText(action, x - 20, y);

        gc.setTextAlign(TextAlignment.LEFT);
        gc.setFont(Font.font(GameConfig.DEFAULT_FONT_FAMILY, FontWeight.BOLD, GameConfig.PAUSE_OPTION_KEY_FONT_SIZE));
        gc.setFill(Color.CYAN);
        gc.fillText("[" + key + "]", x + 20, y);
    }

    /**
     * Check if the user requested to quit to the main menu.
     *
     * @return true if quit requested
     */
    public boolean isQuitRequested() {
        return state == State.QUIT_TO_MENU;
    }

    /**
     * Reset the pause screen state so it can be reused.
     */
    public void reset() {
        this.state = State.RUNNING;
    }

    @Override
    public boolean shouldStop() {
        return state != State.RUNNING;
    }
}
