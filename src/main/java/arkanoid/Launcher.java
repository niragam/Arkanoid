package arkanoid;

/**
 * Launcher class to workaround JavaFX module issues in some environments.
 */
public class Launcher {

    /**
     * Main entry point.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        try {
            App.main(args);
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
