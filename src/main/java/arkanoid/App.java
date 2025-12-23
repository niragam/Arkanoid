package arkanoid;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import arkanoid.animation.AnimationRunner;
import arkanoid.core.GameConfig;
import arkanoid.core.GameFlow;
import arkanoid.level.Levels;
import arkanoid.util.InputState;

/**
 * JavaFX application entry point.
 */
public class App extends Application {

    /**
     * Start the JavaFX application.
     *
     * @param primaryStage the primary stage for this application
     */
    @Override
    public void start(Stage primaryStage) {
        int width = GameConfig.SCREEN_WIDTH;
        int height = GameConfig.SCREEN_HEIGHT;

        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root, width, height);

        primaryStage.setTitle("Arkanoid Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        InputState input = new InputState();
        scene.setOnKeyPressed(e -> input.keyPressed(e.getCode()));
        scene.setOnKeyReleased(e -> input.keyReleased(e.getCode()));

        AnimationRunner runner = new AnimationRunner(gc);
        GameFlow flow = new GameFlow(runner, gc, input);

        flow.runLevels(Levels.all());
    }

    /**
     * The application's entry point.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
