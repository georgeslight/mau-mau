package de.htwberlin.rulesmanagement;

import de.htwberlin.rulesmanagement.api.model.Rules;
import de.htwberlin.rulesmanagement.impl.RuleService;
import de.htwberlin.cardsmanagement.api.enums.Rank;
import de.htwberlin.cardsmanagement.api.enums.Suit;
import de.htwberlin.cardsmanagement.api.model.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RuleServiceTest {

    RuleService ruleService;
    private Rules rules;

    @BeforeEach
    void setUp() {
        this.ruleService = new RuleService();
        this.rules = new Rules();
    }

    /**
     * Test the game direction forwards, expecting the index to increment.
     */
    @Test
    void testIndexIncrement() {
//        rules.setGameDirection(true); // true for forward
        rules.setCanPlayAgain(false);
        rules.setSkipNextPlayerTurn(false);
        int playersCount = 4; // Example player count
        assertEquals(1, ruleService.calculateNextPlayerIndex(0, playersCount, rules));
    }

    /**
     * Test the game direction backwards, expecting the index to decrement.
     */
//    @Test
//    void testIndexDecrement() {
//        rules.setGameDirection(false); // false for backward
//        int playersCount = 4;
//        assertEquals(2, ruleService.calculateNextPlayerIndex(3, playersCount, rules));
//    }

    /**
     * Test skipping the next player when the game direction is forward.
     */
    @Test
    void testSkipNextPlayerForward() {
//        rules.setGameDirection(true);
        rules.setSkipNextPlayerTurn(true);
        int playersCount = 4;
        assertEquals(2, ruleService.calculateNextPlayerIndex(0, playersCount, rules));
    }

    /**
     * Test skipping the next player when the game direction is backward.
     */
//    @Test
//    void testSkipNextPlayerBackward() {
//        rules.setGameDirection(false);
//        rules.setSkipNextPlayerTurn(true);
//        int playersCount = 4;
//        assertEquals(1, ruleService.calculateNextPlayerIndex(3, playersCount, rules));
//    }

    /**
     * Test that the index remains the same when the current player can play again.
     */
    @Test
    void testCanPlayAgain() {
        rules.setCanPlayAgain(true);
        int playersCount = 4;
        assertEquals(1, ruleService.calculateNextPlayerIndex(1, playersCount, rules));
    }

    /**
     * card can be played when suits match and ranks differ.
     */
    @Test
    void suitsMatchRankDont() {
        Card topCard = new Card(Suit.HEARTS, Rank.NINE);
        Card card = new Card(Suit.HEARTS, Rank.TEN);
        assertTrue(ruleService.isValidMove(card, topCard, rules));
    }

    /**
     * card can be played when ranks match and suits differ.
     */
    @Test
    void ranksMatchSuitsDont() {
        Card topCard = new Card(Suit.HEARTS, Rank.NINE);
        Card card = new Card(Suit.CLUBS, Rank.NINE);
        assertTrue(ruleService.isValidMove(card, topCard, rules));
    }

    /**
     * card can be played when both suits and ranks match.
     */
    @Test
    void bothMatch() {
        Card topCard = new Card(Suit.HEARTS, Rank.NINE);
        Card card = new Card(Suit.HEARTS, Rank.NINE);
        assertTrue(ruleService.isValidMove(card, topCard, rules));
    }

    /**
     * card can't be played when neither suits nor ranks match.
     * note that this test is passing because checkValidCard is not implemented yet. Current impl. always returns False
     */
    @Test
    void bothDont() {
        Card topCard = new Card(Suit.HEARTS, Rank.NINE);
        Card card = new Card(Suit.CLUBS, Rank.TEN);
        assertFalse(ruleService.isValidMove(card, topCard, rules));
    }


    /**
     * 2 Jacks can't be played.
     * note that this test is passing because checkValidCard is not implemented yet. Current impl. always returns False
     */
    @Test
    void playingTowJacks() {
        Card topCard = new Card(Suit.HEARTS, Rank.JACK);
        Card card = new Card(Suit.CLUBS, Rank.JACK);
        rules.setWishCard(Suit.HEARTS);
        assertFalse(ruleService.isValidMove(card, topCard, rules));
    }

    /**
     * Nine can always be played.
     */
    @Test
    void playingNine() {
        Card topCard = new Card(Suit.HEARTS, Rank.JACK);
        Card card = new Card(Suit.CLUBS, Rank.NINE);
        assertTrue(ruleService.isValidMove(card, topCard, rules));
    }

    /**
     * when jack is played, the wishedSuit Attribute most be changed
     */
    @Test
    void playJackTest1() {
        ruleService.applyJackSpecialEffect(new Card(Suit.HEARTS, Rank.JACK), Suit.HEARTS, rules);
        assertEquals(Suit.HEARTS, rules.getWishCard());
    }

    /**
     * Cards to be drawn by next player are increased by 2
     */
    @Test
    void play7() {
        ruleService.applySpecialCardsEffect(new Card(Suit.HEARTS, Rank.SEVEN), rules);
        assertEquals(2, rules.getCardsToBeDrawn());
    }
    /**
     * Cards to be drawn by next player are increased by 2. 2 sevens are played
     */
    @Test
    void play7time2() {
        ruleService.applySpecialCardsEffect(new Card(Suit.HEARTS, Rank.SEVEN), rules);
        ruleService.applySpecialCardsEffect(new Card(Suit.DIAMONDS, Rank.SEVEN), rules);
        assertEquals(4, rules.getCardsToBeDrawn());
    }
    /**
     * Cards to be drawn by next player are increased by 2. 3 sevens are played
     */
    @Test
    void play7time3() {
        ruleService.applySpecialCardsEffect(new Card(Suit.HEARTS, Rank.SEVEN), rules);
        ruleService.applySpecialCardsEffect(new Card(Suit.DIAMONDS, Rank.SEVEN), rules);
        ruleService.applySpecialCardsEffect(new Card(Suit.SPADES, Rank.SEVEN), rules);
        assertEquals(6, rules.getCardsToBeDrawn());
    }
    /**
     * Next player Turn will be skipped after playing an 8
     * note that this test is passing because CardsTobeDrawn wasn't assigned any value which gives it 0 as value
     */
    @Test
    void play8SkipNextPlayer() {
        ruleService.applySpecialCardsEffect(new Card(Suit.HEARTS, Rank.EIGHT), rules);
        assertTrue(rules.isSkipNextPlayerTurn());
    }
    /**
     * Draw 2 is skipped after playing an 8
     * note that this test is passing because CardsTobeDrawn wasn't assigned any value which gives it 0 as value
     */
    @Test
    void play8draw2() {
        ruleService.applySpecialCardsEffect(new Card(Suit.HEARTS, Rank.SEVEN), rules);
        ruleService.applySpecialCardsEffect(new Card(Suit.HEARTS, Rank.EIGHT), rules);
        assertEquals(0, rules.getCardsToBeDrawn());
    }
    /**
     * playing an Ace allows the current player to play another card
     */
    @Test
    void playAce() {
        ruleService.applySpecialCardsEffect(new Card(Suit.HEARTS, Rank.ACE), rules);
        assertTrue(rules.isCanPlayAgain());
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
}
