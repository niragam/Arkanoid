package arkanoid.graphics.painters;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.geometry.VPos;

import arkanoid.core.GameConfig;
import arkanoid.entity.Block;
import arkanoid.graphics.Painter;

/**
 * Painter for Block entities with enhanced visual effects.
 */
public class BlockPainter implements Painter<Block> {

    @Override
    public void paint(GraphicsContext gc, Block block) {
        double x = block.getCollisionRectangle().getUpperLeft().getX();
        double y = block.getCollisionRectangle().getUpperLeft().getY();
        double w = block.getCollisionRectangle().getWidth();
        double h = block.getCollisionRectangle().getHeight();
        Color color = block.getColor();
        double bevel = GameConfig.BLOCK_BEVEL_SIZE;

        gc.save();

        // Skip glow effect for border blocks (grey/indestructible)
        if (!block.isIndestructible()) {
            // Subtle outer glow based on block color
            DropShadow glow = new DropShadow();
            glow.setColor(color.deriveColor(0, 1.2, 1.5, 0.4));
            glow.setRadius(8);
            glow.setSpread(0.1);
            gc.setEffect(glow);
        }

        // Main block body with gradient
        gc.setFill(new LinearGradient(
                0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, color.brighter().brighter()),
                new Stop(0.3, color.brighter()),
                new Stop(0.7, color),
                new Stop(1, color.darker())));
        gc.fillRoundRect(x + 1, y + 1, w - 2, h - 2, 4, 4);

        gc.setEffect(null);

        // Top highlight edge (glass-like shine)
        gc.setFill(new LinearGradient(
                0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(255, 255, 255, 0.7)),
                new Stop(0.5, Color.rgb(255, 255, 255, 0.2)),
                new Stop(1, Color.TRANSPARENT)));
        gc.fillRoundRect(x + 2, y + 2, w - 4, h / 3, 3, 3);

        // Left edge highlight
        gc.setFill(new LinearGradient(
                0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(255, 255, 255, 0.4)),
                new Stop(1, Color.TRANSPARENT)));
        gc.fillRect(x + 2, y + 2, bevel, h - 4);

        // Bottom/Right shadow edge
        gc.setFill(new LinearGradient(
                0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.TRANSPARENT),
                new Stop(0.7, Color.rgb(0, 0, 0, 0.2)),
                new Stop(1, Color.rgb(0, 0, 0, 0.4))));
        gc.fillRect(x + 2, y + h - bevel - 1, w - 4, bevel);

        // Block border
        gc.setStroke(color.darker().darker());
        gc.setLineWidth(1);
        gc.strokeRoundRect(x + 0.5, y + 0.5, w - 1, h - 1, 4, 4);

        // Draw hit points indicator for multi-hit blocks
        if (block.getHitPoints() > 1 && !block.isIndestructible()) {
            gc.setEffect(new DropShadow(3, Color.BLACK));
            gc.setFill(Color.WHITE);
            gc.setFont(Font.font(GameConfig.DEFAULT_FONT_FAMILY, FontWeight.BOLD, GameConfig.BLOCK_HP_FONT_SIZE));
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setTextBaseline(VPos.CENTER);
            gc.fillText(String.valueOf(block.getHitPoints()), x + w / 2, y + h / 2);
            gc.setEffect(null);
        }

        gc.restore();
    }
}
