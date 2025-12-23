package arkanoid.core;

import javafx.scene.canvas.GraphicsContext;

import java.util.List;

import arkanoid.animation.AnimationRunner;
import arkanoid.animation.LevelAnimation;
import arkanoid.level.LevelInformation;
import arkanoid.screen.EndScreen;
import arkanoid.screen.MainMenu;
import arkanoid.util.Counter;
import arkanoid.util.InputState;

/**
 * High-level controller that runs a sequence of levels and tracks lives/score.
 */
public class GameFlow {

    private final AnimationRunner runner;
    private final GraphicsContext gc;
    private final InputState input;
    private Counter score;
    private Counter lives;

    /**
     * Construct a GameFlow.
     *
     * @param runner animation runner
     * @param gc     graphics context
     * @param input  input state
     */
    public GameFlow(AnimationRunner runner, GraphicsContext gc, InputState input) {
        this.runner = runner;
        this.gc = gc;
        this.input = input;
    }

    /**
     * Run the provided levels in order.
     *
     * @param levels list of level descriptors
     */
    public void runLevels(List<LevelInformation> levels) {
        // Reset score and lives for new game
        score = new Counter();
        lives = new Counter(GameConfig.STARTING_LIVES);

        // Show main menu first, then start the game sequence
        startGameSequence(levels, 0, false);
    }

    /**
     * Start the game sequence from a given level index.
     * This method schedules animations without blocking the JavaFX thread.
     */
    private void startGameSequence(List<LevelInformation> levels, int levelIndex, boolean skipMenu) {
        if (!skipMenu) {
            // Show main menu first
            MainMenu menu = new MainMenu(input);
            menu.setOnComplete(() -> {
                if (menu.isExitRequested()) {
                    javafx.application.Platform.exit();
                } else {
                    runLevelSequence(levels, 0);
                }
            });
            runner.run(menu);
        } else {
            runLevelSequence(levels, levelIndex);
        }
    }

    /**
     * Run the level sequence starting from the given index.
     */
    private void runLevelSequence(List<LevelInformation> levels, int levelIndex) {
        if (levelIndex >= levels.size() || lives.getValue() <= 0) {
            // Game completed or game over - show end screen then restart
            EndScreen endScreen = new EndScreen(input, score, lives);
            endScreen.setOnComplete(() -> {
                if (endScreen.isExitRequested()) {
                    javafx.application.Platform.exit();
                } else {
                    // Reset and show menu again
                    score = new Counter();
                    lives = new Counter(GameConfig.STARTING_LIVES);
                    startGameSequence(levels, 0, false);
                }
            });
            runner.run(endScreen);
            return;
        }

        LevelInformation levelInfo = levels.get(levelIndex);
        Game level = levelInfo.createGame(gc, input, score, lives);
        level.initialize();

        LevelAnimation levelAnim = new LevelAnimation(level, input, lives);
        levelAnim.setOnComplete(() -> {
            if (levelAnim.isQuitRequested()) {
                // Quit to menu
                score = new Counter();
                lives = new Counter(GameConfig.STARTING_LIVES);
                startGameSequence(levels, 0, false);
            } else if (lives.getValue() <= 0) {
                // Game over
                runLevelSequence(levels, levels.size());
            } else {
                // Next level
                runLevelSequence(levels, levelIndex + 1);
            }
        });
        runner.run(levelAnim);
    }
}
