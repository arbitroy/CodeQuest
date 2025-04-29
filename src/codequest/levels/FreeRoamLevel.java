package codequest.levels;

import codequest.GameManager;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * FreeRoamLevel - Level 5: Applying all learned concepts
 */
public class FreeRoamLevel extends BaseLevel {
    
    private Rectangle goal;
    private List<Rectangle> obstacles = new ArrayList<>();
    private List<Rectangle> targets = new ArrayList<>();
    private Map<String, Object> variables = new HashMap<>();
    private int targetsHit = 0;
    private int totalTargets = 4;
    private boolean enemyNear = false;
    private Rectangle enemy;
    
    public FreeRoamLevel(GameManager gameManager) {
        super(gameManager);
    }
    
    @Override
    protected String getLevelName() {
        return "Free Roam Challenge";
    }
    
    @Override
    protected int getLevelNumber() {
        return 5;
    }
    
    @Override
    protected String getLevelInstructions() {
        return "Congratulations on making it to the final level!\n" +
               "Now it's time to apply everything you've learned.\n" +
               "Your goal: Create a program that avoids obstacles, hits all targets, and reaches the goal area.";
    }
    
    @Override
    protected String getStarterCode() {
        return "// This is the final challenge!\n" +
               "// Use everything you've learned to create a program that:\n" +
               "// 1. Sets a speed variable\n" +
               "// 2. Uses an if statement to check for the enemy\n" +
               "// 3. Uses a loop to shoot at targets\n" +
               "// 4. Moves to the goal\n" +
               "\n" +
               "// Set speed\n" +
               "\n" +
               "// Check for enemy\n" +
               "\n" +
               "// Shoot targets with a loop\n" +
               "\n" +
               "// Move to goal\n";
    }
    
    @Override
    protected String getHelpText() {
        return "This final level tests all the concepts you've learned:\n\n" +
               "1. Variables:\n" +
               "   speed = 10;\n" +
               "   setSpeed(speed);\n\n" +
               "2. Conditionals:\n" +
               "   if (enemyNear) {\n" +
               "       moveBack();\n" +
               "   }\n\n" +
               "3. Loops:\n" +
               "   for (int i = 0; i < 4; i++) {\n" +
               "       shoot();\n" +
               "   }\n\n" +
               "4. Commands:\n" +
               "   moveRight();\n" +
               "   moveLeft();\n" +
               "   jump();\n\n" +
               "Combine these concepts to complete the challenge!";
    }
    @Override
    public Scene createLevelScene() {
        Scene scene = super.createLevelScene();
        
        // Add goal area
        goal = new Rectangle(80, 80);
        goal.setFill(Color.GREEN);
        goal.setX(500); // Right side
        goal.setY(200);
        goal.setOpacity(0.8);
        
        // Add obstacles
        for (int i = 0; i < 3; i++) {
            Rectangle obstacle = new Rectangle(30, 100);
            obstacle.setFill(Color.GRAY);
            obstacle.setX(120 + (i * 150)); // Spread out for visibility
            obstacle.setY(200);
            obstacles.add(obstacle);
            backgroundLayer.getChildren().add(obstacle);
        }
        
        // Add targets - making sure to use Rectangle since targets is List<Rectangle>
        for (int i = 0; i < totalTargets; i++) {
            Rectangle target = new Rectangle(25, 25);
            target.setFill(Color.RED);
            
            // Position targets at different visible locations
            target.setX(150 + (i * 100));
            target.setY(100 + ((i % 2) * 200));
            
            targets.add(target);
            backgroundLayer.getChildren().add(target);
        }
        
        // Add enemy
        enemy = new Rectangle(40, 40);
        enemy.setFill(Color.DARKRED);
        enemy.setX(250);
        enemy.setY(200);
        spriteLayer.getChildren().add(enemy);
        
        // Add status display
        Text statusText = new Text("Targets Hit: 0/" + totalTargets + " | Enemy Near: " + enemyNear + " | Speed: " + sprite.getSpeed());
        statusText.setX(20);
        statusText.setY(30);
        statusText.setFill(Color.WHITE);
        
        foregroundLayer.getChildren().add(statusText);
        backgroundLayer.getChildren().add(goal);
        
        appendToOutput("DEBUG: Goal placed at X:" + goal.getX() + ", Y:" + goal.getY());
        appendToOutput("Welcome to the Final Level: Free Roam Challenge!");
        
        return scene;
    }
    
