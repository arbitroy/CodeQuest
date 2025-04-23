package codequest.levels;

import codequest.GameManager;
import javafx.scene.Scene;
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
 */
public class VariablesLevel extends BaseLevel {
    
    private Rectangle goal;
    private Map<String, Object> variables = new HashMap<>();
    private boolean usedVariable = false;
    
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
               "// Example: speed = 10;\n" +
               "\n" +
               "// Then use the setSpeed command with your variable\n" +
               "// Example: setSpeed(speed);\n" +
               "\n" +
               "// Finally, move to the goal\n" +
               "// moveRight();\n";
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
        
        // Add goal area (green rectangle)
        goal = new Rectangle(100, 50);
        goal.setFill(Color.GREEN);
        goal.setX(500);
        goal.setY(350);
        goal.setOpacity(0.5);
        
        // Add variable status display
        Text varStatus = new Text("Current Speed: " + sprite.getSpeed());
        varStatus.setX(20);
        varStatus.setY(50);
        varStatus.setFill(Color.BLACK);
        varStatus.setFont(Font.font("Arial", 14));
        
        gamePane.getChildren().addAll(goal, varStatus);
        
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
                    } else {
                        appendToOutput("Error: Variable '" + varName + "' not defined");
                    }
                }
            }
            // Check for movement commands
            else if (line.matches("moveRight\\(\\);.*")) {
                appendToOutput("Executing: moveRight()");
                sprite.moveRight();
            } else if (line.matches("moveLeft\\(\\);.*")) {
                appendToOutput("Executing: moveLeft()");
                sprite.moveLeft();
            } else {
                appendToOutput("Unrecognized command: " + line);
            }
        }
        
        // Update the variable display
        updateVariableDisplay();
        
        // Check if the level is completed
        checkLevelCompletion();
    }
    
    private void updateVariableDisplay() {
        // Find and update the text node
        gamePane.getChildren().stream()
                .filter(node -> node instanceof Text)
                .map(node -> (Text)node)
                .filter(text -> text.getText().startsWith("Current Speed"))
                .findFirst()
                .ifPresent(text -> text.setText("Current Speed: " + sprite.getSpeed()));
    }
    
    private void checkLevelCompletion() {
        // Check if the sprite is in the goal area and used a variable
        if (sprite.getXPos() >= 500 && usedVariable) {
            completeLevel();
        }
    }
    
    @Override
    protected void onReset() {
        // Clear variables
        variables.clear();
        usedVariable = false;
    }
}