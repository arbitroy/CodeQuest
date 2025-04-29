package codequest.levels;

import codequest.GameManager;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

/**
 * CommandsLevel - Level 1: Learning basic commands
 * With logically correct goal positioning
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
               "// Try using moveLeft() and jump()\n";
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
        
        // Clear any existing background elements first
        backgroundLayer.getChildren().clear();
        
        // Add a dark blue background
        Rectangle background = new Rectangle(0, 0, gamePane.getWidth(), gamePane.getHeight());
        background.setFill(Color.web("#2c3e50")); // Dark blue background
        backgroundLayer.getChildren().add(background);
        
        // Goal area positioned where moveLeft() will actually take the sprite
        // The debug output shows sprite goes to X:0.0, Y:200.0 after moveLeft()
        goal = new Rectangle(150, 100);
        goal.setFill(Color.LIME); // Bright green
        goal.setStroke(Color.WHITE); // White border
        goal.setStrokeWidth(3);
        goal.setX(0); // Position where sprite will be after moveLeft()
        goal.setY(150); // Position where it will overlap with the sprite
        goal.setOpacity(0.7);
        
        // Add the goal
        backgroundLayer.getChildren().add(goal);
        
        // Add a label for clarity
        Text goalLabel = new Text("GOAL");
        goalLabel.setFill(Color.WHITE);
        goalLabel.setX(goal.getX() + 50);
        goalLabel.setY(goal.getY() + 50);
        goalLabel.setStyle("-fx-font-weight: bold;");
        
        // Add label to background
        backgroundLayer.getChildren().add(goalLabel);
        
        // Debug message
        appendToOutput("DEBUG: Goal placed at X:" + goal.getX() + ", Y:" + goal.getY() + 
                      " with width:" + goal.getWidth() + ", height:" + goal.getHeight());
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
                
                // Debug position
                appendToOutput("DEBUG: Sprite position after moveLeft: X:" + sprite.getXPos() + ", Y:" + sprite.getYPos());
            } else if (line.matches("jump\\(\\);.*")) {
                appendToOutput("Executing: jump()");
                sprite.jump();
                jumped = true;
                
                // Debug position
                appendToOutput("DEBUG: Sprite position after jump: X:" + sprite.getXPos() + ", Y:" + sprite.getYPos());
            } else {
                appendToOutput("Unrecognized command: " + line);
            }
        }
        
        // Debug collision check
        boolean inGoalX = sprite.getXPos() >= goal.getX() && sprite.getXPos() <= goal.getX() + goal.getWidth();
        boolean inGoalY = sprite.getYPos() >= goal.getY() && sprite.getYPos() <= goal.getY() + goal.getHeight();
        appendToOutput("DEBUG: In goal area? X:" + inGoalX + ", Y:" + inGoalY + 
                      " (Needed: moveLeft=" + movedLeft + ", jumped=" + jumped + ")");
        
        // Check if the level is completed
        checkLevelCompletion();
    }
    
    private void checkLevelCompletion() {
        // Check if the sprite is in the goal area and the required commands were used
        if (sprite.getXPos() >= goal.getX() && 
            sprite.getXPos() <= goal.getX() + goal.getWidth() &&
            sprite.getYPos() >= goal.getY() && 
            sprite.getYPos() <= goal.getY() + goal.getHeight() &&
            movedLeft && jumped) {
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