package de.htwberlin.model;

import java.util.Stack;

public class Deck {

    private final Stack<Card> cards;

    public Deck(Stack<Card> cards) {
        this.cards = cards;
    }

    public Stack<Card> getCards() {
        return cards;
    }
}
