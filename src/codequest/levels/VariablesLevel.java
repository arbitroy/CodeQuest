package codequest.levels;

import codequest.GameManager;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * VariablesLevel - Level 2: Learning variables
 * Fixed with visible goal area and proper variable display
 */
public class VariablesLevel extends BaseLevel {
    
    private Rectangle goal;
    private Map<String, Object> variables = new HashMap<>();
    private boolean usedVariable = false;
    private Label speedDisplay;
    
    public VariablesLevel(GameManager gameManager) {
        super(gameManager);
    }
    
    @Override
    protected String getLevelName() {
        return "Learn Variables";
    }
    
    @Override
    protected int getLevelNumber() {
        return 2;
    }
    
    @Override
    protected String getLevelInstructions() {
        return "Great job with commands! Now let's learn about variables.\n" +
               "Variables are containers that store values.\n" +
               "Your goal: Create a speed variable and use it to move your character to the green area.";
    }
    
    @Override
    protected String getStarterCode() {
        return "// Create a variable called 'speed' and set it to a number\n" +
               "speed = 10;\n" +
               "\n" +
               "// Then use the setSpeed command with your variable\n" +
               "setSpeed(speed);\n" +
               "\n" +
               "// Finally, move to the goal\n" +
               "moveRight();\n";
    }
    
    @Override
    protected String getHelpText() {
        return "Variables store information that your program can use later.\n" +
               "To create a variable:\n" +
               "1. Choose a name (like 'speed')\n" +
               "2. Use the equals sign (=) to assign a value\n" +
               "3. End with a semicolon (;)\n\n" +
               "Example: speed = 10;\n\n" +
               "Then use your variable with commands:\n" +
               "setSpeed(speed); - Sets the character's speed to your variable's value\n" +
               "moveRight(); - Moves the character to the right";
    }
    
    @Override
    public Scene createLevelScene() {
        Scene scene = super.createLevelScene();
        
        // Add goal area - using a simple rectangle without changing your existing UI
        goal = new Rectangle(150, 100);
        goal.setFill(Color.GREEN);
        goal.setOpacity(0.7);
        
        // Position on right side of screen but within visible area
        // X:600 should be within the visible area
        goal.setX(600);
        goal.setY(250);
        
        // Make sure it's added to the background layer
        backgroundLayer.getChildren().add(goal);
        
        // Create a variable display box - keep it simple to match your UI
        VBox variableBox = new VBox(10);
        variableBox.setLayoutX(75);
        variableBox.setLayoutY(75);
        variableBox.setPrefWidth(150);
        variableBox.setPrefHeight(80);
        variableBox.setStyle("-fx-background-color: #333333; -fx-padding: 10;");
        
        Text varTitle = new Text("VARIABLES");
        varTitle.setFill(Color.WHITE);
        varTitle.setFont(Font.font("Monospace", 14));
        
        speedDisplay = new Label("Speed: " + sprite.getSpeed());
        speedDisplay.setTextFill(Color.WHITE);
        
        variableBox.getChildren().addAll(varTitle, speedDisplay);
        foregroundLayer.getChildren().add(variableBox);
        
        appendToOutput("DEBUG: Goal placed at X:" + goal.getX() + ", Y:" + goal.getY() + 
                       " with width:" + goal.getWidth() + ", height:" + goal.getHeight());
        appendToOutput("Welcome to Level 2: Variables!\nCreate a speed variable and use it to reach the green area.");
        
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
            
            // Check for variable assignment: variableName = value;
            Pattern varPattern = Pattern.compile("(\\w+)\\s*=\\s*(\\d+);");
            Matcher varMatcher = varPattern.matcher(line);
            
            if (varMatcher.find()) {
                String varName = varMatcher.group(1);
                int varValue = Integer.parseInt(varMatcher.group(2));
                variables.put(varName, varValue);
                appendToOutput("Variable created: " + varName + " = " + varValue);
            } 
            // Check for setSpeed command
            else if (line.matches("setSpeed\\(\\w+\\);")) {
                Pattern paramPattern = Pattern.compile("setSpeed\\((\\w+)\\);");
                Matcher paramMatcher = paramPattern.matcher(line);
                
                if (paramMatcher.find()) {
                    String varName = paramMatcher.group(1);
                    if (variables.containsKey(varName)) {
                        int speed = (int) variables.get(varName);
                        sprite.setSpeed(speed);
                        appendToOutput("Set speed to " + speed + " using variable " + varName);
                        usedVariable = true;
                        updateVariableDisplay();
                        
                        // Debug output
                        appendToOutput("DEBUG: Speed set to " + speed);
                    } else {
                        appendToOutput("Error: Variable '" + varName + "' not defined");
                    }
                }
            }
            // Check for movement commands
            else if (line.matches("moveRight\\(\\);.*")) {
                appendToOutput("Executing: moveRight()");
                sprite.moveRight();
                
                // Debug position
                appendToOutput("DEBUG: Sprite position after moveRight: X:" + sprite.getXPos() + ", Y:" + sprite.getYPos());
            } else if (line.matches("moveLeft\\(\\);.*")) {
                appendToOutput("Executing: moveLeft()");
                sprite.moveLeft();
                
                // Debug position
                appendToOutput("DEBUG: Sprite position after moveLeft: X:" + sprite.getXPos() + ", Y:" + sprite.getYPos());
            } else {
                appendToOutput("Unrecognized command: " + line);
            }
        }
        
        // Debug collision check
        boolean inGoalX = sprite.getXPos() >= goal.getX() && sprite.getXPos() <= goal.getX() + goal.getWidth();
        boolean inGoalY = sprite.getYPos() >= goal.getY() && sprite.getYPos() <= goal.getY() + goal.getHeight();
        appendToOutput("DEBUG: In goal area? X:" + inGoalX + ", Y:" + inGoalY + 
                      " (Used variable: " + usedVariable + ")");
        
        // Check if the level is completed
        checkLevelCompletion();
    }
    
    private void updateVariableDisplay() {
        if (speedDisplay != null) {
            speedDisplay.setText("Speed: " + sprite.getSpeed());
        }
    }
    
    private void checkLevelCompletion() {
        // Check if the sprite is in the goal area and used a variable
        if (sprite.getXPos() >= goal.getX() && 
            sprite.getXPos() <= goal.getX() + goal.getWidth() &&
            sprite.getYPos() >= goal.getY() && 
            sprite.getYPos() <= goal.getY() + goal.getHeight() && 
            usedVariable) {
            completeLevel();
        }
    }
    
    @Override
    protected void onReset() {
        // Clear variables
        variables.clear();
        usedVariable = false;
        
        // Reset speed display
        if (speedDisplay != null) {
            speedDisplay.setText("Speed: " + sprite.getSpeed());
        }
    }
}