package de.htwberlin.service;

import de.htwberlin.Rules;
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
     * applySpecialCardEffects runs the Method play7 if the card played was 7
     */
    @Test
    public void applySpecialCardEffectsOn7() {
        Card card7 = new Card(Suit.HEARTS, Rank.SEVEN);
        ruleService.applySpecialCardEffects(card7);
        verify(ruleService).play7();
    }

    /**
     * applySpecialCardEffects runs the Method play8 if the card played was 8
     */
    @Test
    public void applySpecialCardEffectsOn8() {
        final Card card8 = new Card(Suit.HEARTS, Rank.EIGHT);
        ruleService.applySpecialCardEffects(card8);
        verify(ruleService).play8();
    }
    /**
     * applySpecialCardEffects runs the Method playJack if the card played was a Jack
     */
    @Test
    public void applySpecialCardEffectsOnJack() {
        final Card cardJack = new Card(Suit.HEARTS, Rank.JACK);
        ruleService.applySpecialCardEffects(cardJack);
        verify(ruleService).playJack(cardJack.getSuit());
    }

    /**
     * applySpecialCardEffects runs the Method play8 if the card played was 8
     */
    @Test
    public void applySpecialCardEffectsOnAce() {
        final Card cardAce = new Card(Suit.HEARTS, Rank.ACE);
        ruleService.applySpecialCardEffects(cardAce);
        verify(ruleService).playAce();
    }

    /**
     * Verify valid play when suits match and ranks differ.
     */
    @Test
    void whenSuitsMatchAndRanksDoNotMatch_thenValid() {
        Card topCard = new Card(Suit.HEARTS, Rank.NINE);
        Card card = new Card(Suit.HEARTS, Rank.TEN);
        assertTrue(ruleService.checkValidCard(card, topCard));
    }

    /**
     * Verify valid play when ranks match and suits differ.
     */
    @Test
    void whenSuitsDoNotMatchAndRanksMatch_thenValid() {
        Card topCard = new Card(Suit.HEARTS, Rank.NINE);
        Card card = new Card(Suit.CLUBS, Rank.NINE);
        assertTrue(ruleService.checkValidCard(card, topCard));
    }

    /**
     * Verify valid play when both suits and ranks match.
     */
    @Test
    void whenBothSuitsAndRanksMatch_thenValid() {
        Card topCard = new Card(Suit.HEARTS, Rank.NINE);
        Card card = new Card(Suit.HEARTS, Rank.NINE);
        assertTrue(ruleService.checkValidCard(card, topCard));
    }

    /**
     * Verify invalid play when neither suits nor ranks match.
     */
    @Test
    void whenNeitherSuitsNorRanksMatch_thenInvalid() {
        Card topCard = new Card(Suit.HEARTS, Rank.NINE);
        Card card = new Card(Suit.CLUBS, Rank.TEN);
        assertFalse(ruleService.checkValidCard(card, topCard));
    }
    /**
     * Verify invalid play when 2 Jacks are played.
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
     */
    @Test
    void play8SkipNextPlayer() {
        ruleService.play8();
        assertTrue(rules.isSkipNextPlayerTurn());
    }
    /**
     * Draw 2 is skipped after playing an 8
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