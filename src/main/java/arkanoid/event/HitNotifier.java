package arkanoid.event;

/**
 * Interface for objects that can notify listeners of hit events.
 */
public interface HitNotifier {
    /**
     * Add a listener to hit events.
     *
     * @param hl listener to add
     */
    void addHitListener(HitListener hl);

    /**
     * Remove a listener from hit events.
     *
     * @param hl listener to remove
     */
    void removeHitListener(HitListener hl);
}
