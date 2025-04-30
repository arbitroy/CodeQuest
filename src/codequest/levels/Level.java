package codequest.levels;

import javafx.scene.Scene;

/**
 * Level - Base interface for all game levels
 */
public interface Level {
    /**
     * Creates and returns the scene for this level
     */
    Scene createLevelScene();

    /**
     * Processes a command entered by the user
     */
    void processCommand(String command);

    /**
     * Checks if the level is completed
     */
    boolean isCompleted();
}