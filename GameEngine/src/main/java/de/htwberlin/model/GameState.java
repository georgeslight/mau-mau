package de.htwberlin.model;

import de.htwberlin.enums.Suit;

import java.util.List;
import java.util.Stack;

public class GameState {
    private List<Player> players;
    private Deck deck;
    private Stack<Card> discardPile;
    private int currentPlayerIndex;
    private boolean changedDirection;
    private Card lastCardPlayed;
    private Suit wishCard;

    public Card getLastCardPlayed() {
        return lastCardPlayed;
    }

    public void setLastCardPlayed(Card lastCardPlayed) {
        this.lastCardPlayed = lastCardPlayed;
    }

    public Suit getWishCard() {
        return wishCard;
    }

    public void setWishCard(Suit wishCard) {
        this.wishCard = wishCard;
    }

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

    public boolean isChangedDirection() {
        return changedDirection;
    }

    public void setChangedDirection(boolean changedDirection) {
        this.changedDirection = changedDirection;
    }
}
