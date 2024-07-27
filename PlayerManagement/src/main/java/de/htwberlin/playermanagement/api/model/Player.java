package de.htwberlin.playermanagement.api.model;

import de.htwberlin.cardsmanagement.api.model.Card;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "player_id") // specifies the foreign key in the Card table
    private List<Card> hand;
    private Integer rankingPoints;
    private boolean saidMau;
    @ElementCollection
    private List<Integer> score;
    private boolean isVirtual;

    public Player(String name, List<Card> hand, boolean isVirtual) {
        this.score = new ArrayList<>();
        this.saidMau = false;
        this.rankingPoints = 0;
        this.hand = hand;
        this.name = name;
        this.isVirtual = isVirtual;
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

    public boolean isVirtual() {
        return isVirtual;
    }

    public void setVirtual(boolean virtual) {
        isVirtual = virtual;
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(id, player.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
