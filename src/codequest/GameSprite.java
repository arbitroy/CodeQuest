package codequest;

import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.Group;
import javafx.util.Duration;

/**
 * GameSprite - Represents the player's character in the game
 * Updated to work with the layered pane system
 */
public class GameSprite {
    
    private ImageView spriteView;
    private double xPos;
    private double yPos;
    private int speed = 5;
    private Pane spriteLayer; // Changed to be sprite layer specifically
    
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
            Group fallbackSprite = createFallbackSprite();
            
            // Add the fallback sprite to the sprite layer
            spriteLayer.getChildren().add(fallbackSprite);
            
            // Set properties for positioning
            fallbackSprite.setLayoutX(xPos);
            fallbackSprite.setLayoutY(yPos);
            
            // Return early since we're using the fallback
            xPos = 50;
            yPos = 300;
            return;
        }
        
        // Initial position
        xPos = 1;
        yPos = 200;
        updatePosition();
        
        // Add to the sprite layer
        spriteLayer.getChildren().add(spriteView);
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
            xPos -= speed;
            
            // Set sprite state to running left
            currentState = "runLeft";
            animateMove();
        }
    }
    
    /**
     * Move the sprite right
     */
    public void moveRight() {
        if (xPos < spriteLayer.getWidth() - spriteView.getFitWidth()) {
            xPos += speed;
            
            // Set sprite state to running right
            currentState = "runRight";
            animateMove();
        }
    }
    
    /**
     * Jump action
     */
    public void jump() {
        // Save original y position
        double originalY = yPos;
        
        // Set sprite state to jumping
        currentState = "jump";
        updateAnimation();
        
        // Jump up
        TranslateTransition jumpUp = new TranslateTransition(Duration.millis(300), spriteView);
        jumpUp.setByY(-100);
        
        // Fall back down
        TranslateTransition fallDown = new TranslateTransition(Duration.millis(300), spriteView);
        fallDown.setByY(100);
        
        // Execute jump sequence
        jumpUp.setOnFinished(e -> {
            // Show second jump frame
            if (jumpImages.length > 1) {
                spriteView.setImage(jumpImages[1]);
            }
            fallDown.play();
        });
        
        fallDown.setOnFinished(e -> {
            // Return to idle state
            currentState = "idle";
            updateAnimation();
        });
        
        jumpUp.play();
    }
    
    /**
     * Shoot action
     */
    public void shoot() {
        // Create a projectile
        Rectangle projectile = new Rectangle(10, 5);
        projectile.setFill(Color.RED);
        projectile.setLayoutX(xPos + spriteView.getFitWidth());
        projectile.setLayoutY(yPos + spriteView.getFitHeight()/2);
        
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
        if (xPos > 50) {
            xPos = 50;
            animateMove();
        }
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
     * Animate movement of the sprite
     */
    private void animateMove() {
        // Skip if using fallback sprite (no spriteView)
        if (spriteView == null) return;
        
        // Move the sprite
        TranslateTransition move = new TranslateTransition(Duration.millis(100), spriteView);
        move.setToX(xPos);
        move.setToY(yPos);
        
        move.setOnFinished(e -> {
            // Return to idle state after movement completes
            currentState = "idle";
            updateAnimation();
        });
        
        move.play();
        
        // Update animation frame
        updateAnimation();
    }
    
    /**
     * Update the sprite's position immediately
     */
    private void updatePosition() {
        if (spriteView != null) {
            spriteView.setLayoutX(xPos);
            spriteView.setLayoutY(yPos);
        }
    }
    
    /**
     * Update the sprite's animation based on current state
     */
    private void updateAnimation() {
        // Skip if using fallback sprite (no spriteView)
        if (spriteView == null) return;
        
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
}