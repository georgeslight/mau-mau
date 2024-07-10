package de.htwberlin.rulesmanagement.api.model;

import de.htwberlin.cardsmanagement.api.enums.Suit;
import jakarta.persistence.*;

@Entity
public class Rules {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Enumerated(EnumType.STRING)
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

    public Long getId() {
        return id;
    }
}
