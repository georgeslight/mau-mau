package de.htwberlin.model;

import de.htwberlin.enums.Suit;

import java.util.List;
import java.util.Stack;

public class GameState {
    private List<Player> players;
    private Deck deck;
    private Stack<Card> discardPile;
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

    public List<Card> getDiscardPile() {
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
}
