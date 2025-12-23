package arkanoid.level;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Bright sunny day background with floating balloons and clouds.
 */
public class SunBackground implements Background {

    private final List<Cloud> clouds = new ArrayList<>();
    private final List<Balloon> balloons = new ArrayList<>();
    private final Random random = new Random();
    private double sunRotation = 0;
    private double time = 0;

    private static class Cloud {
        double x, y, speed, width, height;
        double opacity;
    }

    private static class Balloon {
        double x, y, speedY, swayPhase, size;
        Color color;
    }

    public SunBackground(int width, int height) {
        // Create clouds
        for (int i = 0; i < 6; i++) {
            Cloud c = new Cloud();
            c.x = random.nextDouble() * width * 1.5 - width * 0.25;
            c.y = 80 + random.nextDouble() * 200;
            c.speed = 0.3 + random.nextDouble() * 0.5;
            c.width = 120 + random.nextDouble() * 100;
            c.height = 40 + random.nextDouble() * 30;
            c.opacity = 0.6 + random.nextDouble() * 0.3;
            clouds.add(c);
        }

        // Create balloons
        Color[] balloonColors = {
                Color.rgb(255, 80, 80),
                Color.rgb(255, 180, 50),
                Color.rgb(100, 200, 255),
                Color.rgb(255, 100, 200),
                Color.rgb(100, 255, 150)
        };
        for (int i = 0; i < 8; i++) {
            Balloon b = new Balloon();
            b.x = random.nextDouble() * width;
            b.y = height + random.nextDouble() * 200;
            b.speedY = -(0.5 + random.nextDouble() * 1.5);
            b.swayPhase = random.nextDouble() * Math.PI * 2;
            b.size = 25 + random.nextDouble() * 20;
            b.color = balloonColors[random.nextInt(balloonColors.length)];
            balloons.add(b);
        }
    }

    @Override
    public void draw(GraphicsContext gc, int width, int height, double dt) {
        time += dt;
        sunRotation += dt * 18; // ~0.3 at 60fps

        gc.save();

        // 1. Sky gradient
        gc.setFill(new LinearGradient(
                0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(100, 180, 255)),
                new Stop(0.5, Color.rgb(150, 210, 255)),
                new Stop(1, Color.rgb(200, 235, 255))));
        gc.fillRect(0, 0, width, height);

        // 2. Sun with rays
        drawSun(gc, 120, 120);

        // 3. Clouds
        for (Cloud c : clouds) {
            c.x += c.speed;
            if (c.x > width + c.width) {
                c.x = -c.width;
            }
            drawCloud(gc, c);
        }

        // 4. Floating balloons
        for (Balloon b : balloons) {
            b.y += b.speedY;
            b.x += Math.sin(time * 2 + b.swayPhase) * 0.5;

            if (b.y < -100) {
                b.y = height + 50;
                b.x = random.nextDouble() * width;
            }

            drawBalloon(gc, b);
        }

        gc.restore();
    }

    private void drawSun(GraphicsContext gc, double x, double y) {
        double sunRadius = 50;

        gc.save();

        // Sun glow
        gc.setEffect(new GaussianBlur(30));
        gc.setFill(new RadialGradient(
                0, 0, 0.5, 0.5, 0.5, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(255, 255, 200, 0.8)),
                new Stop(0.5, Color.rgb(255, 220, 100, 0.4)),
                new Stop(1, Color.TRANSPARENT)));
        gc.fillOval(x - sunRadius * 2, y - sunRadius * 2, sunRadius * 4, sunRadius * 4);
        gc.setEffect(null);

        // Rotating rays
        gc.save();
        gc.translate(x, y);
        gc.rotate(sunRotation);

        gc.setStroke(new LinearGradient(
                0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(255, 220, 100, 0.8)),
                new Stop(1, Color.rgb(255, 200, 50, 0.2))));
        gc.setLineWidth(4);

        for (int i = 0; i < 12; i++) {
            double angle = Math.toRadians(i * 30);
            double x1 = Math.cos(angle) * (sunRadius + 10);
            double y1 = Math.sin(angle) * (sunRadius + 10);
            double x2 = Math.cos(angle) * (sunRadius + 35);
            double y2 = Math.sin(angle) * (sunRadius + 35);
            gc.strokeLine(x1, y1, x2, y2);
        }
        gc.restore();

        // Sun body
        gc.setFill(new RadialGradient(
                0, 0, 0.3, 0.3, 0.7, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(255, 255, 220)),
                new Stop(0.5, Color.rgb(255, 230, 100)),
                new Stop(1, Color.rgb(255, 200, 50))));
        gc.fillOval(x - sunRadius, y - sunRadius, sunRadius * 2, sunRadius * 2);

        gc.restore();
    }

    private void drawCloud(GraphicsContext gc, Cloud c) {
        gc.save();

        gc.setFill(Color.rgb(255, 255, 255, c.opacity));

        // Cloud made of overlapping ellipses
        double cx = c.x;
        double cy = c.y;
        double w = c.width;
        double h = c.height;

        gc.fillOval(cx, cy, w * 0.5, h);
        gc.fillOval(cx + w * 0.2, cy - h * 0.3, w * 0.4, h * 1.2);
        gc.fillOval(cx + w * 0.4, cy, w * 0.5, h);
        gc.fillOval(cx + w * 0.15, cy + h * 0.2, w * 0.6, h * 0.7);

        gc.restore();
    }

    private void drawBalloon(GraphicsContext gc, Balloon b) {
        gc.save();

        double x = b.x;
        double y = b.y;
        double size = b.size;

        // Balloon shadow
        gc.setFill(Color.rgb(0, 0, 0, 0.1));
        gc.fillOval(x - size / 2 + 5, y - size * 0.6 + 5, size, size * 1.2);

        // Balloon body gradient
        gc.setFill(new RadialGradient(
                0, 0, 0.3, 0.3, 0.6, true, CycleMethod.NO_CYCLE,
                new Stop(0, b.color.brighter().brighter()),
                new Stop(0.5, b.color),
                new Stop(1, b.color.darker())));
        gc.fillOval(x - size / 2, y - size * 0.6, size, size * 1.2);

        // Balloon highlight
        gc.setFill(Color.rgb(255, 255, 255, 0.4));
        gc.fillOval(x - size / 3, y - size * 0.5, size * 0.35, size * 0.5);

        // Balloon knot
        gc.setFill(b.color.darker().darker());
        gc.fillOval(x - 3, y + size * 0.55, 6, 8);

        // String
        gc.setStroke(Color.rgb(100, 100, 100, 0.6));
        gc.setLineWidth(1);
        double stringY = y + size * 0.6;
        gc.beginPath();
        gc.moveTo(x, stringY);
        gc.quadraticCurveTo(x + Math.sin(time * 3 + b.swayPhase) * 10, stringY + 20,
                x + Math.sin(time * 2 + b.swayPhase) * 5, stringY + 40);
        gc.stroke();

        gc.restore();
    }
}
