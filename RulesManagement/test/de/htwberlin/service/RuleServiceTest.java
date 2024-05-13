package de.htwberlin.service;

import de.htwberlin.model.Rules;
import de.htwberlin.enums.Rank;
import de.htwberlin.enums.Suit;
import de.htwberlin.model.Card;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;


import static org.junit.jupiter.api.Assertions.*;

class RuleServiceTest {
    RuleService ruleService = mock(RuleService.class);
    Rules rules = mock(Rules.class);

    /**
     * card can be played when suits match and ranks differ.
     */
    @Test
    void suitsMatchRankDont() {
        Card topCard = new Card(Suit.HEARTS, Rank.NINE);
        Card card = new Card(Suit.HEARTS, Rank.TEN);
        assertTrue(ruleService.checkValidCard(card, topCard));
    }

    /**
     * card can be played when ranks match and suits differ.
     */
    @Test
    void ranksMatchSuitsDont() {
        Card topCard = new Card(Suit.HEARTS, Rank.NINE);
        Card card = new Card(Suit.CLUBS, Rank.NINE);
        assertTrue(ruleService.checkValidCard(card, topCard));
    }

    /**
     * card can be played when both suits and ranks match.
     */
    @Test
    void bothMatch() {
        Card topCard = new Card(Suit.HEARTS, Rank.NINE);
        Card card = new Card(Suit.HEARTS, Rank.NINE);
        assertTrue(ruleService.checkValidCard(card, topCard));
    }

    /**
     * card can't be played when neither suits nor ranks match.
     * note that this test is passing because checkValidCard is not implemented yet. Current impl. always returns False
     */
    @Test
    void bothDont() {
        Card topCard = new Card(Suit.HEARTS, Rank.NINE);
        Card card = new Card(Suit.CLUBS, Rank.TEN);
        assertFalse(ruleService.checkValidCard(card, topCard));
    }


    /**
     * 2 Jacks can't be played.
     * note that this test is passing because checkValidCard is not implemented yet. Current impl. always returns False
     */
    @Test
    void playingTowJacks() {
        Card topCard = new Card(Suit.HEARTS, Rank.JACK);
        Card card = new Card(Suit.CLUBS, Rank.JACK);
        assertFalse(ruleService.checkValidCard(card, topCard));
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
        assertEquals(2, rules.getCardsTObeDrawn());
    }
    /**
     * Cards to be drawn by next player are increased by 2. 2 sevens are played
     */
    @Test
    void play7time2() {
        ruleService.play7();
        ruleService.play7();
        assertEquals(4, rules.getCardsTObeDrawn());
    }
    /**
     * Cards to be drawn by next player are increased by 2. 3 sevens are played
     */
    @Test
    void play7time3() {
        ruleService.play7();
        ruleService.play7();
        ruleService.play7();
        assertEquals(6, rules.getCardsTObeDrawn());
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
        assertEquals(0, rules.getCardsTObeDrawn());
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