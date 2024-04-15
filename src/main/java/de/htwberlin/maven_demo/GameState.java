package de.htwberlin.maven_demo;

import de.htwberlin.maven_demo.model.Deck;
import de.htwberlin.maven_demo.model.Player;

import java.util.List;

public class GameState {
    private List<Player> players;
    private Deck deck;
    private Deck discardPile;
    private int currentPlayerIndex;

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public Deck getDiscardPile() {
        return discardPile;
    }

    public void setDiscardPile(Deck discardPile) {
        this.discardPile = discardPile;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }
}
