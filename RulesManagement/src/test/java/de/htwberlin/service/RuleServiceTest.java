package de.htwberlin.service;

import de.htwberlin.model.Rules;
import de.htwberlin.enums.Rank;
import de.htwberlin.enums.Suit;
import de.htwberlin.model.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.mockito.Mockito.*;


import static org.junit.jupiter.api.Assertions.*;

class RuleServiceTest {
    RuleService ruleService;
    Rules rules;

    @BeforeEach
    void setUp() {
        ruleService = mock(RuleService.class);
        rules = new Rules();
    }

    /**
     * Test the game direction forwards, expecting the index to increment.
     */
    @Test
    void testIndexIncrement() {
        rules.setGameDirection(true); // true for forward
        rules.setCanPlayAgain(false);
        rules.setSkipNextPlayerTurn(false);
        int playersCount = 4; // Example player count
        assertEquals(1, ruleService.calculateNextPlayerIndex(0, playersCount));
    }

    /**
     * Test the game direction backwards, expecting the index to decrement.
     */
    @Test
    void testIndexDecrement() {
        rules.setGameDirection(false); // false for backward
        int playersCount = 4;
        assertEquals(2, ruleService.calculateNextPlayerIndex(3, playersCount));
    }

    /**
     * Test skipping the next player when the game direction is forward.
     */
    @Test
    void testSkipNextPlayerForward() {
        rules.setGameDirection(true);
        rules.setSkipNextPlayerTurn(true);
        int playersCount = 4;
        assertEquals(2, ruleService.calculateNextPlayerIndex(0, playersCount));
    }

    /**
     * Test skipping the next player when the game direction is backward.
     */
    @Test
    void testSkipNextPlayerBackward() {
        rules.setGameDirection(false);
        rules.setSkipNextPlayerTurn(true);
        int playersCount = 4;
        assertEquals(1, ruleService.calculateNextPlayerIndex(3, playersCount));
    }

    /**
     * Test that the index remains the same when the current player can play again.
     */
    @Test
    void testCanPlayAgain() {
        rules.setCanPlayAgain(true);
        int playersCount = 4;
        assertEquals(1, ruleService.calculateNextPlayerIndex(1, playersCount));
    }

    private static Stream<Arguments> twoCards() {
        return Stream.of(Suit.values())
                .flatMap(suit1 -> Stream.of(Rank.values())
                        .flatMap(rank1 -> Stream.of(Suit.values())
                                .filter(suit2 -> suit1 != suit2)
                                .flatMap(suit2 -> Stream.of(Rank.values())
                                        .filter(rank2 -> rank1 != rank2)
                                        .map(rank2 -> Arguments.of(new Card(suit1, rank1), new Card(suit2, rank2)))
                                )
                        )
                );
    }

    /**
     * card can be played when suits match and ranks differ.
     */
    @ParameterizedTest
    @MethodSource("twoCards")
    void suitsMatchRankDont(Card card, Card topCard) {
        assertTrue(ruleService.isValidMove(card, topCard));
    }

    /**
     * card can be played when ranks match and suits differ.
     */
    @Test
    void ranksMatchSuitsDont() {
        Card topCard = new Card(Suit.HEARTS, Rank.NINE);
        Card card = new Card(Suit.CLUBS, Rank.NINE);
        assertTrue(ruleService.isValidMove(card, topCard));
    }

    /**
     * card can be played when both suits and ranks match.
     */
    @Test
    void bothMatch() {
        Card topCard = new Card(Suit.HEARTS, Rank.NINE);
        Card card = new Card(Suit.HEARTS, Rank.NINE);
        assertTrue(ruleService.isValidMove(card, topCard));
    }

    /**
     * card can't be played when neither suits nor ranks match.
     * note that this test is passing because checkValidCard is not implemented yet. Current impl. always returns False
     */
    @Test
    void bothDont() {
        Card topCard = new Card(Suit.HEARTS, Rank.NINE);
        Card card = new Card(Suit.CLUBS, Rank.TEN);
        assertFalse(ruleService.isValidMove(card, topCard));
    }


    /**
     * 2 Jacks can't be played.
     * note that this test is passing because checkValidCard is not implemented yet. Current impl. always returns False
     */
    @Test
    void playingTowJacks() {
        Card topCard = new Card(Suit.HEARTS, Rank.JACK);
        Card card = new Card(Suit.CLUBS, Rank.JACK);
        assertFalse(ruleService.isValidMove(card, topCard));
    }

    /**
     * when jack is played, the wishedSuit Attribute most be changed
     */
    @Test
    void playJackTest1() {
        ruleService.playJack(Suit.HEARTS);
        assertEquals(Suit.HEARTS, rules.getWishCard());
    }

    /**
     * Cards to be drawn by next player are increased by 2
     */
    @Test
    void play7() {
        ruleService.play7();
        assertEquals(2, rules.getCardsToBeDrawn());
    }
    /**
     * Cards to be drawn by next player are increased by 2. 2 sevens are played
     */
    @Test
    void play7time2() {
        ruleService.play7();
        ruleService.play7();
        assertEquals(4, rules.getCardsToBeDrawn());
    }
    /**
     * Cards to be drawn by next player are increased by 2. 3 sevens are played
     */
    @Test
    void play7time3() {
        ruleService.play7();
        ruleService.play7();
        ruleService.play7();
        assertEquals(6, rules.getCardsToBeDrawn());
    }
    /**
     * Next player Turn will be skipped after playing an 8
     * note that this test is passing because CardsTobeDrawn wasn't assigned any value which gives it 0 as value
     */
    @Test
    void play8SkipNextPlayer() {
        ruleService.play8();
        assertTrue(rules.isSkipNextPlayerTurn());
    }
    /**
     * Draw 2 is skipped after playing an 8
     * note that this test is passing because CardsTobeDrawn wasn't assigned any value which gives it 0 as value
     */
    @Test
    void play8draw2() {
        ruleService.play7();
        ruleService.play8();
        assertEquals(0, rules.getCardsToBeDrawn());
    }
    /**
     * playing an Ace allows the current player to play another card
     */
    @Test
    void playAce() {
        ruleService.playAce();
        assertTrue(rules.isCanPlayAgain());
    }
}
