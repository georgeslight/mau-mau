package de.htwberlin.api.model;

import java.util.List;

public class Player {

    private String name;
    private List<Card> hand;
    private Integer rankingPoints;
    private boolean saidMau;
    private List<Integer> score;

    public Player(String name, List<Card> hand) {
        this.score = new ArrayList<>();
        this.saidMau = false;
        this.rankingPoints = 0;
        this.hand = hand;
        this.name = name;
    }

    public List<Card> getHand() {
        return hand;
    }

    public Integer getRankingPoints() {
        return rankingPoints;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getScore() {
        return score;
    }

    public void setScore(List<Integer> score) {
        this.score = score;
    }

    public void setRankingPoints(Integer rankingPoints) {
        this.rankingPoints = rankingPoints;
    }

    public void setHand(List<Card> hand) {
        this.hand = hand;
    }

    public boolean isSaidMau() {
        return saidMau;
    }

    public void setSaidMau(boolean saidMau) {
        this.saidMau = saidMau;
    }
}
