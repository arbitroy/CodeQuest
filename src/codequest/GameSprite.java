package codequest;

import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 * GameSprite - Represents the player's character in the game
 */
public class GameSprite {
    
    private ImageView spriteView;
    private double xPos;
    private double yPos;
    private int speed = 5;
    private Pane gamePane;
    
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
     * Creates a new sprite on the given pane
     */
    public GameSprite(Pane gamePane) {
        this.gamePane = gamePane;
        
        // Initialize sprite view
        spriteView = new ImageView();
        spriteView.setFitWidth(80);
        spriteView.setFitHeight(48);
        
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
            spriteView.setStyle("-fx-background-color: #3498db;");
            spriteView.setFitWidth(40);
            spriteView.setFitHeight(40);
        }
        
        // Initial position
        xPos = 50;
        yPos = 300;
        updatePosition();
        
        // Add to the game pane
        gamePane.getChildren().add(spriteView);
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
        if (xPos < gamePane.getWidth() - spriteView.getFitWidth()) {
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
        ImageView projectile = new ImageView();
        projectile.setFitWidth(10);
        projectile.setFitHeight(5);
        projectile.setStyle("-fx-background-color: red;");
        projectile.setLayoutX(xPos + spriteView.getFitWidth());
        projectile.setLayoutY(yPos + spriteView.getFitHeight()/2);
        
        // Add to game pane
        gamePane.getChildren().add(projectile);
        
        // Animate projectile
        TranslateTransition shoot = new TranslateTransition(Duration.millis(500), projectile);
        shoot.setByX(300);
        shoot.setOnFinished(e -> gamePane.getChildren().remove(projectile));
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
        spriteView.setLayoutX(xPos);
        spriteView.setLayoutY(yPos);
    }
    
    /**
     * Update the sprite's animation based on current state
     */
    private void updateAnimation() {
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