    @Override
    public void processCommand(String command) {
        appendToOutput("\n--- Running your code ---");
        
        // Process variable assignments
        processVariableAssignments(command);
        
        // Process conditionals
        processConditionals(command);
        
        // Process loops
        processLoops(command);
        
        // Process basic commands
        processBasicCommands(command);
        
        // Update status display
        updateStatusDisplay();
        
        // Check collision with obstacles
        checkObstacleCollisions();
        
        // Check if the level is completed
        checkLevelCompletion();
    }
    
    private void processVariableAssignments(String command) {
        Pattern varPattern = Pattern.compile("(\\w+)\\s*=\\s*(\\d+);");
        Matcher varMatcher = varPattern.matcher(command);
        
        while (varMatcher.find()) {
            String varName = varMatcher.group(1);
            int varValue = Integer.parseInt(varMatcher.group(2));
            variables.put(varName, varValue);
            appendToOutput("Variable created: " + varName + " = " + varValue);
        }
        
        // Check for setSpeed command
        Pattern setSpeedPattern = Pattern.compile("setSpeed\\((\\w+)\\);");
        Matcher setSpeedMatcher = setSpeedPattern.matcher(command);
        
        while (setSpeedMatcher.find()) {
            String varName = setSpeedMatcher.group(1);
            if (variables.containsKey(varName)) {
                int speed = (int) variables.get(varName);
                sprite.setSpeed(speed);
                appendToOutput("Set speed to " + speed + " using variable " + varName);
            } else if (varName.matches("\\d+")) {
                // Direct number value
                int speed = Integer.parseInt(varName);
                sprite.setSpeed(speed);
                appendToOutput("Set speed to " + speed);
            } else {
                appendToOutput("Error: Variable '" + varName + "' not defined");
            }
        }
    }
    
    private void processConditionals(String command) {
        Pattern ifPattern = Pattern.compile("if\\s*\\(\\s*(\\w+)\\s*(?:==\\s*true)?\\s*\\)\\s*\\{([^}]*)\\}");
        Matcher ifMatcher = ifPattern.matcher(command);
        
        while (ifMatcher.find()) {
            String condition = ifMatcher.group(1);
            String ifBody = ifMatcher.group(2);
            
            if (condition.equals("enemyNear")) {
                appendToOutput("Checking condition: enemyNear is " + enemyNear);
                
                if (enemyNear) {
                    appendToOutput("Condition is true, executing if block");
                    processCommandBlock(ifBody);
                } else {
                    appendToOutput("Condition is false, skipping if block");
                }
            }
        }
    }
    
