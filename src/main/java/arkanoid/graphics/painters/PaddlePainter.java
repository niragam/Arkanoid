package arkanoid.graphics.painters;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;

import arkanoid.entity.Paddle;
import arkanoid.graphics.Painter;

/**
 * Painter for Paddle entities with futuristic RGB glow and holographic effects.
 */
public class PaddlePainter implements Painter<Paddle> {

        // Animation state for various effects
        private double pulsePhase = 0;
        private double rgbPhase = 0;
        private double scanlineOffset = 0;
        private static final double TWO_PI = Math.PI * 2;

        @Override
        public void paint(GraphicsContext gc, Paddle paddle) {
                double x = paddle.getCollisionRectangle().getUpperLeft().getX();
                double y = paddle.getCollisionRectangle().getUpperLeft().getY();
                double w = paddle.getCollisionRectangle().getWidth();
                double h = paddle.getCollisionRectangle().getHeight();

                gc.save();

                // Update animations
                pulsePhase += 0.08;
                rgbPhase += 0.03;
                scanlineOffset += 2;
                if (pulsePhase > TWO_PI)
                        pulsePhase -= TWO_PI;
                if (rgbPhase > TWO_PI)
                        rgbPhase -= TWO_PI;
                if (scanlineOffset > h)
                        scanlineOffset = 0;

                double pulse = 0.7 + 0.3 * Math.sin(pulsePhase);
                double fastPulse = 0.8 + 0.2 * Math.sin(pulsePhase * 3);

                // Calculate RGB shifting colors
                Color rgbColor1 = Color.hsb((rgbPhase * 180 / Math.PI) % 360, 0.9, 1.0);
                Color rgbColor2 = Color.hsb((rgbPhase * 180 / Math.PI + 120) % 360, 0.9, 1.0);
                Color rgbColor3 = Color.hsb((rgbPhase * 180 / Math.PI + 240) % 360, 0.9, 1.0);

                // 1. Large multi-layered outer glow (RGB neon effect)
                gc.setEffect(new GaussianBlur(25));
                gc.setFill(Color.rgb(0, 150, 255, 0.25 * pulse));
                gc.fillRoundRect(x - 15, y - 8, w + 30, h + 16, h + 16, h + 16);

                // Secondary RGB glow layer
                gc.setFill(rgbColor1.deriveColor(0, 1, 1, 0.15 * pulse));
                gc.fillRoundRect(x - 12, y - 6, w + 24, h + 12, h + 12, h + 12);
                gc.setEffect(null);

                // 2. Animated RGB edge ring
                DropShadow rgbGlow = new DropShadow();
                rgbGlow.setColor(rgbColor1.deriveColor(0, 1, 1, 0.9));
                rgbGlow.setRadius(18);
                rgbGlow.setSpread(0.35);
                gc.setEffect(rgbGlow);

                // 3. Dark chrome frame with subtle gradient
                gc.setFill(new LinearGradient(
                                0, y, 0, y + h, false, CycleMethod.NO_CYCLE,
                                new Stop(0, Color.rgb(60, 65, 80)),
                                new Stop(0.3, Color.rgb(35, 40, 55)),
                                new Stop(0.7, Color.rgb(25, 28, 38)),
                                new Stop(1, Color.rgb(15, 18, 25))));
                gc.fillRoundRect(x, y, w, h, h, h);
                gc.setEffect(null);

                // 4. RGB animated border
                double borderWidth = 2;
                gc.setStroke(new LinearGradient(
                                x, 0, x + w, 0, false, CycleMethod.NO_CYCLE,
                                new Stop(0, rgbColor1.deriveColor(0, 1, 1, 0.8)),
                                new Stop(0.33, rgbColor2.deriveColor(0, 1, 1, 0.8)),
                                new Stop(0.66, rgbColor3.deriveColor(0, 1, 1, 0.8)),
                                new Stop(1, rgbColor1.deriveColor(0, 1, 1, 0.8))));
                gc.setLineWidth(borderWidth);
                gc.strokeRoundRect(x + borderWidth / 2, y + borderWidth / 2,
                                w - borderWidth, h - borderWidth, h - borderWidth, h - borderWidth);

                // 5. Main metallic body with premium finish
                double margin = 4;
                gc.setFill(new LinearGradient(
                                0, y, 0, y + h, false, CycleMethod.NO_CYCLE,
                                new Stop(0, Color.rgb(200, 210, 230)),
                                new Stop(0.1, Color.rgb(170, 185, 210)),
                                new Stop(0.4, Color.rgb(120, 135, 160)),
                                new Stop(0.6, Color.rgb(90, 105, 130)),
                                new Stop(0.9, Color.rgb(60, 75, 100)),
                                new Stop(1, Color.rgb(45, 55, 75))));
                gc.fillRoundRect(x + margin, y + margin, w - margin * 2, h - margin * 2,
                                h - margin * 2, h - margin * 2);

                // 6. Holographic scanlines effect
                gc.save();
                gc.beginPath();
                gc.rect(x + margin, y + margin, w - margin * 2, h - margin * 2);
                gc.clip();

                gc.setStroke(Color.rgb(255, 255, 255, 0.08));
                gc.setLineWidth(1);
                for (double scanY = y + margin + scanlineOffset % 3; scanY < y + h - margin; scanY += 3) {
                        gc.strokeLine(x + margin, scanY, x + w - margin, scanY);
                }
                gc.restore();

                // 7. Center RGB neon strip with animated gradient
                double stripH = 8;
                double stripY = y + (h - stripH) / 2;
                double stripMargin = 12;

                // Strip outer glow
                gc.setEffect(new GaussianBlur(6));
                gc.setFill(new LinearGradient(
                                x + stripMargin, 0, x + w - stripMargin, 0, false, CycleMethod.NO_CYCLE,
                                new Stop(0, rgbColor1.deriveColor(0, 1, 1, 0.7 * pulse)),
                                new Stop(0.5, rgbColor2.deriveColor(0, 1, 1, 0.7 * pulse)),
                                new Stop(1, rgbColor3.deriveColor(0, 1, 1, 0.7 * pulse))));
                gc.fillRoundRect(x + stripMargin - 3, stripY - 3, w - stripMargin * 2 + 6, stripH + 6, 8, 8);
                gc.setEffect(null);

                // Strip body with RGB gradient
                gc.setFill(new LinearGradient(
                                x + stripMargin, 0, x + w - stripMargin, 0, false, CycleMethod.NO_CYCLE,
                                new Stop(0, rgbColor1),
                                new Stop(0.25, rgbColor2),
                                new Stop(0.5, rgbColor3),
                                new Stop(0.75, rgbColor1),
                                new Stop(1, rgbColor2)));
                gc.fillRoundRect(x + stripMargin, stripY, w - stripMargin * 2, stripH, 5, 5);

                // Strip inner highlight
                gc.setFill(new LinearGradient(
                                0, stripY, 0, stripY + stripH / 2, false, CycleMethod.NO_CYCLE,
                                new Stop(0, Color.rgb(255, 255, 255, 0.6)),
                                new Stop(1, Color.TRANSPARENT)));
                gc.fillRoundRect(x + stripMargin + 2, stripY + 1, w - stripMargin * 2 - 4, stripH / 2 - 1, 3, 3);

                // 8. Top specular highlight (glossy finish)
                gc.setFill(new LinearGradient(
                                0, y, 0, y + h / 2, false, CycleMethod.NO_CYCLE,
                                new Stop(0, Color.rgb(255, 255, 255, 0.5)),
                                new Stop(0.3, Color.rgb(255, 255, 255, 0.25)),
                                new Stop(0.6, Color.rgb(255, 255, 255, 0.08)),
                                new Stop(1, Color.TRANSPARENT)));
                gc.fillRoundRect(x + margin + 4, y + margin + 1, w - margin * 2 - 8, (h - margin * 2) / 2.2,
                                h, h);

                // 9. Corner accent lights
                double cornerSize = 6;

                // Left corner light
                gc.setFill(new RadialGradient(
                                0, 0, 0.5, 0.5, 0.5, true, CycleMethod.NO_CYCLE,
                                new Stop(0, rgbColor1.deriveColor(0, 1, 1, fastPulse)),
                                new Stop(0.5, rgbColor1.deriveColor(0, 1, 1, 0.3 * fastPulse)),
                                new Stop(1, Color.TRANSPARENT)));
                gc.fillOval(x + margin + 2, y + h / 2 - cornerSize / 2, cornerSize, cornerSize);

                // Right corner light
                gc.setFill(new RadialGradient(
                                0, 0, 0.5, 0.5, 0.5, true, CycleMethod.NO_CYCLE,
                                new Stop(0, rgbColor3.deriveColor(0, 1, 1, fastPulse)),
                                new Stop(0.5, rgbColor3.deriveColor(0, 1, 1, 0.3 * fastPulse)),
                                new Stop(1, Color.TRANSPARENT)));
                gc.fillOval(x + w - margin - cornerSize - 2, y + h / 2 - cornerSize / 2, cornerSize, cornerSize);

                // 10. Edge highlights with RGB gradient
                double capWidth = 10;

                // Left cap glow
                gc.setFill(new LinearGradient(
                                x, 0, x + capWidth, 0, false, CycleMethod.NO_CYCLE,
                                new Stop(0, rgbColor1.deriveColor(0, 1, 1, 0.6 * pulse)),
                                new Stop(0.5, rgbColor2.deriveColor(0, 1, 1, 0.3 * pulse)),
                                new Stop(1, Color.TRANSPARENT)));
                gc.fillRoundRect(x + margin, y + margin, capWidth, h - margin * 2, h, h);

                // Right cap glow
                gc.setFill(new LinearGradient(
                                x + w - capWidth, 0, x + w, 0, false, CycleMethod.NO_CYCLE,
                                new Stop(0, Color.TRANSPARENT),
                                new Stop(0.5, rgbColor2.deriveColor(0, 1, 1, 0.3 * pulse)),
                                new Stop(1, rgbColor3.deriveColor(0, 1, 1, 0.6 * pulse))));
                gc.fillRoundRect(x + w - margin - capWidth, y + margin, capWidth, h - margin * 2, h, h);

                gc.restore();
        }
}
