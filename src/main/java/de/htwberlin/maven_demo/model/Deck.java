package de.htwberlin.maven_demo.model;

import de.htwberlin.maven_demo.enums.Rank;
import de.htwberlin.maven_demo.enums.Suit;

import java.util.Collections;
import java.util.Stack;
import java.util.stream.Stream;

public class Deck {

    private Stack<Card> cards = new Stack<>();

    public Deck() {
        cards.addAll(Stream.of(Suit.values())
                .flatMap(suit -> Stream.of(Rank.values())
                        .map(rank -> new Card(suit, rank)))
                .toList());
        Collections.shuffle(cards);
    }

    public Stack<Card> getCards() {
        return cards;
    }
}
