package codequest.levels;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import codequest.GameManager;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * ConditionalsLevel - Level 3: Learning conditional statements
 */
public class ConditionalsLevel extends BaseLevel {

    private Rectangle goal;
    private Rectangle enemy; // Changed from ImageView to Rectangle
    private boolean enemyNear = false;
    private Map<String, Object> variables = new HashMap<>();
    private boolean usedConditional = false;
    private boolean handledEnemyCorrectly = false; // Added as class field
    private Timeline enemyTimeline;
    private Random random = new Random();

    public ConditionalsLevel(GameManager gameManager) {
        super(gameManager);
    }

    @Override
    protected String getLevelName() {
        return "Learn Conditionals";
    }

    @Override
    protected int getLevelNumber() {
        return 3;
    }

    @Override
    protected String getLevelInstructions() {
        return "Great progress! Now let's learn about conditionals.\n" +
               "Conditionals let your program make decisions based on conditions.\n" +
               "Your goal: Use an if statement to move back when the enemy is near, then reach the green area.";
    }

    @Override
    protected String getStarterCode() {
        return "// Use an if statement to check if enemy is near\n" +
               "// Example: if (enemyNear == true) {\n" +
               "//     moveBack();\n" +
               "// }\n" +
               "\n" +
               "// Then move to the goal\n" +
               "// moveRight();\n";
    }

    @Override
    protected String getHelpText() {
        return "Conditionals let your program make decisions.\n" +
               "The basic form is:\n\n" +
               "if (condition) {\n" +
               "    // code to run if condition is true\n" +
               "}\n\n" +
               "You can check:\n" +
               "- enemyNear == true (or just enemyNear) - Is the enemy close?\n\n" +
               "If the condition is true, the code inside the curly braces will run.\n" +
               "In this level, you need to:\n" +
               "1. Check if the enemy is near\n" +
               "2. If so, move back\n" +
               "3. Then move right to reach the goal";
    }

    @Override
    public Scene createLevelScene() {
        Scene scene = super.createLevelScene();

        // Add goal area using existing UI style
        goal = new Rectangle(100, 50);
        goal.setFill(Color.GREEN);
        goal.setX(500); // Right side of screen for moveRight
        goal.setY(200);
        goal.setOpacity(0.8);

        // Add enemy - changed to Rectangle for visibility
        enemy = new Rectangle(40, 40);
        enemy.setFill(Color.RED);
        enemy.setX(150);
        enemy.setY(200);

        // Add status display
        Text statusText = new Text("Enemy Near: " + enemyNear);
        statusText.setX(20);
        statusText.setY(50);
        statusText.setFill(Color.WHITE);
        statusText.setFont(Font.font("Arial", 14));

        backgroundLayer.getChildren().add(goal);
        spriteLayer.getChildren().add(enemy);
        foregroundLayer.getChildren().add(statusText);

        // Set up enemy movement
        setupEnemyMovement();

        appendToOutput("DEBUG: Goal placed at X:" + goal.getX() + ", Y:" + goal.getY());
        appendToOutput("Welcome to Level 3: Conditionals!\nUse an if statement to move back when the enemy is near.");

        return scene;
    }

    // Movement pattern counter
    private int movePatternStep = 0;
    
    private void setupEnemyMovement() {
        // Create a timeline for enemy movement with a predictable pattern
        enemyTimeline = new Timeline(
            new KeyFrame(Duration.seconds(1), event -> {
                // Use a predictable pattern instead of random:
                // Pattern: Far, Near, Far, Far, Near
                switch (movePatternStep) {
                    case 0: // Far
                    case 2: // Far
                    case 3: // Far
                        enemy.setX(700); // Far position
                        enemyNear = false;
                        break;
                    case 1: // Near
                    case 4: // Near
                        enemy.setX(200); // Near position
                        enemyNear = true;
                        break;
                }
                
                // Increment pattern step and loop back to start
                movePatternStep = (movePatternStep + 1) % 5;

                // Update the status display
                updateStatusDisplay();
            })
        );

        enemyTimeline.setCycleCount(Animation.INDEFINITE);
        enemyTimeline.play();
    }

    private void updateStatusDisplay() {
        // Find and update the text node
        gamePane.getChildren().stream()
                .filter(node -> node instanceof Text)
                .map(node -> (Text) node)
                .filter(text -> text.getText().startsWith("Enemy Near"))
                .findFirst()
                .ifPresent(text -> text.setText("Enemy Near: " + enemyNear));
    }

    @Override
    public void processCommand(String command) {
        appendToOutput("\n--- Running your code ---");

        // No local handledEnemyCorrectly variable anymore, using class field

        // Check for if statement pattern
        Pattern ifPattern = Pattern.compile("if\\s*\\(\\s*(\\w+)\\s*(?:==\\s*true)?\\s*\\)\\s*\\{([^}]*)\\}");
        Matcher ifMatcher = ifPattern.matcher(command);

        while (ifMatcher.find()) {
            String condition = ifMatcher.group(1);
            String ifBody = ifMatcher.group(2);

            if (condition.equals("enemyNear")) {
                appendToOutput("Checking condition: enemyNear is " + enemyNear);

                if (enemyNear) {
                    appendToOutput("Condition is true, executing if block");

                    // Check if the if body contains moveBack()
                    if (ifBody.contains("moveBack()")) {
                        appendToOutput("Executing: moveBack()");
                        sprite.moveBack();
                        handledEnemyCorrectly = true; // This now persists between runs
                        usedConditional = true;
                    }
                } else {
                    appendToOutput("Condition is false, skipping if block");
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

        // Check if the level is completed - no parameter needed
        checkLevelCompletion();
    }

    private void checkLevelCompletion() {
        // Check if sprite reached the goal and handled the enemy correctly
        if (sprite.getXPos() >= goal.getX() &&
            sprite.getXPos() <= goal.getX() + goal.getWidth() &&
            sprite.getYPos() >= goal.getY() &&
            sprite.getYPos() <= goal.getY() + goal.getHeight() &&
            usedConditional && handledEnemyCorrectly) {
            completeLevel();

            // Stop the enemy timeline
            if (enemyTimeline != null) {
                enemyTimeline.stop();
            }
        }
    }

    @Override
    protected void onReset() {
        // Reset state
        enemyNear = false;
        usedConditional = false;
        handledEnemyCorrectly = false; // Reset this flag too
        movePatternStep = 0; // Reset pattern to start from beginning

        // Restart enemy movement
        if (enemyTimeline != null) {
            enemyTimeline.stop(); // Stop first to clear any existing timelines
            enemyTimeline.play();
        }

        // Update status display
        updateStatusDisplay();
    }
}