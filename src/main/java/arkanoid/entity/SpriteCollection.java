package arkanoid.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * A collection of sprites that can be updated and drawn together.
 */
public class SpriteCollection {

    private final List<Sprite> sprites = new ArrayList<>();

    /**
     * Add a sprite to the collection.
     *
     * @param s sprite to add
     */
    public void addSprite(Sprite s) {
        this.sprites.add(s);
    }

    /**
     * Remove a sprite from the collection.
     *
     * @param s sprite to remove
     */
    public void removeSprite(Sprite s) {
        this.sprites.remove(s);
    }

    /**
     * Notify all sprites that time has passed.
     *
     * @param dt time delta
     */
    public void updateAll(double dt) {
        // Work on a copy to avoid concurrent modification if sprites add/remove during
        // iteration.
        List<Sprite> snapshot = new ArrayList<>(sprites);
        for (Sprite s : snapshot) {
            s.update(dt);
        }
    }

    /**
     * Get all sprites.
     *
     * @return iterable of sprites
     */
    public Iterable<Sprite> getSprites() {
        return java.util.Collections.unmodifiableList(sprites);
    }
}
