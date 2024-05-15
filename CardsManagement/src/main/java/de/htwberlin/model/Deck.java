package de.htwberlin.model;

import de.htwberlin.enums.Rank;
import de.htwberlin.enums.Suit;

import java.util.Collections;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Deck {

    private final Stack<Card> cards;

    public Deck() {
        this.cards = Stream.of(Suit.values())
                .flatMap(suit -> Stream.of(Rank.values())
                        .map(rank -> new Card(suit, rank)))
                .collect(Collectors.toCollection(Stack::new));
    }

    public Stack<Card> getCards() {
        return cards;
    }
}
