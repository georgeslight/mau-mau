package de.htwberlin.cardsmanagement;

import de.htwberlin.cardsmanagement.api.enums.Rank;
import de.htwberlin.cardsmanagement.api.enums.Suit;
import de.htwberlin.cardsmanagement.api.model.Card;
import de.htwberlin.cardsmanagement.impl.CardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CardServiceTest {

    private CardService cardService;

    @BeforeEach
    void setUp() {
        cardService = new CardService();
    }

    @Test
    void testShuffle() {
        // Create a deck of cards
        List<Card> deck = Stream.of(Suit.values())
                .flatMap(suit -> Stream.of(Rank.values())
                        .map(rank -> new Card(suit, rank)))
                        .collect(Collectors.toList());

        // Shuffle the deck
        List<Card> shuffleDeck = cardService.shuffle(deck);

        // Check if the shuffled deck has the same size as the original deck
        assertEquals(deck.size(), shuffleDeck.size());
        // Check that the order of cards in the shuffled deck is different from the original deck
        long samePositionCount = IntStream.range(0, deck.size())
                        .filter(i -> deck.get(i).equals(shuffleDeck.get(i)))
                                .count();
        // Ckeck that at most 3 cards are in the same position
        assertTrue(samePositionCount <= 3,"More than 3 cards are in the same position after shuffling");
//        IntStream.range(0, deck.size())
//                        .forEach(i -> assertNotEquals(deck.get(i), shuffleDeck.get(i)));
        // Check that the shuffled deck contains all the original cards
        assertTrue(shuffleDeck.containsAll(deck) && deck.containsAll(shuffleDeck));
    }

    @ParameterizedTest
    @MethodSource("suitAndRank")
    void testCreateCard(Suit suit, Rank rank) {
        Card card = cardService.createCard(suit, rank);
        assertNotNull(card);
        assertEquals(suit, card.getSuit());
        assertEquals(rank, card.getRank());
    }

    private static Stream<Arguments> suitAndRank() {
        return Stream.of(Suit.values())
                .flatMap(suit -> Stream.of(Rank.values())
                        .map(rank -> Arguments.of(suit, rank)));
    }

    @ParameterizedTest
    @MethodSource("suitAndRank")
    void testCreateDeck(Suit suit, Rank rank) {
        List<Card> deck = cardService.createDeck();
        assertNotNull(deck);
        assertEquals(32, deck.size(), "A deck should have 32 cards");

        // Verify the deck has all unique cards
        Set<Card> cardSet = new HashSet<>(deck);
        assertEquals(32, cardSet.size(), "The deck should have 32 unique cards");

        // Verify the deck has all combinations of suits and ranks
        assertTrue(deck.contains(new Card(suit, rank)), "The deck should contain the card: " + suit + " " + rank);
    }
}
