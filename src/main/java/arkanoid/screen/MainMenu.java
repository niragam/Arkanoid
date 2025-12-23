package arkanoid.screen;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import arkanoid.animation.Animation;
import arkanoid.core.GameConfig;
import arkanoid.core.UIStrings;
import arkanoid.util.InputState;

/**
 * Main menu screen with "Neon Arcade" style visuals and interactive navigation.
 */
public class MainMenu implements Animation {

    private final InputState input;
    private boolean stop;
    private Runnable onComplete;

    // Navigation
    private int currentSelection = 0; // 0 = Start, 1 = Quit
    private boolean upWasPressed;
    private boolean downWasPressed;
    private boolean enterWasPressed;
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
     * Create a main menu screen.
     *
     * @param input shared input state
     */
    public MainMenu(InputState input) {
        this.input = input;
        // Prevent immediate input carry-over
        this.enterWasPressed = input.isPressed(KeyCode.ENTER);
        this.upWasPressed = input.isPressed(KeyCode.UP);
        this.downWasPressed = input.isPressed(KeyCode.DOWN);
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
        updateLogic(dt);
        draw(gc);
    }

    private void updateLogic(double dt) {
        // Input handling for navigation
        boolean upPressed = input.isPressed(KeyCode.UP);
        boolean downPressed = input.isPressed(KeyCode.DOWN);
        boolean enterPressed = input.isPressed(KeyCode.ENTER);

        if (upPressed && !upWasPressed) {
            currentSelection--;
            if (currentSelection < 0)
                currentSelection = 1;
        }
        if (downPressed && !downWasPressed) {
            currentSelection++;
            if (currentSelection > 1)
                currentSelection = 0;
        }

        if (enterPressed && !enterWasPressed) {
            if (currentSelection == 0) {
                stop = true; // Start Game
            } else {
                stop = true;
                exitRequested = true;
            }
        }

        upWasPressed = upPressed;
        downWasPressed = downPressed;
        enterWasPressed = enterPressed;
    }

    private void draw(GraphicsContext gc) {
        // Reset text baseline to default to avoid inheritance from previous screens
        gc.setTextBaseline(javafx.geometry.VPos.BASELINE);

        double width = gc.getCanvas().getWidth();
        double height = gc.getCanvas().getHeight();

        drawBackground(gc, width, height);
        drawTitle(gc, width, height);
        drawMenuOptions(gc, width, height);
    }

    private void drawBackground(GraphicsContext gc, double width, double height) {
        drawSky(gc, width, height);
        drawSun(gc, width, height);
        drawGrid(gc, width, height);
    }

