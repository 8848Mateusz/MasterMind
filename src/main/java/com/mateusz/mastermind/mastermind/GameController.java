package com.mateusz.mastermind.mastermind;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.*;

public class GameController {

    private Timeline timer;
    private int secondsElapsed = 0;
    private boolean isPaused = false;
    private int attempts = 0;
    private int maxAttempts;
    private int codeLength;
    private boolean allowDuplicates;
    private List<Integer> secretCode;

    @FXML
    private String currentDifficulty;

    @FXML
    private Button guessButton;

    @FXML
    private TextField inputField1, inputField2, inputField3, inputField4;

    @FXML
    private ListView<String> historyList;

    @FXML
    private Label infoLabel;

    @FXML
    private HBox inputContainer;

    @FXML
    private Label timerLabel;

    @FXML
    private Button pauseButton;

    @FXML
    private void togglePause() {
        if (isPaused) {
            resumeGame();
        } else {
            pauseGame();
        }
    }

    private void hidePauseButton() {
        pauseButton.setVisible(false);
        pauseButton.setManaged(false);
    }

    private void pauseGame() {
        isPaused = true;
        pauseButton.setText("Resume");
        disableGameControls(true);
    }

    private void resumeGame() {
        isPaused = false;
        pauseButton.setText("Pause");
        disableGameControls(false);
    }

    private void disableGameControls(boolean disable) {
        inputContainer.setDisable(disable);
        guessButton.setDisable(disable);
    }

    private void updateTimerLabel(){
        int minutes = secondsElapsed / 60;
        int seconds = secondsElapsed % 60;
        timerLabel.setText(String.format("%d:%02d", minutes, seconds));
    }

    private void stopTimer() {
        if (timer != null) {
            timer.stop();
        }
    }

    private void startTimer() {
        timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            if (!isPaused) {
                secondsElapsed++;
                updateTimerLabel();
            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    public void startGame(String difficulty) {
         currentDifficulty = difficulty;
        switch (difficulty) {
            case "Easy":
                maxAttempts = 12;
                codeLength = 4;
                allowDuplicates = false;
                infoLabel.setText("Level Easy: 12 attempts, no duplicates");
                break;
            case "Medium":
                maxAttempts = 10;
                codeLength = 5;
                allowDuplicates = true;
                infoLabel.setText("Level Medium: 10 attempts, duplicates allowed");
                break;
            case "Hard":
                maxAttempts = 8;
                codeLength = 6;
                allowDuplicates = true;
                infoLabel.setText("Level Hard: 8 attempts, duplicates allowed");
                break;
            default:
                System.out.println("Unknown difficulty level!");
                return;
        }
        generateSecretCode();
        startTimer();
        updateUIForDifficulty();
    }

    private void generateSecretCode() {
        Random random = new Random();
        secretCode = new ArrayList<>();
        for (int i = 0; i < codeLength; i++) {
            int digit = random.nextInt(10);
            if (!allowDuplicates && secretCode.contains(digit)) {
                i--;
            } else {
                secretCode.add(digit);
            }
        }
        System.out.println("Secret Code: " + secretCode);
    }

    private void updateUIForDifficulty() {
        inputContainer.getChildren().clear();
        for (int i = 0; i < codeLength; i++) {
            TextField inputField = new TextField();
            inputField.setPromptText("Number" + (i + 1));
            inputField.setPrefWidth(50);
            inputContainer.getChildren().add(inputField);
        }
    }

    private boolean validateUserInput(List<Integer> userInput, String difficulty) {
        int requiredLength;
        switch (difficulty) {
            case "Easy":
                requiredLength = 4;
                break;
            case "Medium":
                requiredLength = 5;
                break;
            case "Hard":
                requiredLength = 6;
                break;
            default:
                System.out.println("Unknown difficulty level!");
                return false;
        }

        if (userInput.size() != requiredLength) {
            System.out.println("Invalid input length. Expected " + requiredLength + " numbers.");
            return false;
        }

        for (Integer number : userInput) {
            if (number < 0 || number > 9) {
                System.out.println("Numbers must be between 0 and 9.");
                return false;
            }
        }

        if (difficulty.equals("Easy")) {
            Set<Integer> uniqueNumbers = new HashSet<>(userInput);
            if (uniqueNumbers.size() != userInput.size()) {
                System.out.println("No duplicate numbers allowed on Easy level!");
                return false;
            }
        }
        return true;
    }

    @FXML
    private void checkGuess(ActionEvent event) {
        List<Integer> userInput = new ArrayList<>();
        for (Node node : inputContainer.getChildren()) {
            if (node instanceof TextField) {
                String text = ((TextField) node).getText();
                try {
                    userInput.add(Integer.parseInt(text));
                } catch (NumberFormatException e) {
                    infoLabel.setText("Please enter valid numbers!");
                    return;
                }
            }
        }
        if (!validateUserInput(userInput, currentDifficulty)) {
            infoLabel.setText("Please enter valid numbers!");
            return;
        }
        attempts++;

        if (userInput.equals(secretCode)) {
            infoLabel.setText("Congratulations! You guessed the secret code.");
            guessButton.setVisible(false);
            stopTimer();
            hidePauseButton();
        } else {
            infoLabel.setText("Incorrect guess. Attempts left: " + (maxAttempts - attempts));
        }
        historyList.getItems().add("Guess: " + userInput + " - " + (userInput.equals(secretCode) ? "Correct!" : "Wrong!"));

        if (attempts >= maxAttempts) {
            infoLabel.setText("No more attempts left! The secret code was: " + secretCode);
            guessButton.setVisible(false);
            stopTimer();
            hidePauseButton();
        }
    }

    @FXML
    public void goBackToMenu(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 520, 440);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setWidth(520);
            stage.setHeight(440);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}