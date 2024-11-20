package com.mateusz.mastermind.mastermind;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import java.io.IOException;

public class DifficultyLevelController {

    @FXML
    private Button startGameButton;

    @FXML
    private ComboBox<String> difficultyComboBox;

    @FXML
    public void initialize() {
        difficultyComboBox.getItems().addAll("Easy", "Medium", "Hard");
        difficultyComboBox.setOnAction(event -> {
            startGameButton.setDisable(difficultyComboBox.getValue() == null);
        });
    }

    @FXML
    public void showDifficultyDetails() {
        String selectedDifficulty = difficultyComboBox.getValue();

        if (selectedDifficulty == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Level Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a difficulty level to see the details.");
            alert.showAndWait();
            return;
        }
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
    public void startGame(ActionEvent event) {
        String selectedDifficulty = difficultyComboBox.getValue();
        if (selectedDifficulty == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Level Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a difficulty level before starting the game.");
            alert.showAndWait();
            return;
        }
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("game-view.fxml"));
            Parent root = fxmlLoader.load();
            GameController gameController = fxmlLoader.getController();
            gameController.startGame(selectedDifficulty);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}