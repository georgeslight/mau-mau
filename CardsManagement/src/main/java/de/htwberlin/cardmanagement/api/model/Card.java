package de.htwberlin.cardmanagement.api.model;

import de.htwberlin.cardmanagement.api.enums.Rank;
import de.htwberlin.cardmanagement.api.enums.Suit;

import java.util.Objects;

public class Card {
    private final Suit suit;
    private final Rank rank;

    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public Suit getSuit() {
        return this.suit;
    }

    public Rank getRank() {
        return this.rank;
    }

    @Override
    public String toString() {
        return this.rankToString() + this.suitToSymbol();
    }

    private String rankToString() {
        switch (rank) {
            case SEVEN: return "7";
            case EIGHT: return "8";
            case NINE: return "9";
            case TEN: return "10";
            case JACK: return "J";
            case QUEEN: return "Q";
            case KING: return "K";
            case ACE: return "A";
            default: throw new IllegalArgumentException("Unknown rank: " + rank);
        }
    }

    private String suitToSymbol() {
        switch (suit) {
            case HEARTS: return "♥";
            case DIAMONDS: return "♦";
            case CLUBS: return "♣";
            case SPADES: return "♠";
            default: throw new IllegalArgumentException("Unknown suit: " + suit);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return suit == card.suit && rank == card.rank;
    }

    @Override
    public int hashCode() {
        return Objects.hash(suit, rank);
    }
}
