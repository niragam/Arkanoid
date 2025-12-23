package arkanoid.level;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;

/**
 * Synthwave/retrowave style grid background with static neon effects.
 */
public class GridBackground implements Background {

    private static final int NUM_HORIZONTAL_LINES = 18;
    private static final int NUM_VERTICAL_LINES = 25;

    public GridBackground() {
    }

    @Override
    public void draw(GraphicsContext gc, int width, int height, double dt) {
        // GridBackground is static, dt not needed but required by interface
        gc.save();

        double horizonY = height * 0.5;

        // 1. Sky gradient (dark purple to magenta horizon)
        gc.setFill(new LinearGradient(
                0, 0, 0, horizonY, false, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(8, 4, 25)),
                new Stop(0.4, Color.rgb(25, 8, 50)),
                new Stop(0.7, Color.rgb(60, 15, 80)),
                new Stop(0.9, Color.rgb(120, 40, 100)),
                new Stop(1, Color.rgb(180, 70, 130))));
        gc.fillRect(0, 0, width, horizonY);

        // 2. Ground plane (dark with purple tint)
        gc.setFill(new LinearGradient(
                0, horizonY, 0, height, false, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(35, 8, 50)),
                new Stop(0.2, Color.rgb(18, 4, 35)),
                new Stop(1, Color.rgb(5, 2, 12))));
        gc.fillRect(0, horizonY, width, height - horizonY);

        // 3. Setting sun
        drawSun(gc, width, horizonY);

        // 4. Horizon glow
        gc.setEffect(new GaussianBlur(12));
        gc.setStroke(Color.rgb(255, 100, 180, 0.7));
        gc.setLineWidth(6);
        gc.strokeLine(0, horizonY, width, horizonY);
        gc.setEffect(null);

        // Crisp horizon line
        gc.setStroke(Color.rgb(255, 150, 200));
        gc.setLineWidth(2);
        gc.strokeLine(0, horizonY, width, horizonY);

        // 5. Static perspective grid on ground
        drawPerspectiveGrid(gc, width, height, horizonY);

        // 6. Subtle vertical lines in sky
        gc.setStroke(Color.rgb(200, 50, 150, 0.08));
        gc.setLineWidth(1);
        for (int x = 0; x < width; x += 100) {
            gc.strokeLine(x, 0, x, horizonY);
        }

        gc.restore();
    }

    private void drawSun(GraphicsContext gc, int width, double horizonY) {
        double sunSize = 160;
        double sunX = width / 2.0;
        double sunY = horizonY - sunSize * 0.25;

        gc.save();

        // Outer glow
        gc.setEffect(new GaussianBlur(40));
        gc.setFill(new RadialGradient(
                0, 0, 0.5, 0.5, 0.5, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(255, 200, 100, 0.5)),
                new Stop(0.4, Color.rgb(255, 100, 150, 0.25)),
                new Stop(1, Color.TRANSPARENT)));
        gc.fillOval(sunX - sunSize * 1.2, sunY - sunSize * 0.9,
                sunSize * 2.4, sunSize * 1.8);
        gc.setEffect(null);

        // Sun body gradient
        gc.setFill(new LinearGradient(
                0, sunY - sunSize / 2, 0, sunY + sunSize / 2, false, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(255, 255, 120)),
                new Stop(0.25, Color.rgb(255, 210, 80)),
                new Stop(0.5, Color.rgb(255, 150, 100)),
                new Stop(0.75, Color.rgb(255, 80, 110)),
                new Stop(1, Color.rgb(200, 50, 130))));

        // Clip to area above horizon
        gc.beginPath();
        gc.rect(0, 0, width, horizonY);
        gc.clip();
        gc.fillOval(sunX - sunSize / 2, sunY - sunSize / 2, sunSize, sunSize);

        // Sun stripes (retrowave style)
        gc.setFill(Color.rgb(35, 8, 50, 0.92));
        double stripeGap = 14;
        for (int i = 0; i < 7; i++) {
            double stripeY = sunY + i * stripeGap - 15;
            double stripeHeight = 3 + i * 2;
            if (stripeY < horizonY && stripeY + stripeHeight > sunY - sunSize / 2) {
                gc.fillRect(sunX - sunSize / 2, stripeY, sunSize, stripeHeight);
            }
        }

        gc.restore();
    }

    private void drawPerspectiveGrid(GraphicsContext gc, int width, int height, double horizonY) {
        double centerX = width / 2.0;
        double groundHeight = height - horizonY;

        // Vertical perspective lines (converge at horizon center)
        for (int i = -NUM_VERTICAL_LINES; i <= NUM_VERTICAL_LINES; i++) {
            double spread = i * 60;
            double topX = centerX + spread * 0.05;
            double bottomX = centerX + spread * 3.5;

            // Color gradient from cyan at center to magenta at edges
            double t = Math.abs(i) / (double) NUM_VERTICAL_LINES;
            double alpha = 0.35 + 0.25 * (1 - t);
            Color lineColor = Color.rgb(
                    (int) (50 + t * 200),
                    (int) (255 * (1 - t * 0.6)),
                    255,
                    alpha);
            gc.setStroke(lineColor);
            gc.setLineWidth(1 + (1 - t) * 0.5);
            gc.strokeLine(topX, horizonY, bottomX, height);
        }

        // Horizontal lines with perspective spacing (static)
        for (int i = 1; i <= NUM_HORIZONTAL_LINES; i++) {
            double t = i / (double) (NUM_HORIZONTAL_LINES + 1);
            // Exponential spacing for depth effect
            double lineY = horizonY + Math.pow(t, 2.5) * groundHeight;

            // Lines get brighter and thicker as they get closer
            double alpha = 0.15 + 0.55 * t;
            double lineWidth = 0.5 + t * 2;

            gc.setStroke(Color.rgb(0, 255, 255, alpha));
            gc.setLineWidth(lineWidth);
            gc.strokeLine(0, lineY, width, lineY);
        }
    }
}
