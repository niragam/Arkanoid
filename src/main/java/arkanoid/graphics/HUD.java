package arkanoid.graphics;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.geometry.VPos;

import arkanoid.core.GameConfig;
import arkanoid.util.Counter;

/**
 * Handles drawing the Heads-Up Display (HUD) with enhanced visuals.
 */
public class HUD {

    private final Counter score;
    private final Counter lives;
    private final String levelName;
    private final GraphicsContext gc;
    private final int width;
    // Animation phase for multiplier badge pulse (wraps to prevent overflow)
    private double animPhase = 0;
    private static final double TWO_PI = Math.PI * 2;

    /**
     * Create a new HUD.
     *
     * @param gc        graphics context
     * @param width     screen width
     * @param score     score counter
     * @param lives     lives counter
     * @param levelName name of the level
     */
    public HUD(GraphicsContext gc, int width, Counter score, Counter lives, String levelName) {
        this.gc = gc;
        this.width = width;
        this.score = score;
        this.lives = lives;
        this.levelName = levelName;
    }

    /**
     * Draw the HUD.
     * 
     * @param scoreMultiplier current score multiplier
     */
    public void draw(int scoreMultiplier) {
        double hudHeight = GameConfig.HUD_HEIGHT;
        // Wrap to prevent overflow
        animPhase += 0.05;
        if (animPhase > TWO_PI) {
            animPhase -= TWO_PI;
        }

        gc.save();

        // 1. HUD background with solid gradient (no transparency)
        gc.setFill(new LinearGradient(
                0, 0, 0, hudHeight, false, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(15, 15, 35)),
                new Stop(0.5, Color.rgb(25, 25, 50)),
                new Stop(1, Color.rgb(20, 20, 40))));
        gc.fillRect(0, 0, width, hudHeight);

        // 2. Bottom accent line with glow
        gc.setEffect(new GaussianBlur(4));
        gc.setStroke(Color.rgb(0, 200, 255, 0.6));
        gc.setLineWidth(3);
        gc.strokeLine(0, hudHeight, width, hudHeight);
        gc.setEffect(null);

