package de.htwberlin.service;

import de.htwberlin.cardmanagement.api.service.CardManagerInterface;
import de.htwberlin.rulesmanagement.impl.RuleService;
import de.htwberlin.cardmanagement.api.enums.Rank;
import de.htwberlin.cardmanagement.api.enums.Suit;
import de.htwberlin.cardmanagement.api.model.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RuleServiceTest {
    RuleService ruleService;
    CardManagerInterface cardManagerInterface;


    @BeforeEach
    void setUp() {
        ruleService = new RuleService();
    }


    /**
     * Test the game direction forwards, expecting the index to increment.
     */
    @Test
    void testIndexIncrement() {
        ruleService.getRules().setGameDirection(true); // true for forward
        ruleService.getRules().setCanPlayAgain(false);
        ruleService.getRules().setSkipNextPlayerTurn(false);
        int playersCount = 4; // Example player count
        assertEquals(1, ruleService.calculateNextPlayerIndex(0, playersCount));
    }

    /**
     * Test the game direction backwards, expecting the index to decrement.
     */
    @Test
    void testIndexDecrement() {
        ruleService.getRules().setGameDirection(false); // false for backward
        int playersCount = 4;
        assertEquals(2, ruleService.calculateNextPlayerIndex(3, playersCount));
    }

    /**
     * Test skipping the next player when the game direction is forward.
     */
    @Test
    void testSkipNextPlayerForward() {
        ruleService.getRules().setGameDirection(true);
        ruleService.getRules().setSkipNextPlayerTurn(true);
        int playersCount = 4;
        assertEquals(2, ruleService.calculateNextPlayerIndex(0, playersCount));
    }

    /**
     * Test skipping the next player when the game direction is backward.
     */
    @Test
    void testSkipNextPlayerBackward() {
        ruleService.getRules().setGameDirection(false);
        ruleService.getRules().setSkipNextPlayerTurn(true);
        int playersCount = 4;
        assertEquals(1, ruleService.calculateNextPlayerIndex(3, playersCount));
    }

    /**
     * Test that the index remains the same when the current player can play again.
     */
    @Test
    void testCanPlayAgain() {
        ruleService.getRules().setCanPlayAgain(true);
        int playersCount = 4;
        assertEquals(1, ruleService.calculateNextPlayerIndex(1, playersCount));
    }

//    private static Stream<Arguments> twoCards() {
//        return Stream.of(Suit.values())
//                .flatMap(suit1 -> Stream.of(Rank.values())
//                        .flatMap(rank1 -> Stream.of(Suit.values())
//                                .filter(suit2 -> suit1 != suit2)
//                                .flatMap(suit2 -> Stream.of(Rank.values())
//                                        .filter(rank2 -> rank1 != rank2)
//                                        .map(rank2 -> Arguments.of(new Card(suit1, rank1), new Card(suit2, rank2)))
//                                )
//                        )
//                );
//    }

    /**
     * card can be played when suits match and ranks differ.
     */
    @Test
    void suitsMatchRankDont() {
        Card topCard = new Card(Suit.HEARTS, Rank.NINE);
        Card card = new Card(Suit.HEARTS, Rank.TEN);
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
     * Nine can always be played.
     */
    @Test
    void playingNine() {
        Card topCard = new Card(Suit.HEARTS, Rank.JACK);
        Card card = new Card(Suit.CLUBS, Rank.NINE);
        assertTrue(ruleService.isValidMove(card, topCard));
    }

    /**
     * when jack is played, the wishedSuit Attribute most be changed
     */
    @Test
    void playJackTest1() {
        ruleService.applyJackSpecialEffect(new Card(Suit.HEARTS, Rank.JACK), Suit.HEARTS);
        assertEquals(Suit.HEARTS, ruleService.getRules().getWishCard());
    }

    /**
     * Cards to be drawn by next player are increased by 2
     */
    @Test
    void play7() {
        ruleService.applySpecialCardsEffect(new Card(Suit.HEARTS, Rank.SEVEN));
        assertEquals(2, ruleService.getRules().getCardsToBeDrawn());
    }
    /**
     * Cards to be drawn by next player are increased by 2. 2 sevens are played
     */
    @Test
    void play7time2() {
        ruleService.applySpecialCardsEffect(new Card(Suit.HEARTS, Rank.SEVEN));
        ruleService.applySpecialCardsEffect(new Card(Suit.DIAMONDS, Rank.SEVEN));
        assertEquals(4, ruleService.getRules().getCardsToBeDrawn());
    }
    /**
     * Cards to be drawn by next player are increased by 2. 3 sevens are played
     */
    @Test
    void play7time3() {
        ruleService.applySpecialCardsEffect(new Card(Suit.HEARTS, Rank.SEVEN));
        ruleService.applySpecialCardsEffect(new Card(Suit.DIAMONDS, Rank.SEVEN));
        ruleService.applySpecialCardsEffect(new Card(Suit.SPADES, Rank.SEVEN));
        assertEquals(6, ruleService.getRules().getCardsToBeDrawn());
    }
    /**
     * Next player Turn will be skipped after playing an 8
     * note that this test is passing because CardsTobeDrawn wasn't assigned any value which gives it 0 as value
     */
    @Test
    void play8SkipNextPlayer() {
        ruleService.applySpecialCardsEffect(new Card(Suit.HEARTS, Rank.EIGHT));
        assertTrue(ruleService.getRules().isSkipNextPlayerTurn());
    }
    /**
     * Draw 2 is skipped after playing an 8
     * note that this test is passing because CardsTobeDrawn wasn't assigned any value which gives it 0 as value
     */
    @Test
    void play8draw2() {
        ruleService.applySpecialCardsEffect(new Card(Suit.HEARTS, Rank.SEVEN));
        ruleService.applySpecialCardsEffect(new Card(Suit.HEARTS, Rank.EIGHT));
        assertEquals(0, ruleService.getRules().getCardsToBeDrawn());
    }
    /**
     * playing an Ace allows the current player to play another card
     */
    @Test
    void playAce() {
        ruleService.applySpecialCardsEffect(new Card(Suit.HEARTS, Rank.ACE));
        assertTrue(ruleService.getRules().isCanPlayAgain());
    }

    /**
     * Test that the score is calculated correctly.
     */
    @Test
    void testCalculateScore() {
        int score = ruleService.calculateScore(List.of(
                new Card(Suit.HEARTS, Rank.SEVEN),
                new Card(Suit.HEARTS, Rank.EIGHT),
                new Card(Suit.HEARTS, Rank.NINE),
                new Card(Suit.HEARTS, Rank.TEN),
                new Card(Suit.HEARTS, Rank.JACK),
                new Card(Suit.HEARTS, Rank.QUEEN),
                new Card(Suit.HEARTS, Rank.KING),
                new Card(Suit.HEARTS, Rank.ACE)
        ));
        assertEquals(-54, score);
    }

    //todo Ghazi: Verfiy bei allen mocks ob wirklich gemockt wurde!
    
}