    private void processLoops(String command) {
        Pattern forLoopPattern = Pattern.compile("for\\s*\\(\\s*int\\s+(\\w+)\\s*=\\s*(\\d+)\\s*;\\s*\\w+\\s*<\\s*(\\d+)\\s*;\\s*\\w+\\+\\+\\s*\\)\\s*\\{([^}]*)\\}");
        Matcher forLoopMatcher = forLoopPattern.matcher(command);
        
        while (forLoopMatcher.find()) {
            String varName = forLoopMatcher.group(1);
            int startValue = Integer.parseInt(forLoopMatcher.group(2));
            int endValue = Integer.parseInt(forLoopMatcher.group(3));
            String loopBody = forLoopMatcher.group(4);
            
            appendToOutput("Executing for loop with " + varName + " from " + startValue + " to " + (endValue - 1));
            
            for (int i = startValue; i < endValue; i++) {
                appendToOutput("Loop iteration: " + varName + " = " + i);
                processCommandBlock(loopBody);
                
                // Add a small delay to make actions visible
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private void processBasicCommands(String command) {
        // Process individual commands that are not in blocks
        String[] lines = command.split("\n");
        
        for (String line : lines) {
            // Skip lines that are part of blocks
            if (line.contains("{") || line.contains("}") || line.contains("for") || line.contains("if")) {
                continue;
            }
            
            // Process the command
            processCommandBlock(line);
        }
    }
    
    private void processCommandBlock(String block) {
        // Process each command in the block
        Pattern commandPattern = Pattern.compile("(\\w+)\\((?:([^)]*))?\\);");
        Matcher commandMatcher = commandPattern.matcher(block);
        
        while (commandMatcher.find()) {
            String command = commandMatcher.group(1);
            String params = commandMatcher.group(2);
            
            switch (command) {
                case "moveLeft":
                    appendToOutput("Executing: moveLeft()");
                    sprite.moveLeft();
                    break;
                case "moveRight":
                    appendToOutput("Executing: moveRight()");
                    sprite.moveRight();
                    break;
                case "jump":
                    appendToOutput("Executing: jump()");
                    sprite.jump();
                    break;
                case "shoot":
                    appendToOutput("Executing: shoot()");
                    executeShoot();
                    break;
                case "moveBack":
                    appendToOutput("Executing: moveBack()");
                    sprite.moveBack();
                    break;
                case "setSpeed":
                    // Already handled in processVariableAssignments
                    break;
                default:
                    appendToOutput("Unknown command: " + command);
            }
        }
    }
    
    private void executeShoot() {
        sprite.shoot();
        
        // Check for target hits
        if (targetsHit < totalTargets) {
            // Find the nearest target and hit it
            Rectangle targetToHit = null;
            double minDistance = Double.MAX_VALUE;
            
            for (Rectangle target : targets) {
                if (target.getFill() == Color.RED) {
                    double distance = Math.abs(target.getX() - sprite.getXPos());
                    if (distance < minDistance) {
                        minDistance = distance;
                        targetToHit = target;
                    }
                }
            }
            
            if (targetToHit != null && minDistance < 300) {
                // Mark target as hit
                targetToHit.setFill(Color.GRAY);
                targetsHit++;
            }
        }
    }
    
    private void updateStatusDisplay() {
        // Find and update the text node
        gamePane.getChildren().stream()
                .filter(node -> node instanceof Text)
                .map(node -> (Text) node)
                .filter(text -> text.getText().startsWith("Targets Hit"))
                .findFirst()
                .ifPresent(text -> text.setText("Targets Hit: " + targetsHit + "/" + totalTargets + 
                                              " | Enemy Near: " + enemyNear + 
                                              " | Speed: " + sprite.getSpeed()));
    }
    
    private void checkObstacleCollisions() {
        double spriteX = sprite.getXPos();
        
        for (Rectangle obstacle : obstacles) {
            if (Math.abs(spriteX - obstacle.getX()) < 30) {
                // Move back if collided with obstacle
                sprite.moveBack();
                appendToOutput("Ouch! You hit an obstacle.");
                break;
            }
        }
    }
    
    private void checkLevelCompletion() {
        // Check if all targets are hit and the sprite reached the goal
        if (targetsHit >= totalTargets && 
            sprite.getXPos() >= goal.getX() && 
            sprite.getXPos() <= goal.getX() + goal.getWidth()) {
            completeLevel();
        }
    }
    
    @Override
    protected void onReset() {
        // Reset variables
        variables.clear();
        targetsHit = 0;
        
        // Reset targets
        for (Rectangle target : targets) {
            target.setFill(Color.RED);
        }
        
        // Update status display
        updateStatusDisplay();
    }
}