package com.example.snakegame;

public class PlayerModel {

    private int id;
    private int score;
    private String username;

    public PlayerModel(int id, int score, String username) {
        this.id = id;
        this.score = score;
        this.username = username;
    }

//        public PlayerModel(int id, int score) {
//        this.id = id;
//        this.score = score;
//    }

    @Override
    public String toString() {
        return  "player = " + username +
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

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }
}
