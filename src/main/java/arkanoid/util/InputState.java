package arkanoid.util;

import javafx.scene.input.KeyCode;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Shared input state: tracks which keys are currently pressed.
 * Thread-safe for access from JavaFX Application Thread and AnimationTimer.
 */
public class InputState {

    private final Set<KeyCode> pressed = Collections.newSetFromMap(new ConcurrentHashMap<>());

    /**
     * Mark a key as pressed.
     *
     * @param code key code
     */
    public void keyPressed(KeyCode code) {
        pressed.add(code);
    }

    /**
     * Mark a key as released.
     *
     * @param code key code
     */
    public void keyReleased(KeyCode code) {
        pressed.remove(code);
    }

    /**
     * Check if a key is currently pressed.
     *
     * @param code key code
     * @return true if pressed
     */
    public boolean isPressed(KeyCode code) {
        return pressed.contains(code);
    }
}
