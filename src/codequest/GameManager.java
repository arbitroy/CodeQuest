package codequest;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import codequest.levels.*;

/**
 * GameManager - Manages game levels and transitions
 */
public class GameManager {
    
    private Stage primaryStage;
    private int currentLevel = 0;
    private Level[] levels;
    private Scene startScene;
    
    public GameManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
        initializeLevels();
        createStartScene();
    }
    
    private void initializeLevels() {
        levels = new Level[5];
        levels[0] = new CommandsLevel(this);       // Level 1: Commands
        levels[1] = new VariablesLevel(this);      // Level 2: Variables
        levels[2] = new ConditionalsLevel(this);   // Level 3: Conditionals
        levels[3] = new LoopsLevel(this);          // Level 4: Loops
        levels[4] = new FreeRoamLevel(this);       // Level 5: Free Roam
    }
    
    private void createStartScene() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #2c3e50;");
        
        VBox centerBox = new VBox(20);
        centerBox.setAlignment(Pos.CENTER);
        
        Text title = new Text("CodeQuest");
        title.setFont(Font.font("Arial", 48));
        title.setStyle("-fx-fill: white;");
        
        Text subtitle = new Text("Learn to code by playing!");
        subtitle.setFont(Font.font("Arial", 24));
        subtitle.setStyle("-fx-fill: #ecf0f1;");
        
        Button startButton = new Button("Start Game");
        startButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-size: 18px;");
        startButton.setPrefSize(200, 50);
        startButton.setOnAction(e -> startGame());
        
        centerBox.getChildren().addAll(title, subtitle, startButton);
        root.setCenter(centerBox);
        
        startScene = new Scene(root, 800, 600);
    }
    
    public Scene getStartScene() {
        return startScene;
    }
    
    public void startGame() {
        currentLevel = 0;
        loadCurrentLevel();
    }
    
    public void loadCurrentLevel() {
        if (currentLevel < levels.length) {
            Scene levelScene = levels[currentLevel].createLevelScene();
            primaryStage.setScene(levelScene);
        } else {
            // Game completed
            showGameCompletedScene();
        }
    }
    
    public void nextLevel() {
        currentLevel++;
        loadCurrentLevel();
    }
    
    private void showGameCompletedScene() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #2c3e50;");
        
        VBox centerBox = new VBox(20);
        centerBox.setAlignment(Pos.CENTER);
        
        Text congratsText = new Text("Congratulations!");
        congratsText.setFont(Font.font("Arial", 48));
        congratsText.setStyle("-fx-fill: white;");
        
        Text completionText = new Text("You've completed all the levels and learned the basics of coding!");
        completionText.setFont(Font.font("Arial", 18));
        completionText.setStyle("-fx-fill: #ecf0f1;");
        
        Button restartButton = new Button("Play Again");
        restartButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-size: 18px;");
        restartButton.setPrefSize(200, 50);
        restartButton.setOnAction(e -> startGame());
        
        centerBox.getChildren().addAll(congratsText, completionText, restartButton);
        root.setCenter(centerBox);
        
        Scene completionScene = new Scene(root, 800, 600);
        primaryStage.setScene(completionScene);
    }
}