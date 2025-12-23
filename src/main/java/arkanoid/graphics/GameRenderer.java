package arkanoid.graphics;

import javafx.scene.canvas.GraphicsContext;

import java.util.HashMap;
import java.util.Map;

import arkanoid.entity.Ball;
import arkanoid.entity.Block;
import arkanoid.entity.FloatingText;
import arkanoid.entity.Paddle;
import arkanoid.entity.PowerUp;
import arkanoid.graphics.painters.BallPainter;
import arkanoid.graphics.painters.BlockPainter;
import arkanoid.graphics.painters.FloatingTextPainter;
import arkanoid.graphics.painters.PaddlePainter;
import arkanoid.graphics.painters.PowerUpPainter;

/**
 * Handles rendering of all game objects.
 * Uses the Painter pattern to delegate drawing of specific types.
 */
public class GameRenderer {

    private final GraphicsContext gc;
    private final Map<Class<?>, Painter<?>> painters = new HashMap<>();

    /**
     * Create a game renderer.
     *
     * @param gc graphics context to draw on
     */
    public GameRenderer(GraphicsContext gc) {
        this.gc = gc;
        registerPainters();
    }

    private void registerPainters() {
        painters.put(Ball.class, new BallPainter());
        painters.put(Block.class, new BlockPainter());
        painters.put(Paddle.class, new PaddlePainter());
        painters.put(PowerUp.class, new PowerUpPainter());
        painters.put(FloatingText.class, new FloatingTextPainter());

    }

    /**
     * Render a single object.
     *
     * @param object object to render
     */
    public void render(Object object) {
        Painter<?> painter = painters.get(object.getClass());

        // If no exact match, try to find a painter for a superclass/interface
        if (painter == null) {
            for (Map.Entry<Class<?>, Painter<?>> entry : painters.entrySet()) {
                if (entry.getKey().isInstance(object)) {
                    painter = entry.getValue();
                    break;
                }
            }
        }

        if (painter != null) {
            paintSafely(painter, object);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> void paintSafely(Painter<T> painter, Object object) {
        // We trust that the map contains correct pairings of Class<T> and Painter<T>
        // The cast (T) object is checked at runtime if T is reified, but here T is
        // inferred.
        // However, since we looked up the painter using the object's class (or
        // superclass),
        // we can be reasonably sure it's safe.
        painter.paint(gc, (T) object);
    }

    /**
     * Render a collection of objects.
     *
     * @param objects iterable of objects to render
     */
    public void renderAll(Iterable<?> objects) {
        for (Object obj : objects) {
            render(obj);
        }
    }

}
