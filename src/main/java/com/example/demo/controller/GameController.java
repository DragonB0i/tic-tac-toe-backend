package com.example.demo.controller;

import com.example.demo.model.Score;
import com.example.demo.repository.ScoreRepository;
import com.example.demo.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/game")
public class GameController {

    @Autowired
    private GameService gameService;

    @Autowired
    private ScoreRepository scoreRepository;

    /**
     * NOW ACCEPTS Character[][] TO HANDLE NULLS
     */
    @PostMapping("/play")
    public Map<String, String> playTurn(@RequestBody Character[][] board) {
        char result = gameService.checkWinner(board);
        String message;

        if (result == 'X' || result == 'O') {
            gameService.updateScore(result);
            message = "Player " + result + " has won!";
        } else if (result == 'D') {
            message = "It's a draw!";
        } else {
            message = "Game is still in progress.";
        }
        return Map.of("status", message);
    }

    @GetMapping("/scores")
    public List<Score> getScores() {
        return scoreRepository.findAll();
    }

    /**
     * NOW ACCEPTS Character[][] TO HANDLE NULLS
     */
    @PostMapping("/bot-move")
    public Map<String, Integer> getBotMove(@RequestBody Character[][] board) {
        int bestMove = gameService.findBestMove(board);
        return Map.of("move", bestMove);
    }
}

