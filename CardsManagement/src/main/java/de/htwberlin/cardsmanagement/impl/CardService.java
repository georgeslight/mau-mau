package de.htwberlin.cardsmanagement.impl;

import de.htwberlin.cardsmanagement.api.enums.Rank;
import de.htwberlin.cardsmanagement.api.enums.Suit;
import de.htwberlin.cardsmanagement.api.model.Card;
import de.htwberlin.cardsmanagement.api.service.CardManagerInterface;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CardService implements CardManagerInterface {

    private static final Logger LOGGER = LogManager.getLogger(CardService.class);

    @Override
    public List<Card> shuffle(List<Card> deck) {
        if (deck == null || deck.isEmpty()) {
            LOGGER.warn("Attempted to shuffle an empty or null deck.");
            throw new IllegalArgumentException("Cannot shuffle an empty or null deck");
        }
        return deck.stream()
                .collect(Collectors.collectingAndThen(Collectors.toList(), collected -> {
                    Collections.shuffle(collected);
                    return collected.stream();
                }))
                .collect(Collectors.toList());
    }

    @Override
    public Card createCard(Suit suit, Rank rank) {
        return new Card(suit, rank);
    }

    @Override
    public List<Card> createDeck() {
        return Stream.of(Suit.values())
                .flatMap(suit -> Stream.of(Rank.values())
                        .map(rank -> this.createCard(suit, rank)))
                .collect(Collectors.toList());
    }
}
