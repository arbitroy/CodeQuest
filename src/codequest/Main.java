package codequest;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * CodeQuest - A game to teach programming concepts
 * Redesigned with improved UI and user experience
 */
public class Main extends Application {

    private GameManager gameManager;

    @Override
    public void start(Stage primaryStage) {
        try {
            // Set up application window
            setupStage(primaryStage);

            // Initialize the game manager
            gameManager = new GameManager(primaryStage);

            // Set up the initial scene
            Scene startScene = gameManager.getStartScene();
            primaryStage.setScene(startScene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Configure the primary stage properties
     */
    private void setupStage(Stage primaryStage) {
        // Set window title and properties
        primaryStage.setTitle("CodeQuest - Learn Coding Through Play");
        primaryStage.setResizable(true);
        primaryStage.setMinWidth(1024);
        primaryStage.setMinHeight(768);

        // Set application icon
        try {
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/codequest/assets/app-icon.png")));
        } catch (Exception e) {
            System.out.println("Could not load application icon: " + e.getMessage());
            // Continue without icon if it can't be loaded
        }
    }

    /**
     * Main method to launch the application
     */
    public static void main(String[] args) {
        launch(args);
    }
}