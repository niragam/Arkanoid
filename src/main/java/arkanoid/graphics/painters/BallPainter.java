package arkanoid.graphics.painters;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;

import arkanoid.entity.Ball;
import arkanoid.graphics.Painter;

/**
 * Painter for Ball entities with enhanced glow effects.
 */
public class BallPainter implements Painter<Ball> {

    @Override
    public void paint(GraphicsContext gc, Ball ball) {
        double cx = ball.getCenter().getX();
        double cy = ball.getCenter().getY();
        double r = ball.getRadius();
        double diameter = r * 2.0;
        Color color = ball.getColor();

        gc.save();

        // 1. Outer glow aura (large, soft)
        gc.setEffect(new GaussianBlur(15));
        gc.setFill(Color.rgb(100, 200, 255, 0.3));
        gc.fillOval(cx - r * 2, cy - r * 2, diameter * 2, diameter * 2);
        gc.setEffect(null);

        // 2. Inner glow ring
        gc.setFill(new RadialGradient(
                0, 0, 0.5, 0.5, 0.5, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.TRANSPARENT),
                new Stop(0.6, Color.TRANSPARENT),
                new Stop(0.8, Color.rgb(150, 220, 255, 0.5)),
                new Stop(1, Color.rgb(100, 180, 255, 0.8))));
        gc.fillOval(cx - r * 1.5, cy - r * 1.5, diameter * 1.5, diameter * 1.5);

        // 3. Main ball body with 3D sphere effect
        gc.setFill(new RadialGradient(
                0, 0, 0.3, 0.3, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.WHITE),
                new Stop(0.1, Color.rgb(240, 250, 255)),
                new Stop(0.4, color.brighter()),
                new Stop(0.7, color),
                new Stop(1, color.darker().darker())));
        gc.fillOval(cx - r, cy - r, diameter, diameter);

        // 4. Specular highlight (top-left shine)
        gc.setFill(new RadialGradient(
                0, 0, 0.3, 0.3, 0.4, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(255, 255, 255, 0.9)),
                new Stop(0.5, Color.rgb(255, 255, 255, 0.3)),
                new Stop(1, Color.TRANSPARENT)));
        gc.fillOval(cx - r * 0.8, cy - r * 0.8, r * 1.2, r * 1.2);

        // 5. Subtle rim light (bottom edge)
        gc.setStroke(new RadialGradient(
                0, 0, 0.5, 0.8, 0.5, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.TRANSPARENT),
                new Stop(0.7, Color.TRANSPARENT),
                new Stop(1, Color.rgb(200, 230, 255, 0.6))));
        gc.setLineWidth(1.5);
        gc.strokeOval(cx - r, cy - r, diameter, diameter);

        gc.restore();
    }
}
