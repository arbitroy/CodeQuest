package codequest;

import javafx.animation.TranslateTransition;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * GameSprite - Represents the player's character in the game
 * Fixed to ensure consistent positioning
 */
public class GameSprite {

    private ImageView spriteView;
    private Group fallbackSprite;
    private double xPos;
    private double yPos;
    private int speed = 5;
    private Pane spriteLayer;

    // Animation images
    private Image idleImage;
    private Image[] jumpImages = new Image[2];
    private Image[] runLeftImages = new Image[8];
    private Image[] runRightImages = new Image[8];

    // Animation state
    private String currentState = "idle";
    private int currentFrame = 0;
    private final int FRAME_DELAY = 5;
    private int frameCounter = 0;

    // Debug flag - set to true to show position markers
    private final boolean DEBUG_POSITIONING = false;

    /**
     * Creates a new sprite on the given sprite layer
     */
    public GameSprite(Pane spriteLayer) {
        this.spriteLayer = spriteLayer;

        // Initialize sprite view
        spriteView = new ImageView();
        spriteView.setFitWidth(80);
        spriteView.setFitHeight(48);

        // Ensure sprite has higher z-index
        spriteView.setViewOrder(-1);  // Lower viewOrder means higher z-index

        // Load all sprite images
        try {
            // Load idle sprite
            idleImage = new Image(getClass().getResourceAsStream("/codequest/assets/idle.png"));

            // Load jump sprites
            jumpImages[0] = new Image(getClass().getResourceAsStream("/codequest/assets/jump000.png"));
            jumpImages[1] = new Image(getClass().getResourceAsStream("/codequest/assets/jump001.png"));

            // Load running left sprites
            for (int i = 0; i < 8; i++) {
                String frameNumber = String.format("%03d", i);
                runLeftImages[i] = new Image(getClass().getResourceAsStream("/codequest/assets/run_left" + frameNumber + ".png"));
            }

            // Load running right sprites
            for (int i = 0; i < 8; i++) {
                String frameNumber = String.format("%03d", i);
                runRightImages[i] = new Image(getClass().getResourceAsStream("/codequest/assets/run_right" + frameNumber + ".png"));
            }

            // Set initial image
            spriteView.setImage(idleImage);
        } catch (Exception e) {
            // If images can't be loaded, create a colored rectangle as fallback
            System.out.println("Error loading sprite images: " + e.getMessage());

            // Create a more visible fallback sprite
            fallbackSprite = createFallbackSprite();

            // Add the fallback sprite to the sprite layer
            spriteLayer.getChildren().add(fallbackSprite);

            // Set properties for positioning
            xPos = 50;
            yPos = 200;
            updatePosition();
            return;
        }

        // Initial position - consistent starting point
        xPos = 50;
        yPos = 200;
        updatePosition();

        // Add to the sprite layer
        spriteLayer.getChildren().add(spriteView);

        // Add position debug marker if enabled
        if (DEBUG_POSITIONING) {
            addPositionMarker();
        }
    }

    /**
     * Create a fallback sprite using JavaFX shapes
     */
    private Group createFallbackSprite() {
        Group character = new Group();
        character.setViewOrder(-1); // Ensure it's on top

        // Body (blue rectangle)
        Rectangle body = new Rectangle(40, 40);
        body.setFill(Color.DODGERBLUE);
        body.setArcWidth(10);
        body.setArcHeight(10);

        // Head (circle)
        Circle head = new Circle(20, 10, 15);
        head.setFill(Color.LIGHTBLUE);

        // Eyes
        Circle leftEye = new Circle(15, 8, 3);
        leftEye.setFill(Color.WHITE);
        Circle rightEye = new Circle(25, 8, 3);
        rightEye.setFill(Color.WHITE);

        // Pupils
        Circle leftPupil = new Circle(15, 8, 1.5);
        Circle rightPupil = new Circle(25, 8, 1.5);

        character.getChildren().addAll(body, head, leftEye, rightEye, leftPupil, rightPupil);
        return character;
    }

