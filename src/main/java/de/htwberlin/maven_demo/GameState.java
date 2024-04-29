package de.htwberlin.maven_demo;

import de.htwberlin.maven_demo.model.Card;
import de.htwberlin.maven_demo.model.Deck;
import de.htwberlin.maven_demo.model.Player;


import java.util.List;

public class GameState {
    private List<Player> players;
    private Deck deck;
    private List<Card> discardPile;
    private int currentPlayerIndex;
    private boolean changedDirection;
    private Card lastCardPlayed;
    private Card wishCard;

    public Card getLastCardPlayed() {
        return lastCardPlayed;
    }

    public void setLastCardPlayed(Card lastCardPlayed) {
        this.lastCardPlayed = lastCardPlayed;
    }

    public Card getWishCard() {
        return wishCard;
    }

    public void setWishCard(Card wishCard) {
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

    public void setDiscardPile(List<Card> discardPile) {
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
