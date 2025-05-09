package codequest.levels;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import codequest.GameManager;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * LoopsLevel - Level 4: Learning loops
 * Fixed with automatic targeting and proper display updates
 */
public class LoopsLevel extends BaseLevel {

    private Rectangle goal;
    private List<Rectangle> targets = new ArrayList<>(); // Changed to Rectangle for consistency
    private int targetsHit = 0;
    private boolean usedLoop = false;
    private Text statusText; // Store reference to status text for easier updates

    public LoopsLevel(GameManager gameManager) {
        super(gameManager);
    }

    @Override
    protected String getLevelName() {
        return "Learn Loops";
    }

    @Override
    protected int getLevelNumber() {
        return 4;
    }

    @Override
    protected String getLevelInstructions() {
        return "You're doing great! Now let's learn about loops.\n" +
               "Loops let you repeat actions multiple times without writing the same code over and over.\n" +
               "Your goal: Use a for loop to shoot at all the targets, then reach the green area.\n" +
               "(Don't worry about aiming - your shots will automatically hit the targets in order)";
    }

    @Override
    protected String getStarterCode() {
        return "// Use a for loop to shoot multiple times\n" +
               "// Example: for (int i = 0; i < 3; i++) {\n" +
               "//     shoot();\n" +
               "// }\n" +
               "\n" +
               "// Then move to the goal\n" +
               "// moveRight();\n";
    }

    @Override
    protected String getHelpText() {
        return "Loops let you repeat code multiple times.\n" +
               "A for loop has three parts:\n\n" +
               "for (initialization; condition; update) {\n" +
               "    // code to repeat\n" +
               "}\n\n" +
               "For example:\n" +
               "for (int i = 0; i < 3; i++) {\n" +
               "    shoot();\n" +
               "}\n\n" +
               "This means:\n" +
               "1. Start with i = 0\n" +
               "2. Check if i < 3 (if true, run the code inside)\n" +
               "3. After running the code, add 1 to i (i++)\n" +
               "4. Repeat steps 2-3 until i is not less than 3\n\n" +
               "In this level, use a loop to shoot at all 3 targets.\n" +
               "Your shots will automatically hit targets in sequence - no need to aim.";
    }

    @Override
    public Scene createLevelScene() {
        Scene scene = super.createLevelScene();

        // Add goal area
        goal = new Rectangle(100, 50);
        goal.setFill(Color.GREEN);
        goal.setX(500); // Right side for moveRight
        goal.setY(200);
        goal.setOpacity(0.8);

        // Add targets as Rectangles (not ImageViews) for consistency
        for (int i = 0; i < 3; i++) {
            Rectangle target = new Rectangle(30, 30);
            target.setFill(Color.RED);
            target.setX(200 + (i * 100)); // Spread across the visible area
            target.setY(100 + (i * 50));  // At different heights
            
            backgroundLayer.getChildren().add(target);
            targets.add(target);
        }

        // Add status display and store reference
        statusText = new Text("Targets Hit: 0/3");
        statusText.setX(20);
        statusText.setY(50);
        statusText.setFill(Color.WHITE);
        statusText.setFont(Font.font("Arial", 14));

        backgroundLayer.getChildren().add(goal);
        foregroundLayer.getChildren().add(statusText);

        appendToOutput("DEBUG: Goal placed at X:" + goal.getX() + ", Y:" + goal.getY());
        appendToOutput("Welcome to Level 4: Loops!\nUse a for loop to shoot at all the targets.");
        appendToOutput("Your shots will automatically hit targets in sequence.");

        return scene;
    }

    @Override
    public void processCommand(String command) {
        appendToOutput("\n--- Running your code ---");

        // Check for for loop pattern
        Pattern forLoopPattern = Pattern.compile("for\\s*\\(\\s*int\\s+\\w+\\s*=\\s*\\d+\\s*;\\s*\\w+\\s*<\\s*\\d+\\s*;\\s*\\w+\\+\\+\\s*\\)\\s*\\{([^}]*)\\}");
        Matcher forLoopMatcher = forLoopPattern.matcher(command);

        while (forLoopMatcher.find()) {
            String loopBody = forLoopMatcher.group(1);

            // Extract loop parameters
            Pattern loopParamPattern = Pattern.compile("for\\s*\\(\\s*int\\s+(\\w+)\\s*=\\s*(\\d+)\\s*;\\s*\\w+\\s*<\\s*(\\d+)\\s*;");
            Matcher loopParamMatcher = loopParamPattern.matcher(command);

            if (loopParamMatcher.find()) {
                String varName = loopParamMatcher.group(1);
                int startValue = Integer.parseInt(loopParamMatcher.group(2));
                int endValue = Integer.parseInt(loopParamMatcher.group(3));

                appendToOutput("Found for loop with " + varName + " from " + startValue + " to " + (endValue - 1));

                // Execute the loop
                for (int i = startValue; i < endValue; i++) {
                    appendToOutput("Loop iteration: " + varName + " = " + i);

                    // Check if the loop body contains shoot()
                    if (loopBody.contains("shoot()")) {
                        executeShoot();
                        usedLoop = true;

                        // Add a small delay to make the shots visible
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        // Process other commands (like moveRight)
        Pattern moveRightPattern = Pattern.compile("moveRight\\(\\);");
        Matcher moveRightMatcher = moveRightPattern.matcher(command);

        while (moveRightMatcher.find()) {
            appendToOutput("Executing: moveRight()");
            sprite.moveRight();
        }

        // Check if the level is completed
        checkLevelCompletion();
    }

    private void executeShoot() {
        appendToOutput("Executing: shoot()");
        sprite.shoot();

        // Check for target hits - automatic targeting system
        if (targetsHit < targets.size()) {
            // Always hit the next target in sequence
            Rectangle target = targets.get(targetsHit);
            
            // Mark target as hit
            targetsHit++;
            target.setFill(Color.GRAY);
            
            // Update status directly
            updateStatusDisplay();
            
            appendToOutput("Target " + targetsHit + " hit!");
        } else {
            appendToOutput("No more targets to hit!");
        }
    }

    private void updateStatusDisplay() {
        // Update status directly using the stored reference
        if (statusText != null) {
            statusText.setText("Targets Hit: " + targetsHit + "/3");
        }
    }

    private void checkLevelCompletion() {
        // Check if all targets are hit and the sprite reached the goal
        if (targetsHit >= 3 &&
            sprite.getXPos() >= goal.getX() &&
            sprite.getXPos() <= goal.getX() + goal.getWidth() &&
            sprite.getYPos() >= goal.getY() &&
            sprite.getYPos() <= goal.getY() + goal.getHeight() &&
            usedLoop) {
            completeLevel();
        }
    }

    @Override
    protected void onReset() {
        // Reset targets
        targetsHit = 0;
        usedLoop = false;

        // Reset target visuals
        for (Rectangle target : targets) {
            target.setFill(Color.RED);
        }

        // Update status display
        updateStatusDisplay();
    }
}