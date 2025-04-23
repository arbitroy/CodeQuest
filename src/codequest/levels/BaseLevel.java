package codequest.levels;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import codequest.GameManager;
import codequest.GameSprite;

/**
 * BaseLevel - Common functionality for all level types
 */
public abstract class BaseLevel implements Level {
    
    protected GameManager gameManager;
    protected GameSprite sprite;
    protected Pane gamePane;
    protected TextArea outputArea;
    protected TextArea codeArea;
    protected boolean levelCompleted = false;
    
    public BaseLevel(GameManager gameManager) {
        this.gameManager = gameManager;
    }
    
    @Override
    public Scene createLevelScene() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        root.setStyle("-fx-background-color: #2c3e50;");
        
        // Top - Level title and instructions
        VBox topBox = createTopSection();
        root.setTop(topBox);
        
        // Center - Game area
        gamePane = new Pane();
        gamePane.setPrefSize(600, 400);
        gamePane.setStyle("-fx-background-color: #ecf0f1;");
        root.setCenter(gamePane);
        
        // Initialize the sprite
        sprite = new GameSprite(gamePane);
        
        // Bottom - Code input and output
        VBox bottomBox = createBottomSection();
        root.setBottom(bottomBox);
        
        return new Scene(root, 800, 700);
    }
    
    protected VBox createTopSection() {
        VBox topBox = new VBox(10);
        topBox.setPadding(new Insets(10));
        topBox.setAlignment(Pos.CENTER_LEFT);
        
        Text levelTitle = new Text("Level " + getLevelNumber() + ": " + getLevelName());
        levelTitle.setFont(Font.font("Arial", 24));
        levelTitle.setStyle("-fx-fill: white;");
        
        Text instructions = new Text(getLevelInstructions());
        instructions.setFont(Font.font("Arial", 14));
        instructions.setStyle("-fx-fill: #ecf0f1;");
        
        topBox.getChildren().addAll(levelTitle, instructions);
        return topBox;
    }
    
    protected VBox createBottomSection() {
        VBox bottomBox = new VBox(10);
        bottomBox.setPadding(new Insets(10));
        
        // Code area
        Label codeLabel = new Label("Your Code:");
        codeLabel.setStyle("-fx-text-fill: white;");
        
        codeArea = new TextArea();
        codeArea.setPrefHeight(100);
        codeArea.setPromptText("Type your code here...");
        codeArea.setText(getStarterCode());
        
        // Buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button runButton = new Button("Run Code");
        runButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;");
        runButton.setOnAction(e -> processCommand(codeArea.getText()));
        
        Button resetButton = new Button("Reset");
        resetButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
        resetButton.setOnAction(e -> resetLevel());
        
        Button helpButton = new Button("Help");
        helpButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
        helpButton.setOnAction(e -> showHelp());
        
        buttonBox.getChildren().addAll(runButton, resetButton, helpButton);
        
        // Output area
        Label outputLabel = new Label("Output:");
        outputLabel.setStyle("-fx-text-fill: white;");
        
        outputArea = new TextArea();
        outputArea.setPrefHeight(100);
        outputArea.setEditable(false);
        outputArea.setStyle("-fx-control-inner-background: #34495e; -fx-text-fill: #ecf0f1;");
        
        bottomBox.getChildren().addAll(codeLabel, codeArea, buttonBox, outputLabel, outputArea);
        return bottomBox;
    }
    
    protected void resetLevel() {
        // Reset game state
        sprite = new GameSprite(gamePane);
        levelCompleted = false;
        
        // Clear output and reset code
        outputArea.clear();
        codeArea.setText(getStarterCode());
        
        // Add level-specific reset logic
        onReset();
        
        // Show reset message
        appendToOutput("Level reset. Let's try again!");
    }
    
    protected void completeLevel() {
        if (!levelCompleted) {
            levelCompleted = true;
            
            appendToOutput("\nCongratulations! Level completed!");
            
            Button nextLevelButton = new Button("Next Level");
            nextLevelButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;");
            nextLevelButton.setOnAction(e -> gameManager.nextLevel());
            
            // Add the button to the game pane
            HBox buttonBox = new HBox(nextLevelButton);
            buttonBox.setAlignment(Pos.CENTER);
            buttonBox.setPadding(new Insets(10));
            buttonBox.setLayoutX(250);
            buttonBox.setLayoutY(200);
            buttonBox.setStyle("-fx-background-color: rgba(44, 62, 80, 0.8); -fx-padding: 20px;");
            
            gamePane.getChildren().add(buttonBox);
        }
    }
    
    protected void appendToOutput(String text) {
        outputArea.appendText(text + "\n");
    }
    
    protected void showHelp() {
        appendToOutput("\n--- HELP ---");
        appendToOutput(getHelpText());
    }
    
    @Override
    public boolean isCompleted() {
        return levelCompleted;
    }
    
    // Methods to be implemented by specific level classes
    protected abstract String getLevelName();
    protected abstract int getLevelNumber();
    protected abstract String getLevelInstructions();
    protected abstract String getStarterCode();
    protected abstract String getHelpText();
    protected abstract void onReset();
}