package arkanoid.level;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Premium deep space background with shooting stars, dynamic nebulae,
 * glowing stars with lens flares, and cosmic dust particles.
 */
public class StarBackground implements Background {

    private final List<Star> stars = new ArrayList<>();
    private final List<Nebula> nebulae = new ArrayList<>();
    private final List<ShootingStar> shootingStars = new ArrayList<>();
    private final List<CosmicDust> cosmicDust = new ArrayList<>();
    private double time = 0;
    private double shootingStarTimer = 0;
    private final Random random = new Random();
    private final int screenWidth;
    private final int screenHeight;

    private static class Star {
        double x, y, size, baseAlpha, twinkleSpeed, twinklePhase;
        Color color;
        boolean isBrightStar; // For lens flare effect
        double pulseOffset;
    }

    private static class Nebula {
        double x, y, radius, baseX, driftPhase, driftSpeed;
        Color color;
        double pulsePhase;
    }

    private static class ShootingStar {
        double x, y, velocityX, velocityY, length, life, maxLife;
        Color color;
    }

    private static class CosmicDust {
        double x, y, size, alpha, driftSpeed, driftPhase;
    }

    public StarBackground(int width, int height) {
        this.screenWidth = width;
        this.screenHeight = height;

        // Create varied stars with different properties
        for (int i = 0; i < 200; i++) {
            Star star = new Star();
            star.x = random.nextDouble() * width;
            star.y = random.nextDouble() * height;
            star.twinkleSpeed = 1.5 + random.nextDouble() * 5;
            star.twinklePhase = random.nextDouble() * Math.PI * 2;
            star.pulseOffset = random.nextDouble() * Math.PI * 2;

            // Varied star sizes - more small stars, fewer large ones
            double sizeRoll = random.nextDouble();
            if (sizeRoll < 0.6) {
                star.size = 0.5 + random.nextDouble() * 1.0; // Tiny stars
                star.baseAlpha = 0.3 + random.nextDouble() * 0.4;
                star.isBrightStar = false;
            } else if (sizeRoll < 0.85) {
                star.size = 1.5 + random.nextDouble() * 1.5; // Medium stars
                star.baseAlpha = 0.5 + random.nextDouble() * 0.4;
                star.isBrightStar = false;
            } else if (sizeRoll < 0.95) {
                star.size = 3 + random.nextDouble() * 2; // Bright stars
                star.baseAlpha = 0.7 + random.nextDouble() * 0.3;
                star.isBrightStar = true;
            } else {
                star.size = 5 + random.nextDouble() * 3; // Giant stars with lens flare
                star.baseAlpha = 0.9;
                star.isBrightStar = true;
            }

            // More varied star colors
            double colorRoll = random.nextDouble();
            if (colorRoll < 0.45) {
                star.color = Color.WHITE;
            } else if (colorRoll < 0.60) {
                star.color = Color.rgb(180, 210, 255); // Blue-white (hot)
            } else if (colorRoll < 0.72) {
                star.color = Color.rgb(130, 180, 255); // Blue (very hot)
            } else if (colorRoll < 0.82) {
                star.color = Color.rgb(255, 250, 220); // Yellow-white
            } else if (colorRoll < 0.90) {
                star.color = Color.rgb(255, 220, 180); // Orange
            } else {
                star.color = Color.rgb(255, 180, 150); // Red giant
            }
            stars.add(star);
        }

        // Create animated nebula clouds
        Color[] nebulaColors = {
                Color.rgb(120, 60, 180, 0.18), // Deep purple
                Color.rgb(60, 120, 200, 0.15), // Blue
                Color.rgb(180, 60, 120, 0.12), // Magenta/pink
                Color.rgb(60, 180, 180, 0.12), // Cyan
                Color.rgb(100, 80, 200, 0.14), // Violet
                Color.rgb(200, 100, 150, 0.10) // Rose
        };

        for (int i = 0; i < 6; i++) {
            Nebula nebula = new Nebula();
            nebula.baseX = random.nextDouble() * width;
            nebula.x = nebula.baseX;
            nebula.y = random.nextDouble() * height * 0.7;
            nebula.radius = 180 + random.nextDouble() * 250;
            nebula.driftPhase = random.nextDouble() * Math.PI * 2;
            nebula.driftSpeed = 0.2 + random.nextDouble() * 0.3;
            nebula.pulsePhase = random.nextDouble() * Math.PI * 2;
            nebula.color = nebulaColors[i % nebulaColors.length];
            nebulae.add(nebula);
        }

        // Create cosmic dust particles
        for (int i = 0; i < 50; i++) {
            CosmicDust dust = new CosmicDust();
            dust.x = random.nextDouble() * width;
            dust.y = random.nextDouble() * height;
            dust.size = 0.5 + random.nextDouble() * 1.5;
            dust.alpha = 0.05 + random.nextDouble() * 0.15;
            dust.driftSpeed = 0.5 + random.nextDouble() * 1.0;
            dust.driftPhase = random.nextDouble() * Math.PI * 2;
            cosmicDust.add(dust);
        }
    }

