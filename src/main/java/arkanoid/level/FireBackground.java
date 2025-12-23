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
 * Intense fire/lava background with rising embers and heat distortion effects.
 */
public class FireBackground implements Background {

    private final List<Ember> embers = new ArrayList<>();
    private final List<LavaBlob> lavaBlobs = new ArrayList<>();
    private final Random random = new Random();
    private double time = 0;

    private static class Ember {
        double x, y, speedY, speedX, size, life, maxLife;
        Color color;
    }

    private static class LavaBlob {
        double x, baseY, phase, amplitude, size;
    }

    public FireBackground(int width, int height) {
        // Create embers
        for (int i = 0; i < 80; i++) {
            Ember e = new Ember();
            resetEmber(e, width, height, true);
            embers.add(e);
        }

        // Create lava blobs along bottom
        for (int i = 0; i < 12; i++) {
            LavaBlob blob = new LavaBlob();
            blob.x = (width / 12.0) * i + random.nextDouble() * 50;
            blob.baseY = height - 40 - random.nextDouble() * 30;
            blob.phase = random.nextDouble() * Math.PI * 2;
            blob.amplitude = 10 + random.nextDouble() * 20;
            blob.size = 60 + random.nextDouble() * 80;
            lavaBlobs.add(blob);
        }
    }

    private void resetEmber(Ember e, int width, int height, boolean randomY) {
        e.x = random.nextDouble() * width;
        e.y = randomY ? random.nextDouble() * height : height + 20;
        e.speedY = -(2 + random.nextDouble() * 4);
        e.speedX = (random.nextDouble() - 0.5) * 2;
        e.size = 2 + random.nextDouble() * 4;
        e.maxLife = 1.5 + random.nextDouble();
        e.life = e.maxLife;

        // Ember colors: yellow, orange, red
        double colorRoll = random.nextDouble();
        if (colorRoll < 0.3) {
            e.color = Color.rgb(255, 255, 100); // Yellow
        } else if (colorRoll < 0.7) {
            e.color = Color.rgb(255, 180, 50); // Orange
        } else {
            e.color = Color.rgb(255, 100, 50); // Red-orange
        }
    }

    @Override
    public void draw(GraphicsContext gc, int width, int height, double dt) {
        time += dt;

        gc.save();

        // 1. Gradient background (dark to fiery)
        gc.setFill(new LinearGradient(
                0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(20, 5, 5)),
                new Stop(0.3, Color.rgb(60, 15, 10)),
                new Stop(0.6, Color.rgb(120, 30, 10)),
                new Stop(0.85, Color.rgb(180, 60, 20)),
                new Stop(1, Color.rgb(220, 100, 30))));
        gc.fillRect(0, 0, width, height);

        // 2. Animated lava glow at bottom
        gc.setEffect(new GaussianBlur(40));
        gc.setFill(new LinearGradient(
                0, height - 150, 0, height, false, CycleMethod.NO_CYCLE,
                new Stop(0, Color.TRANSPARENT),
                new Stop(0.3, Color.rgb(255, 100, 0, 0.3)),
                new Stop(0.7, Color.rgb(255, 150, 0, 0.5)),
                new Stop(1, Color.rgb(255, 200, 50, 0.7))));
        gc.fillRect(0, height - 150, width, 150);
        gc.setEffect(null);

        // 3. Lava blobs (bubbling effect)
        for (LavaBlob blob : lavaBlobs) {
            double blobY = blob.baseY + Math.sin(time * 2 + blob.phase) * blob.amplitude;

            gc.setEffect(new GaussianBlur(15));
            gc.setFill(new RadialGradient(
                    0, 0, 0.5, 0.3, 0.5, true, CycleMethod.NO_CYCLE,
                    new Stop(0, Color.rgb(255, 255, 150, 0.8)),
                    new Stop(0.3, Color.rgb(255, 180, 50, 0.6)),
                    new Stop(0.7, Color.rgb(255, 100, 20, 0.4)),
                    new Stop(1, Color.TRANSPARENT)));
            gc.fillOval(blob.x - blob.size / 2, blobY - blob.size / 2, blob.size, blob.size);
        }
        gc.setEffect(null);

        // 4. Rising embers
        for (Ember e : embers) {
            // Update
            e.x += e.speedX + Math.sin(time * 3 + e.y * 0.1) * 0.5;
            e.y += e.speedY;
            e.life -= 0.016;

            // Reset if needed
            if (e.life <= 0 || e.y < -20) {
                resetEmber(e, width, height, false);
                continue;
            }

            // Draw ember with glow
            double alpha = Math.min(1, e.life / (e.maxLife * 0.3));
            double glowSize = e.size * 3;

            // Outer glow
            gc.setFill(e.color.deriveColor(0, 1, 1, alpha * 0.3));
            gc.fillOval(e.x - glowSize / 2, e.y - glowSize / 2, glowSize, glowSize);

            // Core
            gc.setFill(e.color.deriveColor(0, 1, 1, alpha));
            gc.fillOval(e.x - e.size / 2, e.y - e.size / 2, e.size, e.size);

            // Bright center
            gc.setFill(Color.rgb(255, 255, 200, alpha * 0.8));
            gc.fillOval(e.x - e.size / 4, e.y - e.size / 4, e.size / 2, e.size / 2);
        }

        // 5. Heat shimmer effect (subtle)
        double shimmerAlpha = 0.05 + 0.03 * Math.sin(time * 5);
        gc.setFill(Color.rgb(255, 200, 100, shimmerAlpha));
        gc.fillRect(0, 0, width, height);

        gc.restore();
    }
}
