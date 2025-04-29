package codequest.levels;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import codequest.GameManager;
import codequest.GameSprite;

/**
 * BaseLevel - Common functionality for all level types
 * Fixed layer management while keeping original visual style
 */
public abstract class BaseLevel implements Level {
    
    protected GameManager gameManager;
    protected GameSprite sprite;
    protected Pane gamePane;
    protected Pane backgroundLayer; // For background elements like goal areas
    protected Pane spriteLayer;     // For the player character 
    protected Pane foregroundLayer; // For UI elements on top
    protected TextArea outputArea;
    protected TextArea codeArea;
    protected boolean levelCompleted = false;
    
    // Standard dimensions for elementss
    protected static final int GAME_WIDTH = 800;
    protected static final int GAME_HEIGHT = 400;
    
    public BaseLevel(GameManager gameManager) {
        this.gameManager = gameManager;
    }
    
    @Override
    public Scene createLevelScene() {
        // Create the main layout
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        root.setStyle("-fx-background-color: #1e1e2e;"); // Dark background to match screenshot
        
        // Top - Level title and instructions
        VBox topBox = createTopSection();
        root.setTop(topBox);
        
        // Center - Game area with layered panes
        setupGameLayers();
        root.setCenter(gamePane);
        
        // Bottom - Code input and output
        VBox bottomBox = createBottomSection();
        root.setBottom(bottomBox);
        
        // Initialize the sprite on the sprite layer - positioned to match screenshot
        sprite = new GameSprite(spriteLayer);
        
        return new Scene(root, 800, 700);
    }
    
    /**
     * Setup the game pane with proper layering
     */
    private void setupGameLayers() {
        // Main container pane - will contain all layers
        gamePane = new Pane();
        gamePane.setPrefSize(GAME_WIDTH, GAME_HEIGHT);
        gamePane.setMaxHeight(GAME_HEIGHT);
        gamePane.setStyle("-fx-background-color: #2c3e50;"); // Dark blue background
        
        // Background layer - for terrain, goal areas, etc.
        backgroundLayer = new Pane();
        backgroundLayer.setPrefSize(GAME_WIDTH, GAME_HEIGHT);
        
        // Sprite layer - for the player character
        spriteLayer = new Pane();
        spriteLayer.setPrefSize(GAME_WIDTH, GAME_HEIGHT);
        
        // Foreground layer - for UI elements, text, etc.
        foregroundLayer = new Pane();
        foregroundLayer.setPrefSize(GAME_WIDTH, GAME_HEIGHT);
        
        // Add layers in order (bottom to top)
        gamePane.getChildren().addAll(backgroundLayer, spriteLayer, foregroundLayer);
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
        instructions.setWrappingWidth(780);
        
        topBox.getChildren().addAll(levelTitle, instructions);
        return topBox;
    }
    
    protected VBox createBottomSection() {
        VBox bottomBox = new VBox(10);
        bottomBox.setPadding(new Insets(10));
        
        // Code area - styling to match screenshot
        Label codeLabel = new Label("Your Code:");
        codeLabel.setStyle("-fx-text-fill: white;");
        
        codeArea = new TextArea();
        codeArea.setPrefHeight(100);
        codeArea.setPromptText("Type your code here...");
        codeArea.setText(getStarterCode());
        codeArea.setStyle("-fx-control-inner-background: #2d3436; -fx-text-fill: #dfe6e9;");
        
        // Buttons - styling to match screenshot
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button runButton = new Button("Run Code");
        runButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-size: 14px;");
        runButton.setPrefSize(120, 40);
        runButton.setOnAction(e -> processCommand(codeArea.getText()));
        
        Button resetButton = new Button("Reset Level");
        resetButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 14px;");
        resetButton.setPrefSize(120, 40);
        resetButton.setOnAction(e -> resetLevel());
        
        Button helpButton = new Button("Help");
        helpButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px;");
        helpButton.setPrefSize(120, 40);
        helpButton.setOnAction(e -> showHelp());
        
        buttonBox.getChildren().addAll(runButton, resetButton, helpButton);
        
        // Output area - styling to match screenshot
        Label outputLabel = new Label("Output:");
        outputLabel.setStyle("-fx-text-fill: white;");
        
        outputArea = new TextArea();
        outputArea.setPrefHeight(100);
        outputArea.setEditable(false);
        outputArea.setStyle("-fx-control-inner-background: #2d3436; -fx-text-fill: #8fbcbb;");
        
        bottomBox.getChildren().addAll(codeLabel, codeArea, buttonBox, outputLabel, outputArea);
        return bottomBox;
    }
    
    protected void resetLevel() {
        // Clear layers
        spriteLayer.getChildren().clear();
        
        // Reset game state
        sprite = new GameSprite(spriteLayer);
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
            
            // Create a next level button
            Button nextLevelButton = new Button("Next Level");
            nextLevelButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;");
            nextLevelButton.setOnAction(e -> gameManager.nextLevel());
            
            // Add the button to the foreground layer
            HBox buttonBox = new HBox(nextLevelButton);
            buttonBox.setAlignment(Pos.CENTER);
            buttonBox.setPadding(new Insets(10));
            buttonBox.setLayoutX(GAME_WIDTH/2 - 75);
            buttonBox.setLayoutY(GAME_HEIGHT/2 - 25);
            buttonBox.setStyle("-fx-background-color: rgba(44, 62, 80, 0.8); -fx-padding: 20px;");
            
            foregroundLayer.getChildren().add(buttonBox);
        }
    }
    
    protected void appendToOutput(String text) {
        outputArea.appendText(text + "\n");
        // Scroll to the bottom for better visibility
        outputArea.positionCaret(outputArea.getText().length());
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