package com.mateusz.mastermind.mastermind;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class LeaderboardController {

    @FXML
    private TableView<Score> leaderboardTable;
    @FXML
    private TableColumn<Score, String> playerColumn;
    @FXML
    private TableColumn<Score, Integer> attemptsColumn;
    @FXML
    private TableColumn<Score, String> timeColumn;
    @FXML
    private TableColumn<Score, String> difficultyColumn;
    @FXML
    private  TableColumn<Score, String> gameModeColumn;

    @FXML
    public void initialize() {
        playerColumn.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        attemptsColumn.setCellValueFactory(new PropertyValueFactory<>("attempts"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        difficultyColumn.setCellValueFactory(new PropertyValueFactory<>("difficulty"));
        gameModeColumn.setCellValueFactory(new PropertyValueFactory<>("gameMode"));

        ObservableList<Score> scores = loadScores();
        if (scores.isEmpty()) {
            System.out.println("Leaderboard is empty or could not load scores.");
        }
        leaderboardTable.setItems(scores);
    }

    @FXML
    public void goBackToMenu(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ObservableList<Score> loadScores() {
        ObservableList<Score> scores = FXCollections.observableArrayList();
        List<String[]> rawScores = Leaderboard.readScores();

        if (rawScores.isEmpty()) {
            System.out.println("No scores found in the leaderboard.");
            return scores;
        }

        for (String[] row : rawScores) {
            try {
                if (row.length == 5) {
                    scores.add(new Score(row[0], Integer.parseInt(row[1]), row[2], row[3], row[4]));
                } else {
                    System.err.println("Skipped invalid row: " + Arrays.toString(row));
                }
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
        return scores;
    }
}

