package codequest.levels;

import codequest.GameManager;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * CommandsLevel - Level 1: Learning basic commands
 * Updated to use proper layering
 */
public class CommandsLevel extends BaseLevel {
    
    private Rectangle goal;
    private boolean movedLeft = false;
    private boolean jumped = false;
    
    public CommandsLevel(GameManager gameManager) {
        super(gameManager);
    }
    
    @Override
    protected String getLevelName() {
        return "Learn Commands";
    }
    
    @Override
    protected int getLevelNumber() {
        return 1;
    }
    
    @Override
    protected String getLevelInstructions() {
        return "Welcome to CodeQuest! In this level, you'll learn basic commands.\n" +
               "Commands are instructions that tell the computer what to do.\n" +
               "Your goal: Use the moveLeft() and jump() commands to reach the green area.";
    }
    
    @Override
    protected String getStarterCode() {
        return "// Type your commands here\n" +
               "// Try using moveLeft() and jump()\n" +
               "\n" +
               "\n";
    }
    
    @Override
    protected String getHelpText() {
        return "Commands are instructions that tell the computer what to do.\n" +
               "In this level, you can use these commands:\n" +
               "- moveLeft() - Moves the character to the left\n" +
               "- jump() - Makes the character jump\n" +
               "Make sure to end each command with parentheses () and a semicolon ;";
    }
    
    @Override
    public Scene createLevelScene() {
        Scene scene = super.createLevelScene();
        
        // Add goal area (green rectangle) to the BACKGROUND layer
        goal = new Rectangle(100, 50);
        goal.setFill(Color.GREEN);
        goal.setX(100);
        goal.setY(200);
        goal.setOpacity(0.5);
        
        // Add to BACKGROUND layer instead of gamePane
        backgroundLayer.getChildren().add(goal);
        
        appendToOutput("Welcome to Level 1: Commands!\nUse moveLeft() and jump() to reach the green area.");
        
        return scene;
    }
    
    @Override
    public void processCommand(String command) {
        // Split the command into lines
        String[] lines = command.split("\n");
        
        appendToOutput("\n--- Running your code ---");
        
        for (String line : lines) {
            // Remove comments and trim whitespace
            line = line.replaceAll("//.*", "").trim();
            
            if (line.isEmpty()) {
                continue;
            }
            
            // Check for basic commands
            if (line.matches("moveLeft\\(\\);.*")) {
                appendToOutput("Executing: moveLeft()");
                sprite.moveLeft();
                movedLeft = true;
            } else if (line.matches("jump\\(\\);.*")) {
                appendToOutput("Executing: jump()");
                sprite.jump();
                jumped = true;
            } else {
                appendToOutput("Unrecognized command: " + line);
            }
        }
        
        // Check if the level is completed
        checkLevelCompletion();
    }
    
    private void checkLevelCompletion() {
        // Check if the sprite is in the goal area
        if (sprite.getXPos() <= 100 && movedLeft && jumped) {
            completeLevel();
        }
    }
    
    @Override
    protected void onReset() {
        // Clear the game-specific state
        movedLeft = false;
        jumped = false;
    }
}