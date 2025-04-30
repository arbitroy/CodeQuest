package codequest;

import codequest.levels.CommandsLevel;
import codequest.levels.ConditionalsLevel;
import codequest.levels.FreeRoamLevel;
import codequest.levels.Level;
import codequest.levels.LoopsLevel;
import codequest.levels.VariablesLevel;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * GameManager - Manages game levels and transitions
 * Redesigned with improved UI and transitions
 */
public class GameManager {

    private Stage primaryStage;
    private int currentLevel = 0;
    private Level[] levels;
    private Scene startScene;
    private Scene completionScene;

    // Constants
    private static final int WINDOW_WIDTH = 1024;
    private static final int WINDOW_HEIGHT = 768;

    public GameManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
        initializeLevels();
        createStartScene();
        createCompletionScene();
    }

    private void initializeLevels() {
        levels = new Level[5];
        levels[0] = new CommandsLevel(this);       // Level 1: Commands
        levels[1] = new VariablesLevel(this);      // Level 2: Variables
        levels[2] = new ConditionalsLevel(this);   // Level 3: Conditionals
        levels[3] = new LoopsLevel(this);          // Level 4: Loops
        levels[4] = new FreeRoamLevel(this);       // Level 5: Free Roam
    }

    private void createStartScene() {
        // Create a modern, visually appealing start scene
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #1e3c72, #2a5298);");

        // Create a container for content with a slight shadow
        StackPane contentContainer = new StackPane();
        contentContainer.setPadding(new Insets(40));

        VBox centerBox = new VBox(30);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setMaxWidth(600);

        // Game logo/title with shadow effect
        Text title = new Text("CodeQuest");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 72));
        title.setFill(Color.WHITE);
        title.setStroke(Color.web("#2c3e50", 0.3));
        title.setStrokeWidth(2);

        // Apply a drop shadow effect
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.web("#000000", 0.5));
        dropShadow.setRadius(15);
        dropShadow.setOffsetY(5);
        title.setEffect(dropShadow);

        // Subtitle with coding theme
        Text subtitle = new Text("Learn programming through adventure!");
        subtitle.setFont(Font.font("Arial", FontWeight.NORMAL, 24));
        subtitle.setFill(Color.web("#ecf0f1"));
        subtitle.setTextAlignment(TextAlignment.CENTER);

        // Game description
        Text description = new Text(
            "Embark on a journey to master coding concepts including commands, " +
            "variables, conditionals, and loops through interactive gameplay."
        );
        description.setFont(Font.font("Arial", 16));
        description.setFill(Color.web("#ecf0f1", 0.9));
        description.setTextAlignment(TextAlignment.CENTER);
        description.setWrappingWidth(500);

        // Start button with hover effect
        Button startButton = new Button("Start Adventure");
        startButton.setPrefSize(240, 60);
        startButton.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        startButton.setStyle(
            "-fx-background-color: #2ecc71; " +
            "-fx-text-fill: white; " +
            "-fx-background-radius: 30; " +
            "-fx-cursor: hand;"
        );

        // Add hover effect
        startButton.setOnMouseEntered(e ->
            startButton.setStyle(
                "-fx-background-color: #27ae60; " +
                "-fx-text-fill: white; " +
                "-fx-background-radius: 30; " +
                "-fx-cursor: hand; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 3);"
            )
        );

        startButton.setOnMouseExited(e ->
            startButton.setStyle(
                "-fx-background-color: #2ecc71; " +
                "-fx-text-fill: white; " +
                "-fx-background-radius: 30; " +
                "-fx-cursor: hand;"
            )
        );

        startButton.setOnAction(e -> startGame());

        // Add decorative elements - coding icons
        try {
            ImageView codeIcon = new ImageView(new Image(getClass().getResourceAsStream("/codequest/assets/code-icon.png")));
            codeIcon.setFitWidth(80);
            codeIcon.setFitHeight(80);
            codeIcon.setOpacity(0.8);

            centerBox.getChildren().addAll(title, subtitle, description, startButton);

        } catch (Exception e) {
            // If image can't be loaded, just continue without it
            System.out.println("Could not load decorative images: " + e.getMessage());
            centerBox.getChildren().addAll(title, subtitle, description, startButton);
        }

        contentContainer.getChildren().add(centerBox);
        root.setCenter(contentContainer);

        // Create scene with fade-in effect
        startScene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

        // Add style sheet
        startScene.getStylesheets().add(getClass().getResource("/codequest/assets/styles.css").toExternalForm());
    }

    private void createCompletionScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #136a8a, #267871);");

        VBox centerBox = new VBox(30);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(40));

        // Congratulations text with special effect
        Text congratsText = new Text("Congratulations!");
        congratsText.setFont(Font.font("Arial", FontWeight.BOLD, 72));
        congratsText.setFill(Color.WHITE);
        congratsText.setStroke(Color.web("#033649", 0.3));
        congratsText.setStrokeWidth(2);

        // Apply animation to the text
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1.5), congratsText);
        fadeTransition.setFromValue(0.7);
        fadeTransition.setToValue(1.0);
        fadeTransition.setCycleCount(Animation.INDEFINITE);
        fadeTransition.setAutoReverse(true);
        fadeTransition.play();

        // Completion message
        Text completionText = new Text(
            "You've completed all levels of CodeQuest and mastered the basics of programming!\n" +
            "You've learned about commands, variables, conditionals, and loops - " +
            "the fundamental building blocks of coding."
        );
        completionText.setFont(Font.font("Arial", 18));
        completionText.setFill(Color.web("#ecf0f1"));
        completionText.setTextAlignment(TextAlignment.CENTER);
        completionText.setWrappingWidth(700);

        // Achievement message
        Text achievementText = new Text(
            "Now you're ready to continue your coding journey with these core concepts mastered."
        );
        achievementText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        achievementText.setFill(Color.web("#f1c40f"));
        achievementText.setTextAlignment(TextAlignment.CENTER);

        // Buttons for next actions
        Button restartButton = new Button("Play Again");
        restartButton.setPrefSize(200, 50);
        restartButton.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        restartButton.setStyle(
            "-fx-background-color: #2ecc71; " +
            "-fx-text-fill: white; " +
            "-fx-background-radius: 25; " +
            "-fx-cursor: hand;"
        );
        restartButton.setOnAction(e -> startGame());

        Button exitButton = new Button("Exit Game");
        exitButton.setPrefSize(200, 50);
        exitButton.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        exitButton.setStyle(
            "-fx-background-color: #e74c3c; " +
            "-fx-text-fill: white; " +
            "-fx-background-radius: 25; " +
            "-fx-cursor: hand;"
        );
        exitButton.setOnAction(e -> primaryStage.close());

        // Button container
        VBox buttonBox = new VBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(restartButton, exitButton);

        centerBox.getChildren().addAll(
            congratsText,
            completionText,
            achievementText,
            buttonBox
        );

        root.setCenter(centerBox);

        completionScene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        completionScene.getStylesheets().add(getClass().getResource("/codequest/assets/styles.css").toExternalForm());
    }

    public Scene getStartScene() {
        return startScene;
    }

    public void startGame() {
        currentLevel = 0;
        loadCurrentLevel();
    }

    public void loadCurrentLevel() {
        if (currentLevel < levels.length) {
            Scene levelScene = levels[currentLevel].createLevelScene();

            // Add transition effect
            FadeTransition fadeIn = new FadeTransition(Duration.millis(800), levelScene.getRoot());
            fadeIn.setFromValue(0.3);
            fadeIn.setToValue(1.0);

            primaryStage.setScene(levelScene);
            fadeIn.play();
        } else {
            // Game completed
            showGameCompletedScene();
        }
    }

    public void nextLevel() {
        currentLevel++;
        loadCurrentLevel();
    }

    private void showGameCompletedScene() {
        // Add transition effect
        FadeTransition fadeIn = new FadeTransition(Duration.millis(1000), completionScene.getRoot());
        fadeIn.setFromValue(0.3);
        fadeIn.setToValue(1.0);

        primaryStage.setScene(completionScene);
        fadeIn.play();
    }
}