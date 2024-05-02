package com.example.recuerdate.jocMemoria.model;

public class GameModel {
    private int score;

    public GameModel() {
        score = 0;
    }

    public GameModel(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score += score;
    }
}
