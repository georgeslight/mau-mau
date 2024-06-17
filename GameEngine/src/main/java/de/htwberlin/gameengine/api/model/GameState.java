package de.htwberlin.gameengine.api.model;

import de.htwberlin.cardmanagement.api.model.Card;
import de.htwberlin.playermanagement.api.model.Player;
import de.htwberlin.rulesmanagement.api.model.Rules;

import java.util.List;
import java.util.Stack;

public class GameState {
    private List<Player> players;
    private Stack<Card> deck;
    private Stack<Card> discardPile;
    // change to Player?
    private int currentPlayerIndex;
    // change to Player?
//    private int nextPlayerIndex;

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

//    public int getNextPlayerIndex() {
//        return nextPlayerIndex;
//    }
//
//    public void setNextPlayerIndex(int nextPlayerIndex) {
//        this.nextPlayerIndex = nextPlayerIndex;
//    }
}
