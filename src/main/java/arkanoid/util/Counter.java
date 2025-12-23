package arkanoid.util;

/**
 * Simple mutable integer counter.
 * Game runs on single JavaFX Application Thread, so no synchronization needed.
 */
public class Counter {

    private int value;

    /**
     * Create a counter with initial value 0.
     */
    public Counter() {
        this(0);
    }

    /**
     * Create a counter with a specific initial value.
     *
     * @param initialValue starting value
     */
    public Counter(int initialValue) {
        this.value = initialValue;
    }

    /**
     * Increase the counter by a given amount.
     *
     * @param amount amount to add
     */
    public void increase(int amount) {
        this.value += amount;
    }

    /**
     * Decrease the counter by a given amount.
     *
     * @param amount amount to subtract
     */
    public void decrease(int amount) {
        this.value -= amount;
    }

    /**
     * Get the current value.
     *
     * @return current value
     */
    public int getValue() {
        return value;
    }
}