        // Crisp accent line
        gc.setStroke(new LinearGradient(
                0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(255, 0, 150)),
                new Stop(0.5, Color.rgb(0, 200, 255)),
                new Stop(1, Color.rgb(255, 0, 150))));
        gc.setLineWidth(2);
        gc.strokeLine(0, hudHeight, width, hudHeight);

        // Setup text rendering
        gc.setTextBaseline(VPos.CENTER);

        // 3. Score (Left) with glow effect
        drawScore(hudHeight, scoreMultiplier);

        // 4. Level Name (Center) with neon effect
        drawLevelName(hudHeight);

        // 5. Lives (Right) with animated hearts
        drawLives(hudHeight);

        gc.restore();
    }

    private void drawScore(double hudHeight, int scoreMultiplier) {
        gc.save();

        String scoreText = "SCORE";
        String scoreValue = String.format("%,d", score.getValue());
        double x = GameConfig.HUD_PADDING_X;
        double y = hudHeight / 2;

        // Score label
        gc.setFill(Color.rgb(150, 150, 180));
        gc.setFont(Font.font(GameConfig.HUD_SCORE_FONT_NAME, FontWeight.NORMAL, 12));
        gc.setTextAlign(TextAlignment.LEFT);
        gc.fillText(scoreText, x, y - 8);

        // Score value with glow
        DropShadow glow = new DropShadow();
        glow.setColor(Color.rgb(0, 200, 255, 0.7));
        glow.setRadius(6);
        glow.setSpread(0.3);
        gc.setEffect(glow);

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(GameConfig.HUD_SCORE_FONT_NAME, FontWeight.BOLD, GameConfig.HUD_SCORE_FONT_SIZE));
        gc.fillText(scoreValue, x, y + 8);

        gc.setEffect(null);

        // Multiplier badge
        if (scoreMultiplier > 1) {
            double badgeX = x + 120;
            double pulse = 1 + 0.1 * Math.sin(animPhase * 3);

            // Badge glow
            gc.setEffect(new GaussianBlur(5));
            gc.setFill(Color.rgb(255, 200, 50, 0.5));
            gc.fillRoundRect(badgeX - 5, y - 12, 50 * pulse, 24, 12, 12);
            gc.setEffect(null);

            // Badge background
            gc.setFill(new LinearGradient(
                    0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                    new Stop(0, Color.rgb(255, 200, 50)),
                    new Stop(1, Color.rgb(255, 150, 0))));
            gc.fillRoundRect(badgeX, y - 10, 45, 20, 10, 10);

            // Badge text
            gc.setFill(Color.rgb(80, 40, 0));
            gc.setFont(Font.font(GameConfig.HUD_SCORE_FONT_NAME, FontWeight.BLACK, 14));
            gc.setTextAlign(TextAlignment.CENTER);
            gc.fillText("x" + scoreMultiplier, badgeX + 22, y);
        }

        gc.restore();
    }

    private void drawLevelName(double hudHeight) {
        gc.save();

        double x = width / 2.0;
        double y = hudHeight / 2;

        // Neon glow effect
        DropShadow neonGlow = new DropShadow();
        neonGlow.setColor(Color.rgb(255, 0, 200, 0.6));
        neonGlow.setRadius(10);
        neonGlow.setSpread(0.2);
        gc.setEffect(neonGlow);

        gc.setFill(Color.rgb(255, 150, 220));
        gc.setFont(Font.font(GameConfig.HUD_FONT_NAME, FontWeight.BOLD, GameConfig.HUD_LEVEL_NAME_FONT_SIZE));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText(levelName.toUpperCase(), x, y);

        gc.setEffect(null);

        // Crisp text overlay
        gc.setFill(Color.WHITE);
        gc.fillText(levelName.toUpperCase(), x, y);

        gc.restore();
    }

    private void drawLives(double hudHeight) {
        gc.save();

        double startX = width - GameConfig.HUD_PADDING_X;
        double centerY = hudHeight / 2;
        int currentLives = lives.getValue();

        double heartSize = GameConfig.HUD_HEART_SIZE;
        double spacing = GameConfig.HUD_HEART_SPACING;

        if (currentLives <= GameConfig.HUD_MAX_INDIVIDUAL_HEARTS) {
            // Calculate total width of hearts section
            double totalHeartsWidth = currentLives * spacing;
            
            // Draw "LIVES" label aligned with hearts
            gc.setTextAlign(TextAlignment.RIGHT);
            gc.setTextBaseline(VPos.CENTER);
            gc.setFill(Color.rgb(150, 150, 180));
            gc.setFont(Font.font(GameConfig.HUD_FONT_NAME, FontWeight.NORMAL, 14));
            gc.fillText("LIVES", startX - totalHeartsWidth - 10, centerY);

            // Draw individual hearts - vertically centered (static, no animation)
            for (int i = 0; i < currentLives; i++) {
                double heartX = startX - (i * spacing) - heartSize;
                double heartY = centerY - heartSize / 2;
                drawHeart(heartX, heartY, heartSize, i == 0);
            }
        } else {
            // Draw "x N" and one heart for many lives
            gc.setTextAlign(TextAlignment.RIGHT);
            gc.setTextBaseline(VPos.CENTER);
            
            // Draw count
            String text = "x" + currentLives;
            gc.setFill(Color.WHITE);
            gc.setFont(Font.font(GameConfig.HUD_FONT_NAME, FontWeight.BOLD, GameConfig.HUD_FONT_SIZE));
            gc.fillText(text, startX, centerY);

            // Draw heart to the left of count (static, no animation)
            double heartX = startX - 55;
            double heartY = centerY - heartSize / 2;
            drawHeart(heartX, heartY, heartSize, true);

            // Draw "LIVES" label
            gc.setFill(Color.rgb(150, 150, 180));
            gc.setFont(Font.font(GameConfig.HUD_FONT_NAME, FontWeight.NORMAL, 14));
            gc.fillText("LIVES", heartX - 10, centerY);
        }

        gc.restore();
    }

    private void drawHeart(double x, double y, double size, boolean glow) {
        gc.save();
        
        // Center the heart at the given position
        gc.translate(x, y);
        gc.scale(size / 100.0, size / 100.0);

        // Heart path (centered around 50,50 in the 100x100 coordinate space)
        gc.beginPath();
        gc.appendSVGPath("M 50 90 Q 10 60 10 30 A 20 20 0 0 1 50 30 A 20 20 0 0 1 90 30 Q 90 60 50 90 Z");

        if (glow) {
            // Glow effect for first heart
            gc.setEffect(new GaussianBlur(8));
            gc.setFill(Color.rgb(255, 50, 100, 0.5));
            gc.fill();
            gc.setEffect(null);
        }

        // Heart gradient
        gc.setFill(new RadialGradient(
                0, 0, 40, 30, 60, false, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(255, 100, 120)),
                new Stop(0.5, Color.rgb(255, 50, 80)),
                new Stop(1, Color.rgb(180, 20, 50))));
        gc.fill();

        // Shine highlight
        gc.beginPath();
        gc.appendSVGPath("M 25 35 A 8 8 0 0 1 40 30");
        gc.setStroke(Color.rgb(255, 200, 200, 0.8));
        gc.setLineWidth(6);
        gc.stroke();

        gc.restore();
    }
}
