package com.example.snakegame;

public class PlayerModel {

    private int id;
    private int score;

    public PlayerModel(int id, int score) {
        this.id = id;
        this.score = score;
    }

    @Override
    public String toString() {
        return  "player = " + id +
                ", score = " + score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
