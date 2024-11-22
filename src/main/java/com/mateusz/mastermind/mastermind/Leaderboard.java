package com.mateusz.mastermind.mastermind;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Leaderboard {

    private static final String FILE_PATH = "src/main/resources/data/leaderboard.csv";

    public static void saveScore(String playerName, int attempts, String time, String difficulty, String gameMode) {
        try {
            File file = new File(FILE_PATH);

            if (!file.exists() || file.length() == 0) {
                try (FileWriter writer = new FileWriter(file, true)) {
                    writer.append("Player,Attempts,Time,Difficulty,Game Mode\n");
                }
            }

            try (FileWriter writer = new FileWriter(file, true)) {
                writer.append(playerName).append(",")
                        .append(String.valueOf(attempts)).append(",")
                        .append(time).append(",")
                        .append(difficulty).append(",")
                        .append(gameMode).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String[]> readScores() {
        List<String[]> scores = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            boolean isHeader = true;
            while ((line = reader.readLine()) != null) {
                if (isHeader) { // Pomijanie nagłówka
                    isHeader = false;
                    continue;
                }
                String[] row = line.split(",");
                if (row.length == 5) { // Sprawdzenie liczby kolumn
                    scores.add(row);
                } else {
                    System.err.println("Invalid row format: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return scores;
    }
}