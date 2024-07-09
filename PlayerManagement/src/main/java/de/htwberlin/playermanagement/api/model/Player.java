package de.htwberlin.playermanagement.api.model;

import de.htwberlin.cardmanagement.api.model.Card;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @OneToMany
    @JoinColumn(name = "player_id") // specifies the foreign key in the Card table
    private List<Card> hand;
    private Integer rankingPoints;
    private boolean saidMau;
    @ElementCollection
    private List<Integer> score;

    public Player(String name, List<Card> hand) {
        this.score = new ArrayList<>();
        this.saidMau = false;
        this.rankingPoints = 0;
        this.hand = hand;
        this.name = name;
    }

    public Player() {
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
        this.score = new ArrayList<>(score);
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
