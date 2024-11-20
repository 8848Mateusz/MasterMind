package com.mateusz.mastermind.mastermind;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;

public class DifficultyLevelController {

    @FXML
    private ComboBox<String> difficultyComboBox;

    @FXML
    public void initialize() {
        difficultyComboBox.getItems().addAll("Easy", "Medium", "Hard");
        difficultyComboBox.setValue("Easy");
    }

    @FXML
    public void showDifficultyDetails() {
        String selectedDifficulty = difficultyComboBox.getValue();
        String details;

        switch (selectedDifficulty) {
            case "Easy":
                details = "12 attempts, 4 colors, no repetitions.";
                break;
            case "Medium":
                details = "10 attempts, 5 colors, repetitions possible.";
                break;
            case "Hard":
                details = "8 attempts, 6 colors, repetitions possible.";
                break;
            default:
                details = "Unknown difficulty level.";
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Show level details");
        alert.setHeaderText("Level: " + selectedDifficulty);
        alert.setContentText(details);
        alert.showAndWait();
    }

    @FXML
    public void startGame() {
        String selectedDifficulty = difficultyComboBox.getValue();
        System.out.println("The game starts at the level: " + selectedDifficulty);
    }
}