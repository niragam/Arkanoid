package arkanoid.animation;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;

/**
 * Runs a given Animation using JavaFX AnimationTimer.
 *
 * The JavaFX Application is responsible for creating the canvas and
 * GraphicsContext and passing them into this runner.
 */
public class AnimationRunner {

    private static final double MAX_DT = 0.05; // Cap dt at 50ms to prevent physics jumps

    private final GraphicsContext gc;
    private AnimationTimer currentTimer;

    /**
     * Create an animation runner.
     *
     * @param gc graphics context to draw on
     */
    public AnimationRunner(GraphicsContext gc) {
        this.gc = gc;
    }

    /**
     * Run the given animation until it indicates it should stop.
     * When the animation stops, its onComplete callback (if any) will be invoked.
     *
     * @param animation animation to run
     */
    public void run(Animation animation) {
        if (animation == null) {
            return;
        }

        // Stop any currently running animation
        if (currentTimer != null) {
            currentTimer.stop();
            currentTimer = null;
        }

        final long[] lastTime = { 0 }; // 0 indicates first frame

        currentTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double dt;
                if (lastTime[0] == 0) {
                    // First frame: use a small fixed dt to prevent large jumps
                    dt = 1.0 / 60.0;
                } else {
                    dt = (now - lastTime[0]) / 1_000_000_000.0;
                    // Cap dt to prevent physics issues on lag spikes
                    if (dt > MAX_DT) {
                        dt = MAX_DT;
                    }
                }
                lastTime[0] = now;

                animation.doOneFrame(gc, dt);
                if (animation.shouldStop()) {
                    stop();
                    currentTimer = null;
                    // Invoke the completion callback if set
                    Runnable callback = animation.getOnComplete();
                    if (callback != null) {
                        callback.run();
                    }
                }
            }
        };
        currentTimer.start();
    }
}
