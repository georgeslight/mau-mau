package de.htwberlin.virtualplayer.impl;

import static org.junit.jupiter.api.Assertions.*;
import de.htwberlin.cardsmanagement.api.enums.Rank;
import de.htwberlin.cardsmanagement.api.enums.Suit;
import de.htwberlin.cardsmanagement.api.model.Card;
import de.htwberlin.playermanagement.api.model.Player;
import de.htwberlin.rulesmanagement.api.model.Rules;
import de.htwberlin.rulesmanagement.api.service.RuleEngineInterface;
import de.htwberlin.virtualplayer.impl.VirtualPlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VirtualPlayerServiceTest {

    private VirtualPlayerService virtualPlayerService;

    @Mock
    private RuleEngineInterface ruleEngineInterface;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        virtualPlayerService = new VirtualPlayerService();
    }

    /**
     * Test that the virtual player decides to play a valid card.
     */
    @Test
    void testDecideCardToPlay_validCard() {
        // Set up a player with a hand containing a valid card to play
        Card topCard = new Card(Suit.HEARTS, Rank.TEN);
        Card validCard = new Card(Suit.HEARTS, Rank.JACK);
        List<Card> hand = List.of(validCard, new Card(Suit.SPADES, Rank.SEVEN));
        Player player = new Player("Virtual Player", hand, true);

        // Mock the behavior of the rule engine to recognize a valid move
        when(ruleEngineInterface.isValidMove(eq(validCard), eq(topCard), any())).thenReturn(true);

        // Decide card to play
        Card chosenCard = virtualPlayerService.decideCardToPlay(player, topCard, ruleEngineInterface, new Rules());

        // Verify that the valid card is chosen
        assertEquals(validCard, chosenCard);
    }

    /**
     * Test that the virtual player decides to draw a card when no valid card is available.
     */
    @Test
    void testDecideCardToPlay_noValidCard() {
        // Set up a player with a hand that contains no valid card to play
        Card topCard = new Card(Suit.HEARTS, Rank.TEN);
        List<Card> hand = List.of(new Card(Suit.CLUBS, Rank.NINE), new Card(Suit.SPADES, Rank.SEVEN));
        Player player = new Player("Virtual Player", hand, true);

        // Mock the behavior of the rule engine to recognize no valid move
        when(ruleEngineInterface.isValidMove(any(), eq(topCard), any())).thenReturn(false);

        // Decide card to play
        Card chosenCard = virtualPlayerService.decideCardToPlay(player, topCard, ruleEngineInterface, new Rules());

        // Verify that no card is chosen
        assertNull(chosenCard);
    }

    /**
     * Test that the virtual player decides a suit randomly when a Jack is played.
     */
    @Test
    void testDecideSuit() {
        // Set up a player
        Player player = new Player("Virtual Player", List.of(), true);

        // Decide suit
        Suit chosenSuit = virtualPlayerService.decideSuit(player, ruleEngineInterface);

        // Verify that a valid suit is chosen
        assertNotNull(chosenSuit);
        assertTrue(List.of(Suit.values()).contains(chosenSuit));
    }

    /**
     * Cant play Jack on Jack
     */
    @Test
    void testDecideCardToPlay_cantPlayJackOnJack() {
        // Set up a player with a hand containing a valid card to play
        Card topCard = new Card(Suit.HEARTS, Rank.JACK);
        Card validCard = new Card(Suit.HEARTS, Rank.JACK);
        List<Card> hand = List.of(validCard, new Card(Suit.SPADES, Rank.SEVEN));
        Player player = new Player("Virtual Player", hand, true);

        // Mock the behavior of the rule engine to recognize a valid move
        when(ruleEngineInterface.isValidMove(eq(validCard), eq(topCard), any())).thenReturn(false);

        // Decide card to play
        Card chosenCard = virtualPlayerService.decideCardToPlay(player, topCard, ruleEngineInterface, new Rules());

        // Verify that the valid card is chosen
        assertNull(chosenCard);
    }

    /**
     * Test that the virtual player decides to draw a card when no valid move is possible.
     */
    @Test
    void testShouldDrawCard_noValidMove() {
        // Set up a player with a hand that contains no valid card to play
        Card topCard = new Card(Suit.HEARTS, Rank.TEN);
        List<Card> hand = List.of(new Card(Suit.CLUBS, Rank.NINE), new Card(Suit.SPADES, Rank.SEVEN));
        Player player = new Player("Virtual Player", hand, true);

        // Mock the behavior of the rule engine to recognize no valid move
        when(ruleEngineInterface.isValidMove(any(), eq(topCard), any())).thenReturn(false);

        // Decide whether to draw a card
        boolean shouldDraw = virtualPlayerService.shouldDrawCard(player, topCard, ruleEngineInterface, new Rules());

        // Verify that the virtual player decides to draw
        assertTrue(shouldDraw);
    }

    /**
     * Test that the virtual player decides not to draw a card when a valid move is possible.
     */
    @Test
    void testShouldDrawCard_validMoveExists() {
        // Set up a player with a hand containing a valid card to play
        Card topCard = new Card(Suit.HEARTS, Rank.TEN);
        Card validCard = new Card(Suit.HEARTS, Rank.JACK);
        List<Card> hand = List.of(validCard, new Card(Suit.SPADES, Rank.SEVEN));
        Player player = new Player("Virtual Player", hand, true);

        // Mock the behavior of the rule engine to recognize a valid move
        when(ruleEngineInterface.isValidMove(eq(validCard), eq(topCard), any())).thenReturn(true);

        // Decide whether to draw a card
        boolean shouldDraw = virtualPlayerService.shouldDrawCard(player, topCard, ruleEngineInterface, new Rules());

        // Verify that the virtual player decides not to draw
        assertFalse(shouldDraw);
    }

    /**
     * Test that the virtual player decides to say "Mau" when holding one card.
     */
    @Test
    void testShouldSayMau_oneCard() {
        // Set up a player with one card
        List<Card> hand = List.of(new Card(Suit.HEARTS, Rank.ACE));
        Player player = new Player("Virtual Player", hand, true);

        // Decide whether to say "Mau"
        boolean shouldSayMau = virtualPlayerService.shouldSayMau(player);

        // Verify that the virtual player decides to say "Mau"
        assertTrue(shouldSayMau);
    }

    /**
     * Test that the virtual player decides not to say "Mau" when holding more than one card.
     */
    @Test
    void testShouldSayMau_multipleCards() {
        // Set up a player with multiple cards
        List<Card> hand = List.of(new Card(Suit.HEARTS, Rank.ACE), new Card(Suit.SPADES, Rank.KING));
        Player player = new Player("Virtual Player", hand, true);

        // Decide whether to say "Mau"
        boolean shouldSayMau = virtualPlayerService.shouldSayMau(player);

        // Verify that the virtual player decides not to say "Mau"
        assertFalse(shouldSayMau);
    }
}
