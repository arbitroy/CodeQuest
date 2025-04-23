package codequest;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * CodeQuest - A game to teach programming concepts
 */
public class Main extends Application {
    
    private GameManager gameManager;
    
    @Override
    public void start(Stage primaryStage) {
        // Set up the primary stage
        primaryStage.setTitle("CodeQuest - Learn Coding Through Play");
        primaryStage.setResizable(false);
        
        // Initialize the game manager
        gameManager = new GameManager(primaryStage);
        
        // Set up the initial scene
        Scene startScene = gameManager.getStartScene();
        primaryStage.setScene(startScene);
        primaryStage.show();
    }
    
    /**
     * Main method to launch the application
     */
    public static void main(String[] args) {
        launch(args);
    }
}