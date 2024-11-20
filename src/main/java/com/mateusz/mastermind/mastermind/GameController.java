package com.mateusz.mastermind.mastermind;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameController {

    @FXML
    private Button guessButton;

    @FXML
    private TextField inputField1, inputField2, inputField3, inputField4;

    @FXML
    private ListView<String> historyList;

    @FXML
    private Label infoLabel;

    private List<Integer> secretCode;

    @FXML
    public void initialize() {
        generateSecretCode();
    }

    private void generateSecretCode() {
        Random random = new Random();
        secretCode = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            secretCode.add(random.nextInt(10));
        }
        System.out.println("Secret Code: " + secretCode);
    }

    @FXML
    public void handleGuess() {
        try {
            int guess1 = Integer.parseInt(inputField1.getText());
            int guess2 = Integer.parseInt(inputField2.getText());
            int guess3 = Integer.parseInt(inputField3.getText());
            int guess4 = Integer.parseInt(inputField4.getText());

            List<Integer> guess = List.of(guess1, guess2, guess3, guess4);

            if (guess.stream().anyMatch(num -> num < 0 || num > 9)) {
                infoLabel.setText("Numbers must be between 0 and 9!");
                return;
            }

            int correctPositions = 0;
            int correctNumbers = 0;

            for (int i = 0; i < 4; i++) {
                if (guess.get(i).equals(secretCode.get(i))) {
                    correctPositions++;
                } else if (secretCode.contains(guess.get(i))) {
                    correctNumbers++;
                }
            }

            historyList.getItems().add(
                    "Guess: " + guess + " | Correct Positions: " + correctPositions + ", Correct Numbers: " + correctNumbers
            );

            if (correctPositions == 4) {
                infoLabel.setText("Congratulations! You've guessed the code!");
                guessButton.setVisible(false);
            } else {
                infoLabel.setText("Keep trying!");
            }

            inputField1.clear();
            inputField2.clear();
            inputField3.clear();
            inputField4.clear();

        } catch (NumberFormatException e) {
            infoLabel.setText("Please enter valid numbers!");
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