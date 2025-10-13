package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "score")
public class Score {

    @Id
    private String teamName;
    private long wins;

    public Score() {
    }

    // Getters and Setters
    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }
    public long getWins() { return wins; }
    public void setWins(long wins) { this.wins = wins; }
}