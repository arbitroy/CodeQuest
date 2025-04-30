package codequest.levels;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import codequest.GameManager;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

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

//        // Fix whitespace by filling the entire window
//        gamePane.setPrefWidth(1024); // Match window width
//        gamePane.setMaxWidth(Double.MAX_VALUE);
//        backgroundLayer.setPrefWidth(1024);
//        backgroundLayer.setMaxWidth(Double.MAX_VALUE);
//        spriteLayer.setPrefWidth(1024);
//        spriteLayer.setMaxWidth(Double.MAX_VALUE);
//        foregroundLayer.setPrefWidth(1024);
//        foregroundLayer.setMaxWidth(Double.MAX_VALUE);
//
//        // Clear any existing elements
//        backgroundLayer.getChildren().clear();
//        foregroundLayer.getChildren().clear();

        // Set a proper background for the game area that fills the entire width
//        Rectangle background = new Rectangle(0, 0, 1024, GAME_HEIGHT);
//        background.setFill(Color.web("#2c3e50"));
//        backgroundLayer.getChildren().add(background);
//
//        // Add edge borders to clearly define the play area
//        Rectangle leftBorder = new Rectangle(0, 0, 5, GAME_HEIGHT);
//        leftBorder.setFill(Color.web("#1a2639"));
//        Rectangle rightBorder = new Rectangle(1019, 0, 5, GAME_HEIGHT);
//        rightBorder.setFill(Color.web("#1a2639"));
//        backgroundLayer.getChildren().addAll(leftBorder, rightBorder);
//
//        // Make character sprite visible in a fixed, predictable position
//        sprite.moveBack(); // Ensure character is at starting position
//
//        // Add goal area with proper proportions and position
//        goal = new Rectangle(120, 80);
//        goal.setFill(Color.GREEN);
//        goal.setOpacity(0.7);
//        goal.setStroke(Color.WHITE);
//        goal.setStrokeWidth(2);
//        goal.setX(350);
//        goal.setY(200);
//
//        // Create labeled GOAL text inside the goal
//        Text goalLabel = new Text("GOAL");
//        goalLabel.setFill(Color.WHITE);
//        goalLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
//        goalLabel.setX(goal.getX() + goal.getWidth()/2 - 30);
//        goalLabel.setY(goal.getY() + goal.getHeight()/2 + 5);
//
//        // Add a dotted line path from character to goal
//        Line pathLine = new Line(
//            sprite.getXPos() + 40, sprite.getYPos() + 24,
//            goal.getX() + 10, goal.getY() + goal.getHeight()/2
//        );
//        pathLine.setStroke(Color.web("#3498db", 0.7));
//        pathLine.setStrokeWidth(3);
//        pathLine.getStrokeDashArray().addAll(10.0, 10.0);
//
//        // Add START label near character
//        Text startLabel = new Text("START");
//        startLabel.setFill(Color.WHITE);
//        startLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
//        startLabel.setX(sprite.getXPos() - 5);
//        startLabel.setY(sprite.getYPos() - 10);
//
//        // Create an enhanced variable display box
//        VBox variableBox = new VBox(10);
//        variableBox.setLayoutX(120);
//        variableBox.setLayoutY(100);
//        variableBox.setPrefWidth(160);
//        variableBox.setPrefHeight(80);
//        variableBox.setPadding(new Insets(10));
//
//        // Add styling with dark background and rounded corners
//        variableBox.setStyle(
//            "-fx-background-color: #1a1b26;" +
//            "-fx-background-radius: 8px;" +
//            "-fx-border-color: #4a5568;" +
//            "-fx-border-width: 1px;" +
//            "-fx-border-radius: 8px;"
//        );
//
//        // Add drop shadow for depth
//        DropShadow dropShadow = new DropShadow();
//        dropShadow.setColor(Color.web("#000000", 0.5));
//        dropShadow.setRadius(5);
//        dropShadow.setOffsetY(2);
//        variableBox.setEffect(dropShadow);
//
//        // Add VARIABLES title
//        Text varTitle = new Text("VARIABLES");
//        varTitle.setFill(Color.web("#8fbcbb"));
//        varTitle.setFont(Font.font("Monospace", FontWeight.BOLD, 14));
//
//        // Create speed display with monospace font
//        speedDisplay = new Label("Speed: " + sprite.getSpeed());
//        speedDisplay.setTextFill(Color.WHITE);
//        speedDisplay.setFont(Font.font("Monospace", 14));
//
//        variableBox.getChildren().addAll(varTitle, speedDisplay);
//
//        // Add helpful hint text
//        Text hint = new Text("Hint: Use a higher speed value to reach the goal faster!");
//        hint.setFill(Color.LIGHTBLUE);
//        hint.setFont(Font.font("Arial", 14));
//        hint.setX(GAME_WIDTH / 2 - 175);
//        hint.setY(30);
//
//        // Add all elements to appropriate layers
//        backgroundLayer.getChildren().addAll(goal, pathLine);
//        foregroundLayer.getChildren().addAll(goalLabel, startLabel, variableBox, hint);
//
//        appendToOutput("DEBUG: Goal placed at X:" + goal.getX() + ", Y:" + goal.getY() +
//                       " with width:" + goal.getWidth() + ", height:" + goal.getHeight());
//        appendToOutput("Welcome to Level 2: Variables!\nCreate a speed variable and use it to reach the green area.");

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

            // Change color based on speed for visual feedback
            if (sprite.getSpeed() > 10) {
                speedDisplay.setTextFill(Color.web("#2ecc71")); // Green for fast
            } else if (sprite.getSpeed() < 5) {
                speedDisplay.setTextFill(Color.web("#e74c3c")); // Red for slow
            } else {
                speedDisplay.setTextFill(Color.WHITE); // Default
            }
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
            speedDisplay.setTextFill(Color.WHITE);
        }
    }
}