    /**
     * Move the sprite left
     */
    public void moveLeft() {
        if (xPos > 0) {
            xPos -= speed * 10; // Increased movement distance

            // Set sprite state to running left
            currentState = "runLeft";

            // Make sure we don't go off screen
            if (xPos < 0) {
                xPos = 0;
            }

            // Move the sprite to new position
            moveToPosition(xPos, yPos);
        }
    }

    /**
     * Move the sprite right
     */
    public void moveRight() {
        double maxX = spriteLayer.getWidth() - (spriteView != null ? spriteView.getFitWidth() : 40);
        if (xPos < maxX) {
            xPos += speed * 10; // Increased movement distance

            // Set sprite state to running right
            currentState = "runRight";

            // Make sure we don't go off screen
            if (xPos > maxX) {
                xPos = maxX;
            }

            // Move the sprite to new position
            moveToPosition(xPos, yPos);
        }
    }

    /**
     * Jump action with proper animation reset
     */
    public void jump() {
        // Save original y position
        final double startingY = yPos;

        // Set sprite state to jumping
        currentState = "jump";

        // Ensure we start with the first jump frame
        if (spriteView != null && jumpImages.length > 0) {
            spriteView.setImage(jumpImages[0]);
        }

        if (spriteView != null) {
            // Cancel any ongoing animations first
            spriteView.setTranslateY(0);

            // Jump up animation
            TranslateTransition jumpUp = new TranslateTransition(Duration.millis(300), spriteView);
            jumpUp.setByY(-100);

            // Fall back down animation
            TranslateTransition fallDown = new TranslateTransition(Duration.millis(300), spriteView);
            fallDown.setToY(0); // Return to zero translation

            // Execute jump sequence
            jumpUp.setOnFinished(e -> {
                // Show second jump frame
                if (jumpImages.length > 1) {
                    spriteView.setImage(jumpImages[1]);
                }
                fallDown.play();
            });

            fallDown.setOnFinished(e -> {
                // CRITICAL FIX: Explicitly set back to idle image
                currentState = "idle";
                spriteView.setImage(idleImage);

                // Reset position completely
                spriteView.setTranslateY(0);
                spriteView.setLayoutY(startingY);
                yPos = startingY;
            });

            jumpUp.play();
        }
        else if (fallbackSprite != null) {
            // Similar code for fallback sprite
            fallbackSprite.setTranslateY(0);

            TranslateTransition jumpUp = new TranslateTransition(Duration.millis(300), fallbackSprite);
            jumpUp.setByY(-100);

            TranslateTransition fallDown = new TranslateTransition(Duration.millis(300), fallbackSprite);
            fallDown.setToY(0);

            jumpUp.setOnFinished(e -> fallDown.play());

            fallDown.setOnFinished(e -> {
                // Reset completely
                fallbackSprite.setTranslateY(0);
                fallbackSprite.setLayoutY(startingY);
                yPos = startingY;
            });

            jumpUp.play();
        }
    }

    /**
     * Shoot action
     */
    public void shoot() {
        // Create a projectile
        Rectangle projectile = new Rectangle(10, 5);
        projectile.setFill(Color.RED);

        // Position at the sprite's current position
        double startX = xPos + (spriteView != null ? spriteView.getFitWidth() : 40);
        double startY = yPos + (spriteView != null ? spriteView.getFitHeight()/2 : 20);

        projectile.setX(startX);
        projectile.setY(startY);

        // Add to sprite layer
        spriteLayer.getChildren().add(projectile);

        // Animate projectile
        TranslateTransition shoot = new TranslateTransition(Duration.millis(500), projectile);
        shoot.setByX(300);
        shoot.setOnFinished(e -> spriteLayer.getChildren().remove(projectile));
        shoot.play();
    }

    /**
     * Move back action
     */
    public void moveBack() {
        xPos = 50;
        moveToPosition(xPos, yPos);
    }

