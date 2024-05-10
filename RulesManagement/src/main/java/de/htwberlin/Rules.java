package de.htwberlin;

import de.htwberlin.enums.Suit;
import de.htwberlin.model.Card;

public class Rules {
    private boolean gameDirection;
    private Card lastCardPlayed;
    private Suit wishCard;
    //speichert wieviel cards vom nächsten spieler gezogen werden müssen
    private int cardsTObeDrawn;
    //wenn true, muss nächste Spieler eine Runde Aussetzen
    private boolean skipNextPlayerTurn;
    //When Ace played, Player is allowed to play another card
    private boolean canPlayAgain;



    public Rules() {}

    public boolean isGameDirection() {
        return gameDirection;
    }

    public void setGameDirection(boolean gameDirection) {
        this.gameDirection = gameDirection;
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

    public int getCardsTObeDrawn() {
        return cardsTObeDrawn;
    }

    public void setCardsTObeDrawn(int cardsTObeDrawn) {
        this.cardsTObeDrawn = cardsTObeDrawn;
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
