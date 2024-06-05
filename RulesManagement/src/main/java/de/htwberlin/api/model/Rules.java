package de.htwberlin.api.model;

import de.htwberlin.api.enums.Suit;

public class Rules {
    // gameDirection: true = clockwise, false = counter-clockwise
    private boolean gameDirection;
    private Card lastCardPlayed;
    private Suit wishCard;
    // number of cards to be drawn by the next player
    private int cardsToBeDrawn;
    private boolean skipNextPlayerTurn;
    //When Ace played, Player is allowed to play another card
    private boolean canPlayAgain;

    public void setCardsTObeDrawn(Integer cardsTObeDrawn) {
        this.cardsToBeDrawn = cardsTObeDrawn;
    }

    public Rules() {}

    public boolean isGameDirection() {
        return gameDirection;
    }

    public void setGameDirection(boolean gameDirection) {
        this.gameDirection = gameDirection;
    }

    public Integer getCardsToBeDrawn() {
        return cardsToBeDrawn;
    }

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

    public boolean isSkipNextPlayerTurn() {
        return skipNextPlayerTurn;
    }

    public void setSkipNextPlayerTurn(boolean skipNextPlayerTurn) {
        this.skipNextPlayerTurn = skipNextPlayerTurn;
    }

    public boolean isCanPlayAgain() {
        return canPlayAgain;
    }

    public void setCanPlayAgain(boolean canPlayAgain) {
        this.canPlayAgain = canPlayAgain;
    }
}
