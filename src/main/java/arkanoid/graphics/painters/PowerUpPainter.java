package arkanoid.graphics.painters;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.effect.DropShadow;

import arkanoid.entity.PowerUp;
import arkanoid.graphics.Painter;

/**
 * Painter for PowerUp entities.
 */
public class PowerUpPainter implements Painter<PowerUp> {

    @Override
    public void paint(GraphicsContext gc, PowerUp powerUp) {
        if (powerUp.isCollected()) {
            return;
        }

        double cx = powerUp.getCenter().getX();
        double cy = powerUp.getCenter().getY();
        double size = powerUp.getSize();
        PowerUp.Type type = powerUp.getType();

        gc.save();

        // Add a subtle glow/shadow for all powerups
        DropShadow glow = new DropShadow();
        glow.setColor(type.getColor());

        if (type == PowerUp.Type.MULTI_BALL) {
            glow.setRadius(8); // Reduced from 15
            glow.setSpread(0.2); // Reduced from 0.4
        } else {
            glow.setRadius(15);
            glow.setSpread(0.4);
        }

        gc.setEffect(glow);

        switch (type) {
            case EXTRA_LIFE:
                drawHeart(gc, cx, cy, size * 1.5, type.getColor());
                break;
            case WIDE_PADDLE:
                drawWidePaddle(gc, cx, cy, size, type.getColor());
                break;
            case MULTI_BALL:
                drawMultiBall(gc, cx, cy, size, type.getColor());
                break;
        }

        gc.restore();
    }

    private void drawHeart(GraphicsContext gc, double cx, double cy, double size, Color color) {
        double r = size / 2;
        gc.translate(cx - r, cy - r);
        gc.scale(size / 100.0, size / 100.0);

        gc.beginPath();
        // Standard heart SVG path
        gc.appendSVGPath("M 50 90 Q 10 60 10 30 A 20 20 0 0 1 50 30 A 20 20 0 0 1 90 30 Q 90 60 50 90 Z");

        // Gradient fill
        gc.setFill(new RadialGradient(
                0, 0, 30, 30, 60, false, CycleMethod.NO_CYCLE,
                new Stop(0, Color.WHITE),
                new Stop(0.4, color),
                new Stop(1, color.darker())));
        gc.fill();

        // Outline
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(3);
        gc.stroke();
    }

    private void drawWidePaddle(GraphicsContext gc, double cx, double cy, double size, Color color) {
        double width = size * 1.5;
        double height = size * 0.5;

        // Draw the paddle shape
        gc.setFill(new LinearGradient(
                0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, color.brighter()),
                new Stop(1, color.darker())));
        gc.fillRoundRect(cx - width / 2, cy - height / 2, width, height, 10, 10);

        // Draw arrows <->
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);

        // Left arrow
        gc.strokeLine(cx - width / 2 - 5, cy, cx - width / 2 + 5, cy - 5);
        gc.strokeLine(cx - width / 2 - 5, cy, cx - width / 2 + 5, cy + 5);

        // Right arrow
        gc.strokeLine(cx + width / 2 + 5, cy, cx + width / 2 - 5, cy - 5);
        gc.strokeLine(cx + width / 2 + 5, cy, cx + width / 2 - 5, cy + 5);
    }

    private void drawMultiBall(GraphicsContext gc, double cx, double cy, double size, Color color) {
        double r = size / 3; // Smaller balls

        // Draw 3 balls in a triangle
        drawMiniBall(gc, cx, cy - r, r, color); // Top
        drawMiniBall(gc, cx - r, cy + r * 0.8, r, color); // Bottom Left
        drawMiniBall(gc, cx + r, cy + r * 0.8, r, color); // Bottom Right
    }

    private void drawMiniBall(GraphicsContext gc, double x, double y, double r, Color color) {
        gc.setFill(new RadialGradient(
                0, 0, x - r * 0.3, y - r * 0.3, r, false, CycleMethod.NO_CYCLE,
                new Stop(0, Color.WHITE),
                new Stop(1, color)));
        gc.fillOval(x - r, y - r, r * 2, r * 2);
    }
}