    private void drawSky(GraphicsContext gc, double width, double height) {
        gc.setFill(new LinearGradient(
                0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(10, 0, 20)),
                new Stop(0.5, Color.rgb(40, 0, 60)),
                new Stop(1, Color.rgb(80, 0, 80))));
        gc.fillRect(0, 0, width, height);
    }

    private void drawSun(GraphicsContext gc, double width, double height) {
        double sunY = height * GameConfig.MENU_SUN_Y_PERCENT;
        double sunSize = GameConfig.MENU_SUN_SIZE;
        gc.save();
        gc.setFill(new LinearGradient(
                0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.YELLOW),
                new Stop(0.5, Color.MAGENTA),
                new Stop(1, Color.PURPLE)));
        gc.fillOval(width / 2 - sunSize / 2, sunY - sunSize, sunSize, sunSize);

        // Sun stripes (retro style)
        gc.setFill(Color.rgb(40, 0, 60)); // Match sky color near horizon
        for (int i = 0; i < 6; i++) {
            double stripeHeight = 4 + i * 2;
            double stripeY = sunY - 10 - (i * 15);
            gc.fillRect(width / 2 - sunSize / 2, stripeY, sunSize, stripeHeight);
        }
        gc.restore();
    }

    private void drawGrid(GraphicsContext gc, double width, double height) {
        double horizonY = height * GameConfig.MENU_SUN_Y_PERCENT;
        gc.save();
        // Clip to bottom half
        gc.beginPath();
        gc.rect(0, horizonY, width, height - horizonY);
        gc.clip();

        // Floor background
        gc.setFill(Color.rgb(20, 0, 40));
        gc.fillRect(0, horizonY, width, height - horizonY);

        // Vertical perspective lines (static)
        gc.setStroke(Color.rgb(255, 0, 255, 0.5)); // Neon Pink
        gc.setLineWidth(2);
        double centerX = width / 2;
        for (int i = -10; i <= 10; i++) {
            gc.strokeLine(centerX + i * 20, horizonY, centerX + i * 300, height);
        }

        // Static horizontal lines with perspective spacing
        double groundHeight = height - horizonY;
        for (int i = 1; i <= 12; i++) {
            double t = i / 13.0;
            double lineY = horizonY + Math.pow(t, 2.2) * groundHeight;
            double alpha = 0.2 + 0.5 * t;
            gc.setStroke(Color.rgb(0, 255, 255, alpha));
            gc.setLineWidth(0.5 + t * 1.5);
            gc.strokeLine(0, lineY, width, lineY);
        }
        gc.restore();
    }

    private void drawTitle(GraphicsContext gc, double width, double height) {
        String title = UIStrings.GAME_TITLE;
        Font font = Font.font(GameConfig.DEFAULT_FONT_FAMILY, FontWeight.BLACK, GameConfig.MENU_TITLE_FONT_SIZE);
        double x = width / 2;
        double y = height * GameConfig.MENU_TITLE_Y_PERCENT;

        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(font);

        // Glow effect
        gc.save();
        DropShadow glow = new DropShadow();
        glow.setColor(Color.CYAN);
        glow.setRadius(10); // Reduced from 20
        glow.setSpread(0.2); // Reduced from 0.5
        gc.setEffect(glow);

        gc.setFill(Color.WHITE);
        gc.fillText(title, x, y);
        gc.restore();
    }

    private void drawMenuOptions(GraphicsContext gc, double width, double height) {
        double startY = height * GameConfig.MENU_BUTTON_START_Y_PERCENT;
        double gap = GameConfig.MENU_BUTTON_GAP;

        drawButton(gc, width / 2, startY, UIStrings.MENU_START_GAME, currentSelection == 0);
        drawButton(gc, width / 2, startY + gap, UIStrings.MENU_QUIT, currentSelection == 1);

        // Instructions
        double instructionsY = height * GameConfig.MENU_INSTRUCTIONS_Y_PERCENT;
        double gapY = GameConfig.MENU_INSTRUCTIONS_GAP_Y;

        drawInstructionRow(gc, width / GameConfig.MENU_INSTRUCTIONS_X_DIVISOR, instructionsY, UIStrings.MENU_ACTION_MOVE, "←", "→");
        drawInstructionRow(gc, width / GameConfig.MENU_INSTRUCTIONS_X_DIVISOR, instructionsY + gapY, UIStrings.MENU_ACTION_PAUSE, "P");
        drawInstructionRow(gc, width / GameConfig.MENU_INSTRUCTIONS_X_DIVISOR, instructionsY + gapY * 2, UIStrings.MENU_ACTION_SKIP_LEVEL,
                "K");
    }

    private void drawButton(GraphicsContext gc, double x, double y, String text, boolean selected) {
        double w = GameConfig.MENU_BUTTON_WIDTH;
        double h = GameConfig.MENU_BUTTON_HEIGHT;
        double corner = GameConfig.MENU_BUTTON_CORNER;

        if (selected) {
            // Selected style: Filled, glowing magenta
            gc.save();
            DropShadow glow = new DropShadow();
            glow.setColor(Color.MAGENTA);
            glow.setRadius(20);
            glow.setSpread(0.3);
            gc.setEffect(glow);

            gc.setFill(Color.rgb(255, 0, 255, 0.9));
            gc.fillRoundRect(x - w / 2, y - h / 2, w, h, corner, corner);
            gc.restore();

            gc.setFill(Color.WHITE);
        } else {
            // Unselected style: Semi-transparent fill with bright border
            // Dark fill for contrast
            gc.setFill(Color.rgb(20, 10, 40, 0.8));
            gc.fillRoundRect(x - w / 2, y - h / 2, w, h, corner, corner);

            // Bright cyan border
            gc.setStroke(Color.rgb(0, 220, 255));
            gc.setLineWidth(2.5);
            gc.strokeRoundRect(x - w / 2, y - h / 2, w, h, corner, corner);

            // Inner highlight line at top
            gc.setStroke(Color.rgb(100, 255, 255, 0.4));
            gc.setLineWidth(1);
            gc.strokeLine(x - w / 2 + corner, y - h / 2 + 2, x + w / 2 - corner, y - h / 2 + 2);

            gc.setFill(Color.rgb(150, 255, 255));
        }

        gc.setFont(Font.font(GameConfig.DEFAULT_FONT_FAMILY, FontWeight.BOLD, GameConfig.MENU_BUTTON_FONT_SIZE));
        // Center text vertically in button
        gc.fillText(text, x, y + 7);
    }

    private void drawInstructionRow(GraphicsContext gc, double centerX, double y, String action, String... keys) {
        // Draw Action Text (Left aligned relative to center-ish)
        gc.setTextAlign(TextAlignment.RIGHT);
        gc.setTextBaseline(javafx.geometry.VPos.CENTER);
        gc.setFont(Font.font(GameConfig.DEFAULT_FONT_FAMILY, FontWeight.NORMAL, GameConfig.MENU_INSTRUCTION_ACTION_FONT_SIZE));
        gc.setFill(Color.LIGHTGRAY);
        gc.fillText(action, centerX - 20, y);

        // Draw Keys (Right aligned relative to center-ish)
        double keyX = centerX + 20;
        for (String key : keys) {
            drawKey(gc, keyX, y, key);
            keyX += 60; // Spacing between multiple keys
        }
    }

    private void drawKey(GraphicsContext gc, double x, double y, String text) {
        double size = 40;
        if (text.length() > 1)
            size = 80; // Wider for SPACE

        // Key Background
        gc.setFill(Color.DARKGRAY);
        gc.fillRoundRect(x, y - 20, size, 40, 10, 10);

        // Key Border (3D effect)
        gc.setStroke(Color.GRAY);
        gc.setLineWidth(2);
        gc.strokeRoundRect(x, y - 20, size, 40, 10, 10);

        // Key Text
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(javafx.geometry.VPos.CENTER);
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(GameConfig.DEFAULT_FONT_FAMILY, FontWeight.BOLD, GameConfig.MENU_INSTRUCTION_KEY_FONT_SIZE));
        gc.fillText(text, x + size / 2, y);
    }

    @Override
    public boolean shouldStop() {
        return stop;
    }
}
