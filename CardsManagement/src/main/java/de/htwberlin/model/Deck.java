package de.htwberlin.model;

import de.htwberlin.enums.Rank;
import de.htwberlin.enums.Suit;

import java.util.Collections;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Deck {

    private Stack<Card> cards = new Stack<>();

    public Deck() {
        cards.addAll(Stream.of(Suit.values())
                .flatMap(suit -> Stream.of(Rank.values())
                        .map(rank -> new Card(suit, rank)))
                .collect(Collectors.toList()));
        Collections.shuffle(cards);
    }

    public Stack<Card> getCards() {
        return cards;
    }
}
