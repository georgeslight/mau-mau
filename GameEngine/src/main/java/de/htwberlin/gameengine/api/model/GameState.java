package de.htwberlin.gameengine.api.model;

import de.htwberlin.cardsmanagement.api.model.Card;
import de.htwberlin.playermanagement.api.model.Player;
import de.htwberlin.rulesmanagement.api.model.Rules;
import jakarta.persistence.*;

import java.util.List;
import java.util.Stack;

@Entity
public class GameState {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToMany
    @JoinColumn(name = "game_id") // specifies the foreign key in the Player table
    private List<Player> players;
    @OneToMany
    @JoinColumn(name = "deck_game_id") // specifies the foreign key in the Card table
    private Stack<Card> deck;
    @OneToMany
    @JoinColumn(name = "discard_game_id") // specifies the foreign key in the Card table
    private Stack<Card> discardPile;
    private int currentPlayerIndex;
    @OneToOne
    @JoinColumn(name = "rules_id") // Specifies the foreign key in the GameState table
    private Rules rules;
    private boolean gameRunning;

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void setDeck(Stack<Card> deck) {
        this.deck = deck;
    }

    public Stack<Card> getDeck() {
        return deck;
    }

    public Stack<Card> getDiscardPile() {
        return discardPile;
    }

    public void setDiscardPile(Stack<Card> discardPile) {
        this.discardPile = discardPile;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }

    public Rules getRules() {
        return rules;
    }

    public void setRules(Rules rules) {
        this.rules = rules;
    }

    public boolean isGameRunning() {
        return gameRunning;
    }

    public void setGameRunning(boolean gameRunning) {
        this.gameRunning = gameRunning;
    }
}
