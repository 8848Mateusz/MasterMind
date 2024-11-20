package com.mateusz.mastermind.mastermind;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Leaderboard {

    private static final String FILE_PATH = "src/main/resources/data/leaderboard.csv";

    public static void saveScore(String playerName, int attempts, String time, String difficulty) {
        try (FileWriter writer = new FileWriter(FILE_PATH, true)) {
            writer.append(playerName).append(",")
                    .append(String.valueOf(attempts)).append(",")
                    .append(time).append(",")
                    .append(difficulty).append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String[]> readScores() {
        List<String[]> scores = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith("Player")) {
                    String[] row = line.split(",");
                    if (row.length == 4) { // Sprawdzenie, czy wiersz ma dokładnie 4 kolumny
                        scores.add(row);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return scores;
    }
}