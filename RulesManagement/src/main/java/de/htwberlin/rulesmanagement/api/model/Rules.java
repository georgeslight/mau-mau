package de.htwberlin.rulesmanagement.api.model;

import de.htwberlin.cardmanagement.api.enums.Suit;
import de.htwberlin.cardmanagement.api.model.Card;

public class Rules {

    private Card lastCardPlayed;
    private Suit wishCard;
    private int cardsToBeDrawn = 0;
    private boolean skipNextPlayerTurn;
    private boolean canPlayAgain;

    public void setCardsTObeDrawn(Integer cardsTObeDrawn) {
        this.cardsToBeDrawn = cardsTObeDrawn;
    }

    public Rules() {}

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
