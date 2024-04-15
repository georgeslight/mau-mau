package de.htwberlin.maven_demo;

import de.htwberlin.maven_demo.model.Card;
import de.htwberlin.maven_demo.model.Player;

import java.util.List;
import java.util.Stack;

public class GameState {
    private List<Player> players;
    private Stack<Card> deck;
    private Stack<Card> discardPile;
    private int currentPlayerIndex;
    private boolean isReversed;


    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Stack<Card> getDeck() {
        return deck;
    }

    public void setDeck(Stack<Card> deck) {
        this.deck = deck;
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

    public boolean isReversed() {
        return isReversed;
    }

    public void setReversed(boolean reversed) {
        isReversed = reversed;
    }
}
