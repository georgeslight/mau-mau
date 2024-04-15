package de.htwberlin.maven_demo.model;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private List<Card> hand = new ArrayList<>();
    private Integer rankingPoints;
    private boolean saidMau;
    private int[] score;


    public Player(List<Card> hand) {
        this.hand = hand;
    }

    public List<Card> getHand() {
        return hand;
    }

    public Integer getRankingPoints() {
        return rankingPoints;
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
