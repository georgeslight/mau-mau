package de.htwberlin.service;

import de.htwberlin.enums.Rank;
import de.htwberlin.enums.Suit;
import de.htwberlin.model.Card;
import de.htwberlin.model.Deck;

import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CardService implements CardManagerInterface {
    @Override
    public Stack<Card> shuffle(Stack<Card> deck) {
        //        todo
        return null;
    }

    @Override
    public Card createCard(Suit suit, Rank rank) {
//        todo
        return null;
    }

    @Override
    public Deck createDeck() {
//        return new Deck(Stream.of(Suit.values())
//                .flatMap(suit -> Stream.of(Rank.values())
//                        .map(rank -> new Card(suit, rank)))
//                .collect(Collectors.toCollection(Stack::new)));
        return null;
    }
}
