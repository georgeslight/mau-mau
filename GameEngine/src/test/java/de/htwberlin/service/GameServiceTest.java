package de.htwberlin.service;

import de.htwberlin.model.*;
import de.htwberlin.enums.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions.*;
import org.mockito.Mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GameServiceTest {
    private GameManagerInterface gameService;
    private GameState gameState;
    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;
    private RuleService ruleService;

    @BeforeEach
    void setUp() {
        gameService = new GameService();
        gameState = new GameState();
        ruleService = mock(RuleService.class);

        player1 = new Player();
        player1.setName("Player 1");
        player2 = new Player();
        player2.setName("Player 2");
        player3 = new Player();
        player3.setName("Player 3");
        player4 = new Player();
        player4.setName("Player 4");

        gameState.setPlayers(Arrays.asList(player1, player2, player3, player4));
        gameState.setCurrentPlayerIndex(0);
        gameState.setDeck(new Deck());
        gameState.setDiscardPile(new Stack<>());
    }

    /**
     * Test initializing the game with a specified number of players.
     */
    @Test
    void testInitializeGame() {
        GameState state = gameService.initializeGame(4);
        assertNotNull(state); // Game state exists
        assertEquals(4, state.getPlayers().size()); // Correct number of players
        assertEquals(1, state.getDiscardPile().size()); // A card has been drawn
    }

    /**
     * Test moving to the next player in the sequence.
     */
    @Test
    void testNextPlayer() {
        // Mocking the behavior of calculateNextPlayerIndex
        when(ruleService.calculateNextPlayerIndex(0, 4)).thenReturn(1);
        // Invoke nextPlayer method
        Player nextPlayer = gameService.nextPlayer(gameState);
        assertEquals(player2, nextPlayer);
        assertEquals(1, gameState.getCurrentPlayerIndex());
    }

    /**
     * Test drawing a card from the draw pile.
     */
    @Test
    void testDrawCard() {
        int drawPileSize = gameState.getDeck().getCards().size();
        int playerHandSize = player1.getHand().size();
        int discardPileSize = gameState.getDiscardPile().size();
        Card drawnCard = gameService.drawCard(gameState.getCurrentPlayerIndex());
        assertEquals(drawPileSize - 1, gameState.getDeck().getCards().size());// One card removed from draw pile
        assertFalse(gameState.getDeck().getCards().contains(drawnCard));// Drawn card is no longer in draw pile

        assertEquals(playerHandSize + 1, player1.getHand().size());// Player has one more card
        assertTrue(player1.getHand().contains(drawnCard));// Player has the drawn card

        assertEquals(discardPileSize, gameState.getDiscardPile().size());// Discard pile size unchanged
        assertTrue(gameState.getDiscardPile().contains(drawnCard));// Drawn card is now on discard pile
    }

    /**
     * Test playing a card from the player's hand.
     */
    @Test
    void testPlayCard() {
        Card playedCard = new Card(Suit.HEARTS, Rank.ACE);
        Card antoherCard = new Card(Suit.DIAMONDS, Rank.ACE);
        List<Card> cards = new ArrayList<>();
        cards.add(playedCard);
        cards.add(antoherCard);
        player1.setHand(cards);
        gameService.playCard(player1, playedCard, gameState);
        assertTrue(player1.getHand().contains(antoherCard));// Player still has the other card
        assertFalse(player1.getHand().contains(playedCard));// Player no longer has the played card
        assertEquals(playedCard, gameState.getDiscardPile().peek());// Played card is now on discard pile
    }

    /**
     * Test checking if a player has won the game.
     */
    @Test
    void testCheckWinner() {
        player1.setHand(Collections.emptyList()); // No cards left
        assertTrue(gameService.checkWinner(player1));
        player1.setHand(Collections.singletonList(new Card(Suit.HEARTS, Rank.ACE)));
        assertFalse(gameService.checkWinner(player1));
    }
}
