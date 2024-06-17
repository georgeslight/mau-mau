package de.htwberlin.cardmanagement.impl;

import de.htwberlin.cardmanagement.api.enums.Rank;
import de.htwberlin.cardmanagement.api.enums.Suit;
import de.htwberlin.cardmanagement.api.model.Card;
import de.htwberlin.cardmanagement.api.service.CardManagerInterface;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Stack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CardService implements CardManagerInterface {

    private static final Logger LOGGER = LogManager.getLogger(CardService.class);

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
    public Stack<Card> createDeck() {
        return Stream.of(Suit.values())
                .flatMap(suit -> Stream.of(Rank.values())
                        .map(rank -> this.createCard(suit, rank)))
                .collect(Collectors.toCollection(Stack::new));
    }
}