    /**
     * Set the sprite's speed
     */
    public void setSpeed(int newSpeed) {
        if (newSpeed > 0 && newSpeed <= 20) {
            this.speed = newSpeed;
        }
    }

    /**
     * Get the sprite's speed
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Get the sprite's X position
     */
    public double getXPos() {
        return xPos;
    }

    /**
     * Get the sprite's Y position
     */
    public double getYPos() {
        return yPos;
    }

    /**
     * Move sprite to a specific position with animation
     */
    private void moveToPosition(double newX, double newY) {
        if (spriteView != null) {
            // Reset any translations that might be in effect
            spriteView.setTranslateX(0);
            spriteView.setTranslateY(0);

            // Animate movement
            TranslateTransition move = new TranslateTransition(Duration.millis(150), spriteView);
            move.setToX(newX - spriteView.getLayoutX());
            move.setToY(newY - spriteView.getLayoutY());

            // After animation completes, update the actual position
            move.setOnFinished(e -> {
                spriteView.setLayoutX(newX);
                spriteView.setLayoutY(newY);
                spriteView.setTranslateX(0);
                spriteView.setTranslateY(0);

                // Return to idle state
                currentState = "idle";
                updateAnimation();

                // Update debug marker if enabled
                if (DEBUG_POSITIONING) {
                    updatePositionMarker();
                }
            });

            move.play();

            // Update animation frame
            updateAnimation();
        }
        else if (fallbackSprite != null) {
            // Update fallback sprite position directly
            fallbackSprite.setLayoutX(newX);
            fallbackSprite.setLayoutY(newY);

            // Update debug marker if enabled
            if (DEBUG_POSITIONING) {
                updatePositionMarker();
            }
        }
    }

    /**
     * Update the sprite's position immediately without animation
     */
    private void updatePosition() {
        if (spriteView != null) {
            spriteView.setLayoutX(xPos);
            spriteView.setLayoutY(yPos);
            spriteView.setTranslateX(0);
            spriteView.setTranslateY(0);
        }
        else if (fallbackSprite != null) {
            fallbackSprite.setLayoutX(xPos);
            fallbackSprite.setLayoutY(yPos);
            fallbackSprite.setTranslateX(0);
            fallbackSprite.setTranslateY(0);
        }

        // Update debug marker if enabled
        if (DEBUG_POSITIONING) {
            updatePositionMarker();
        }
    }

    /**
     * Update the sprite's animation based on current state
     */
    private void updateAnimation() {
        // Skip if using fallback sprite (no spriteView)
        if (spriteView == null) {
			return;
		}

        // Advance frame counter
        frameCounter++;

        // Only update animation every FRAME_DELAY frames for smoother animation
        if (frameCounter >= FRAME_DELAY) {
            frameCounter = 0;
            currentFrame = (currentFrame + 1) % 8; // 8 frames for running animations

            switch (currentState) {
                case "idle":
                    spriteView.setImage(idleImage);
                    break;
                case "jump":
                    spriteView.setImage(jumpImages[0]); // First jump frame
                    break;
                case "runLeft":
                    spriteView.setImage(runLeftImages[currentFrame]);
                    break;
                case "runRight":
                    spriteView.setImage(runRightImages[currentFrame]);
                    break;
                default:
                    spriteView.setImage(idleImage);
            }
        }
    }

    /* Debug helpers */

    private Circle positionMarker;

    private void addPositionMarker() {
        positionMarker = new Circle(5);
        positionMarker.setFill(Color.RED);
        positionMarker.setStroke(Color.BLACK);
        positionMarker.setCenterX(xPos);
        positionMarker.setCenterY(yPos);
        spriteLayer.getChildren().add(positionMarker);
    }

    private void updatePositionMarker() {
        if (positionMarker != null) {
            positionMarker.setCenterX(xPos);
            positionMarker.setCenterY(yPos);
        }
    }
}