    @Override
    public void draw(GraphicsContext gc, int width, int height, double dt) {
        time += dt;
        shootingStarTimer += dt;

        gc.save();

        // 1. Rich deep space gradient background
        gc.setFill(new LinearGradient(
                0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(3, 3, 15)),
                new Stop(0.2, Color.rgb(8, 6, 25)),
                new Stop(0.5, Color.rgb(12, 8, 35)),
                new Stop(0.8, Color.rgb(10, 6, 30)),
                new Stop(1, Color.rgb(5, 3, 20))));
        gc.fillRect(0, 0, width, height);

        // 2. Draw animated nebula clouds
        gc.setEffect(new GaussianBlur(70));
        for (Nebula nebula : nebulae) {
            // Animate drift
            nebula.x = nebula.baseX + Math.sin(time * nebula.driftSpeed + nebula.driftPhase) * 30;

            // Animate pulse
            double nebulaPulse = 0.8 + 0.2 * Math.sin(time * 0.5 + nebula.pulsePhase);
            double adjustedRadius = nebula.radius * nebulaPulse;

            gc.setFill(new RadialGradient(
                    0, 0, 0.5, 0.5, 0.5, true, CycleMethod.NO_CYCLE,
                    new Stop(0, nebula.color.deriveColor(0, 1, 1.1, nebulaPulse)),
                    new Stop(0.3, nebula.color.deriveColor(0, 1, 1, 0.7 * nebulaPulse)),
                    new Stop(0.6, nebula.color.deriveColor(0, 1, 1, 0.3 * nebulaPulse)),
                    new Stop(1, Color.TRANSPARENT)));
            gc.fillOval(nebula.x - adjustedRadius, nebula.y - adjustedRadius,
                    adjustedRadius * 2, adjustedRadius * 2);
        }
        gc.setEffect(null);

        // 3. Draw cosmic dust particles (subtle movement)
        for (CosmicDust dust : cosmicDust) {
            double driftX = Math.sin(time * dust.driftSpeed + dust.driftPhase) * 2;
            double driftY = Math.cos(time * dust.driftSpeed * 0.7 + dust.driftPhase) * 2;

            gc.setFill(Color.rgb(200, 180, 255, dust.alpha));
            gc.fillOval(dust.x + driftX, dust.y + driftY, dust.size, dust.size);
        }

        // 4. Draw twinkling stars with varied effects
        for (Star star : stars) {
            double twinkle = Math.sin(time * star.twinkleSpeed + star.twinklePhase);
            double alpha = star.baseAlpha + twinkle * 0.35;
            alpha = Math.max(0.15, Math.min(1.0, alpha));

            // Bright stars get enhanced glow and lens flare
            if (star.isBrightStar) {
                double pulse = 0.85 + 0.15 * Math.sin(time * 2 + star.pulseOffset);

                // Large soft glow
                gc.setEffect(new GaussianBlur(star.size * 2.5));
                gc.setFill(star.color.deriveColor(0, 0.7, 1, alpha * 0.4 * pulse));
                gc.fillOval(star.x - star.size * 2, star.y - star.size * 2,
                        star.size * 6, star.size * 6);
                gc.setEffect(null);

                // Lens flare cross effect for very bright stars
                if (star.size > 4) {
                    double flareLength = star.size * 4 * pulse;
                    double flareAlpha = alpha * 0.25 * pulse;

                    // Vertical flare
                    gc.setFill(new LinearGradient(
                            0, star.y - flareLength, 0, star.y + star.size + flareLength,
                            false, CycleMethod.NO_CYCLE,
                            new Stop(0, Color.TRANSPARENT),
                            new Stop(0.4, star.color.deriveColor(0, 0.3, 1, flareAlpha)),
                            new Stop(0.5, star.color.deriveColor(0, 0.1, 1, flareAlpha * 1.5)),
                            new Stop(0.6, star.color.deriveColor(0, 0.3, 1, flareAlpha)),
                            new Stop(1, Color.TRANSPARENT)));
                    gc.fillRect(star.x + star.size * 0.3, star.y - flareLength,
                            star.size * 0.4, star.size + flareLength * 2);

                    // Horizontal flare
                    gc.setFill(new LinearGradient(
                            star.x - flareLength, 0, star.x + star.size + flareLength, 0,
                            false, CycleMethod.NO_CYCLE,
                            new Stop(0, Color.TRANSPARENT),
                            new Stop(0.4, star.color.deriveColor(0, 0.3, 1, flareAlpha)),
                            new Stop(0.5, star.color.deriveColor(0, 0.1, 1, flareAlpha * 1.5)),
                            new Stop(0.6, star.color.deriveColor(0, 0.3, 1, flareAlpha)),
                            new Stop(1, Color.TRANSPARENT)));
                    gc.fillRect(star.x - flareLength, star.y + star.size * 0.3,
                            star.size + flareLength * 2, star.size * 0.4);
                }

                // Inner glow ring
                gc.setFill(new RadialGradient(
                        0, 0, 0.5, 0.5, 0.5, true, CycleMethod.NO_CYCLE,
                        new Stop(0, star.color.deriveColor(0, 0.5, 1.2, alpha * pulse)),
                        new Stop(0.5, star.color.deriveColor(0, 0.8, 1, alpha * 0.5 * pulse)),
                        new Stop(1, Color.TRANSPARENT)));
                gc.fillOval(star.x - star.size * 0.5, star.y - star.size * 0.5,
                        star.size * 3, star.size * 3);
            } else if (star.size > 1.5) {
                // Medium stars - subtle glow
                gc.setFill(star.color.deriveColor(0, 1, 1, alpha * 0.25));
                gc.fillOval(star.x - star.size * 0.5, star.y - star.size * 0.5,
                        star.size * 3, star.size * 3);
            }

            // Star core
            gc.setFill(star.color.deriveColor(0, 1, 1, alpha));
            gc.fillOval(star.x, star.y, star.size, star.size);

            // Bright white center for larger stars
            if (star.size > 1.2) {
                gc.setFill(Color.rgb(255, 255, 255, alpha * 0.85));
                double centerSize = star.size * 0.4;
                gc.fillOval(star.x + (star.size - centerSize) / 2,
                        star.y + (star.size - centerSize) / 2,
                        centerSize, centerSize);
            }
        }

