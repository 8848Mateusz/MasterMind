package com.mateusz.mastermind.mastermind;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.*;
import java.util.stream.Collectors;

public class GameController {

    private Timeline timer;
    private int secondsElapsed = 0;
    private boolean isPaused = false;
    private int attempts = 0;
    private int correctNumbers = 0;
    private int correctPositions = 0;
    private int maxAttempts;
    private int codeLength;
    private boolean allowDuplicates;
    private List<String> secretCode;
    private String playerName;
    private String gameMode;

    private final Map<String, String> colorToIconMap = Map.of(
            "Red", "\uD83D\uDD34",
            "Blue", "\uD83D\uDD35",
            "Green", "\uD83D\uDFE2",
            "Yellow", "\uD83D\uDFE1",
            "Orange", "\uD83D\uDFE0",
            "Purple", "\uD83D\uDFE3"
    );

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    @FXML
    private String currentDifficulty;

    @FXML
    private Button guessButton;

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

    private void updateTimerLabel() {
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

    public void startGame(String difficulty, String gameMode) {
        if (gameMode == null) {
            infoLabel.setText("Error: Game mode is not set!");
            return;
        }

        currentDifficulty = difficulty;
        switch (difficulty) {
            case "Easy":
                maxAttempts = 12;
                codeLength = 4;
                allowDuplicates = false;
                break;
            case "Medium":
                maxAttempts = 10;
                codeLength = 5;
                allowDuplicates = true;
                break;
            case "Hard":
                maxAttempts = 8;
                codeLength = 6;
                allowDuplicates = true;
                break;
            default:
                System.out.println("Unknown difficulty level!");
                return;
        }
        infoLabel.setText(gameMode.equals("Numbers")
                ? "Guess the " + codeLength + "-digit code (numbers)"
                : "Guess the " + codeLength + "-color sequence");
        generateSecretCode();
        startTimer();
        updateUIForDifficulty();
    }

    private void generateSecretCode() {
        Random random = new Random();
        secretCode = new ArrayList<>();
        if (gameMode.equals("Numbers")) {
            for (int i = 0; i < codeLength; i++) {
                String digit = String.valueOf(random.nextInt(10));
                if (!allowDuplicates && secretCode.contains(digit)) {
                    i--;
                } else {
                    secretCode.add(digit);
                }
            }
        } else if (gameMode.equals("Colors")) {
            String[] colors = {"Red", "Blue", "Green", "Yellow", "Orange", "Purple"};
            for (int i = 0; i < codeLength; i++) {
                String color = colors[random.nextInt(colors.length)];
                if (!allowDuplicates && secretCode.contains(color)) {
                    i--;
                } else {
                    secretCode.add(color);
                }
            }
        }
        System.out.println("Secret Code: " + secretCode);
    }

    private void updateUIForDifficulty() {
        inputContainer.getChildren().clear();
        if (gameMode.equals("Colors")) {
            for (int i = 0; i < codeLength; i++) {
                ComboBox<String> colorBox = new ComboBox<>();
                colorBox.getItems().addAll("Red", "Blue", "Green", "Yellow", "Orange", "Purple");
                colorBox.setPromptText("Color " + (i + 1));
                colorBox.setCellFactory(lv -> new ListCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            setGraphic(createColorIcon(item));
                            setText(null);
                        }
                    }
                });

                colorBox.setButtonCell(new ListCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            setGraphic(createColorIcon(item));
                            setText(null);
                        }
                    }
                });
                inputContainer.getChildren().add(colorBox);
            }
        } else {
            for (int i = 0; i < codeLength; i++) {
                TextField inputField = new TextField();
                inputField.setPromptText("Number " + (i + 1));
                inputField.setPrefWidth(50);
                inputContainer.getChildren().add(inputField);
            }
        }
    }

    private boolean validateUserInput(List<String> userInput) {
        if (userInput.size() != codeLength) {
            return false;
        }
        if (gameMode.equals("Numbers")) {
            try {
                for (String input : userInput) {
                    int number = Integer.parseInt(input);
                    if (number < 0 || number > 9) {
                        return false;
                    }
                }
            } catch (NumberFormatException e) {
                return false;
            }
        } else if (gameMode.equals("Colors")) {
            Set<String> validColors = Set.of("Red", "Blue", "Green", "Yellow", "Orange", "Purple");
            for (String input : userInput) {
                if (!validColors.contains(input)) {
                    return false;
                }
            }
        }
        return true;
    }

    @FXML
    private void checkGuess(ActionEvent event) {
        List<String> userInput = new ArrayList<>();
        for (Node node : inputContainer.getChildren()) {
            if (node instanceof TextField) {
                userInput.add(((TextField) node).getText());
            } else if (node instanceof ComboBox) {
                ComboBox<String> comboBox = (ComboBox<String>) node;
                String value = comboBox.getValue();
                if (value == null) {
                    infoLabel.setText("Please select all colors!");
                    return;
                }
                userInput.add(value);
            }
        }

        if (!validateUserInput(userInput)) {
            infoLabel.setText("Invalid input. Please try again.");
            return;
        }

        List<String> tempSecretCode = new ArrayList<>(secretCode);
        List<String> tempUserInput = new ArrayList<>(userInput);

        correctPositions = 0;
        correctNumbers = 0;

        for (int i = 0; i < tempUserInput.size(); i++) {
            if (tempUserInput.get(i).equals(tempSecretCode.get(i))) {
                correctPositions++;
                tempSecretCode.set(i, null);
                tempUserInput.set(i, null);
            }
        }

        for (String value : tempUserInput) {
            if (value != null && tempSecretCode.contains(value)) {
                correctNumbers++;
                tempSecretCode.set(tempSecretCode.indexOf(value), null);
            }
        }

        List<String> guessIcons = userInput.stream()
                .map(value -> colorToIconMap.getOrDefault(value, value))
                .collect(Collectors.toList());

        String guessResult = "Guess: " + String.join(" ", guessIcons) +
                " | Correct Positions: " + correctPositions +
                ", Correct Matches: " + correctNumbers;
        historyList.getItems().add(guessResult);

        attempts++;
        if (userInput.equals(secretCode)) {
            endGame(true);
        } else if (attempts >= maxAttempts) {
            endGame(false);
        } else {
            infoLabel.setText("Incorrect guess. Attempts left: " + (maxAttempts - attempts));
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

    private void endGame(boolean isWin) {
        stopTimer();
        String time = String.format("%02d:%02d", secondsElapsed / 60, secondsElapsed % 60);
        Leaderboard.saveScore(playerName, attempts, time, currentDifficulty, gameMode);

        List<String> codeIcons = secretCode.stream()
                .map(value -> colorToIconMap.getOrDefault(value, value))
                .collect(Collectors.toList());

        if (isWin) {
            infoLabel.setText("Congratulations, " + playerName + "! You guessed the secret code.");
        } else {
            infoLabel.setText("Game over! The code was: " + String.join(" ", codeIcons));
        }
        guessButton.setVisible(false);
        hidePauseButton();
    }

    private Node createColorIcon(String color) {
        Circle circle = new Circle(15); // Promień 15
        switch (color) {
            case "Red" -> circle.setFill(Color.RED);
            case "Blue" -> circle.setFill(Color.BLUE);
            case "Green" -> circle.setFill(Color.GREEN);
            case "Yellow" -> circle.setFill(Color.YELLOW);
            case "Orange" -> circle.setFill(Color.ORANGE);
            case "Purple" -> circle.setFill(Color.PURPLE);
            default -> circle.setFill(Color.GRAY);
        }
        return circle;
    }
}