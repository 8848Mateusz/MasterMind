package com.mateusz.mastermind.mastermind;

public class Score {

    private String playerName;
    private int attempts;
    private String time;
    private String difficulty;
    private String gameMode;

    public Score(String playerName, int attempts, String time, String difficulty, String gameMode) {
        this.playerName = playerName;
        this.attempts = attempts;
        this.time = time;
        this.difficulty = difficulty;
        this.gameMode = gameMode;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getGameMode() {
        return gameMode;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }
}
