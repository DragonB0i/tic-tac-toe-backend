package com.example.demo.model;

public class Game {

    private char[][] board;
    private String currentPlayer;
    private GameState state;

    public Game() {
        this.board = new char[3][3];
        this.currentPlayer = "X";
        this.state = GameState.IN_PROGRESS;
    }

    // Getters and Setters
    public char[][] getBoard() { return board; }
    public void setBoard(char[][] board) { this.board = board; }
    public String getCurrentPlayer() { return currentPlayer; }
    public void setCurrentPlayer(String currentPlayer) { this.currentPlayer = currentPlayer; }
    public GameState getState() { return state; }
    public void setState(GameState state) { this.state = state; }
}