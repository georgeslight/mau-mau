package de.htwberlin.impl.service;

import de.htwberlin.api.enums.Rank;
import de.htwberlin.api.enums.Suit;
import de.htwberlin.api.model.Card;
import de.htwberlin.api.model.Deck;
import de.htwberlin.api.service.CardManagerInterface;

import java.util.Collections;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CardService implements CardManagerInterface {
    @Override
    public Stack<Card> shuffle(Stack<Card> deck) {
        return deck.stream()
                .collect(Collectors.collectingAndThen(Collectors.toList(), collected -> {
                    Collections.shuffle(collected);
                    return collected.stream();
                }))
                .collect(Collectors.toCollection(Stack::new));
    }

    @Override
    public Card createCard(Suit suit, Rank rank) {
        return new Card(suit, rank);
    }

    @Override
    public Deck createDeck() {
        return new Deck(Stream.of(Suit.values())
                .flatMap(suit -> Stream.of(Rank.values())
                        .map(rank -> new Card(suit, rank)))
                .collect(Collectors.toCollection(Stack::new)));
    }
}
