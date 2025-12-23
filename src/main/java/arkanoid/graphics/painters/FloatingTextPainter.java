package arkanoid.graphics.painters;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.geometry.VPos;

import arkanoid.core.GameConfig;
import arkanoid.entity.FloatingText;
import arkanoid.graphics.Painter;

/**
 * Painter for FloatingText entities with glow and scale animation.
 */
public class FloatingTextPainter implements Painter<FloatingText> {

    @Override
    public void paint(GraphicsContext gc, FloatingText ft) {
        if (ft.shouldRemove()) {
            return;
        }

        double lifeProgress = ft.getLifeTime() / ft.getMaxLifeTime();
        double opacity = 1.0 - lifeProgress;
        if (opacity < 0) {
            opacity = 0;
        }

        // Scale animation: starts at 1.5x and shrinks to 1x over lifetime
        double scale = 1.0 + 0.5 * (1.0 - lifeProgress * lifeProgress);

        // Slight horizontal drift based on text content (for variety)
        double drift = Math.sin(ft.getLifeTime() * 3) * 5;

        double x = ft.getPosition().getX() + drift;
        double y = ft.getPosition().getY();

        gc.save();

        // Apply transformations
        gc.translate(x, y);
        gc.scale(scale, scale);
        gc.translate(-x, -y);

        // Determine colors based on text (score bonuses get gold, multipliers get special color)
        Color textColor = ft.getColor();
        Color glowColor = Color.CYAN;

        String text = ft.getText();
        if (text.contains("x")) {
            // Multiplier bonus - gold/orange
            textColor = Color.rgb(255, 220, 100);
            glowColor = Color.rgb(255, 180, 50);
        } else if (text.startsWith("+")) {
            // Regular score - cyan/white
            textColor = Color.WHITE;
            glowColor = Color.rgb(100, 200, 255);
        }

        // 1. Outer glow
        gc.setEffect(new GaussianBlur(8));
        gc.setGlobalAlpha(opacity * 0.6);
        gc.setFill(glowColor);
        gc.setFont(Font.font(GameConfig.DEFAULT_FONT_FAMILY, FontWeight.BOLD, GameConfig.FLOATING_TEXT_FONT_SIZE + 2));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.fillText(text, x, y);

        // 2. Drop shadow
        gc.setEffect(new DropShadow(4, 2, 2, Color.rgb(0, 0, 0, 0.5)));
        gc.setGlobalAlpha(opacity);
        gc.setFill(textColor);
        gc.setFont(Font.font(GameConfig.DEFAULT_FONT_FAMILY, FontWeight.BOLD, GameConfig.FLOATING_TEXT_FONT_SIZE));
        gc.fillText(text, x, y);

        // 3. Main text with inner glow
        DropShadow innerGlow = new DropShadow();
        innerGlow.setColor(glowColor);
        innerGlow.setRadius(3);
        innerGlow.setSpread(0.5);
        gc.setEffect(innerGlow);
        gc.fillText(text, x, y);

        // 4. Bright center highlight
        gc.setEffect(null);
        gc.setGlobalAlpha(opacity * 0.8);
        gc.setFill(Color.WHITE);
        gc.fillText(text, x, y);

        gc.restore();
    }
}