        // 5. Spawn and draw shooting stars
        if (shootingStarTimer > 2 + random.nextDouble() * 4) {
            shootingStarTimer = 0;
            spawnShootingStar();
        }

        Iterator<ShootingStar> iter = shootingStars.iterator();
        while (iter.hasNext()) {
            ShootingStar ss = iter.next();
            ss.x += ss.velocityX * dt * 60;
            ss.y += ss.velocityY * dt * 60;
            ss.life -= dt;

            if (ss.life <= 0 || ss.x < -50 || ss.x > width + 50 || ss.y > height + 50) {
                iter.remove();
                continue;
            }

            double lifeRatio = ss.life / ss.maxLife;
            double fadeAlpha = lifeRatio;

            // Draw shooting star trail
            double tailX = ss.x - ss.velocityX * ss.length / Math.abs(ss.velocityX) * 0.5;
            double tailY = ss.y - ss.velocityY * ss.length / Math.abs(ss.velocityX) * 0.5;

            // Glow effect
            gc.setEffect(new GaussianBlur(4));
            gc.setStroke(ss.color.deriveColor(0, 0.5, 1, fadeAlpha * 0.6));
            gc.setLineWidth(4);
            gc.strokeLine(ss.x, ss.y, tailX, tailY);
            gc.setEffect(null);

            // Core trail
            gc.setStroke(new LinearGradient(
                    ss.x, ss.y, tailX, tailY, false, CycleMethod.NO_CYCLE,
                    new Stop(0, ss.color.deriveColor(0, 0.2, 1.2, fadeAlpha)),
                    new Stop(0.3, ss.color.deriveColor(0, 1, 1, fadeAlpha * 0.8)),
                    new Stop(1, Color.TRANSPARENT)));
            gc.setLineWidth(2);
            gc.strokeLine(ss.x, ss.y, tailX, tailY);

            // Bright head
            gc.setFill(Color.rgb(255, 255, 255, fadeAlpha));
            gc.fillOval(ss.x - 2, ss.y - 2, 4, 4);
        }

        gc.restore();
    }

    private void spawnShootingStar() {
        ShootingStar ss = new ShootingStar();

        // Start from top or sides
        if (random.nextBoolean()) {
            ss.x = random.nextDouble() * screenWidth;
            ss.y = -10;
        } else {
            ss.x = screenWidth + 10;
            ss.y = random.nextDouble() * screenHeight * 0.5;
        }

        // Diagonal velocity (moving down and left typically)
        ss.velocityX = -3 - random.nextDouble() * 5;
        ss.velocityY = 2 + random.nextDouble() * 4;
        ss.length = 40 + random.nextDouble() * 60;
        ss.maxLife = 1.5 + random.nextDouble() * 1.5;
        ss.life = ss.maxLife;

        // Shooting star colors
        double colorRoll = random.nextDouble();
        if (colorRoll < 0.6) {
            ss.color = Color.rgb(200, 220, 255); // Blue-white
        } else if (colorRoll < 0.8) {
            ss.color = Color.WHITE;
        } else {
            ss.color = Color.rgb(255, 230, 200); // Warm white
        }

        shootingStars.add(ss);
    }
}
