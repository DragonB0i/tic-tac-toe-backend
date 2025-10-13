package com.example.demo.service;

import com.example.demo.model.Score;
import com.example.demo.repository.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GameService {

    @Autowired
    private ScoreRepository scoreRepository;

    private static final char PLAYER_X = 'X';
    private static final char PLAYER_O = 'O';

    // This is a helper method to safely convert the board from the frontend
    // (which can have nulls) to the board our game logic uses (which can't).
    private char[][] convertToPrimitive(Character[][] source) {
        char[][] dest = new char[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                // If the input is null, we use the 'empty' character '\0'. Otherwise, we use the character itself.
                dest[i][j] = (source[i][j] == null) ? '\0' : source[i][j];
            }
        }
        return dest;
    }

    /**
     * This is the public method called by the GameController to check for a winner.
     * It accepts the nullable Character array.
     */
    public char checkWinner(Character[][] nullableBoard) {
        char[][] board = convertToPrimitive(nullableBoard);
        return checkWinnerInternal(board); // Call the internal logic with the safe, primitive board
    }

    /**
     * This is the public method called by the GameController for the bot's move.
     * It accepts the nullable Character array.
     */
    public int findBestMove(Character[][] nullableBoard) {
        char[][] board = convertToPrimitive(nullableBoard);
        int bestVal = -1000;
        int bestMoveRow = -1;
        int bestMoveCol = -1;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == '\0') {
                    board[i][j] = PLAYER_O;
                    int moveVal = minimax(board, 0, false);
                    board[i][j] = '\0';

                    if (moveVal > bestVal) {
                        bestMoveRow = i;
                        bestMoveCol = j;
                        bestVal = moveVal;
                    }
                }
            }
        }
        // Return the best move as a single index (0-8)
        return (bestMoveRow * 3) + bestMoveCol;
    }

    /**
     * The recursive Minimax algorithm. This is the bot's "brain".
     */
    private int minimax(char[][] board, int depth, boolean isMax) {
        char winnerResult = checkWinnerInternal(board);

        if (winnerResult == PLAYER_O) return 10 - depth; // Bot wins
        if (winnerResult == PLAYER_X) return depth - 10; // Player wins
        if (winnerResult == 'D') return 0; // Draw

        if (isMax) { // Bot's (Maximizer) turn
            int best = -1000;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == '\0') {
                        board[i][j] = PLAYER_O;
                        best = Math.max(best, minimax(board, depth + 1, false));
                        board[i][j] = '\0';
                    }
                }
            }
            return best;
        } else { // Player's (Minimizer) turn
            int best = 1000;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == '\0') {
                        board[i][j] = PLAYER_X;
                        best = Math.min(best, minimax(board, depth + 1, true));
                        board[i][j] = '\0';
                    }
                }
            }
            return best;
        }
    }

    /**
     * This is a private, internal version of checkWinner used by the minimax algorithm.
     * It works with the primitive char[][] array.
     */
    private char checkWinnerInternal(char[][] board) {
        // Check rows
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != '\0' && board[i][0] == board[i][1] && board[i][1] == board[i][2]) return board[i][0];
        }
        // Check columns
        for (int i = 0; i < 3; i++) {
            if (board[0][i] != '\0' && board[0][i] == board[1][i] && board[1][i] == board[2][i]) return board[0][i];
        }
        // Check diagonals
        if (board[0][0] != '\0' && board[0][0] == board[1][1] && board[1][1] == board[2][2]) return board[0][0];
        if (board[0][2] != '\0' && board[0][2] == board[1][1] && board[1][1] == board[2][0]) return board[0][2];

        // Check for draw
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == '\0') return 'P'; // Game still in progress
            }
        }
        return 'D'; // It's a draw
    }

    /**
     * Updates the score in the database. This method remains unchanged.
     */
    @Transactional
    public void updateScore(char winner) {
        if (winner != 'X' && winner != 'O') {
            return;
        }

        String teamName = String.valueOf(winner);
        Score score = scoreRepository.findById(teamName).orElse(new Score());
        score.setTeamName(teamName);
        score.setWins(score.getWins() + 1);
        scoreRepository.save(score);
    }
}

