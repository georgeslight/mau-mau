package de.htwberlin.cardmanagement.api.enums;

public enum Suit {
    CLUBS,
    DIAMONDS,
    HEARTS,
    SPADES;

    @Override
    public String toString() {
        switch (this) {
            case CLUBS: return "♣";
            case DIAMONDS: return "♦";
            case HEARTS: return "♥";
            case SPADES: return "♠";
            default: throw new IllegalArgumentException("Unknown suit: " + this);
        }
    }
}


