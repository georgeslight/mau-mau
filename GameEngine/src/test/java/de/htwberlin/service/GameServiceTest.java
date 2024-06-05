package de.htwberlin.service;

import de.htwberlin.api.model.GameState;
import de.htwberlin.api.model.Player;
import de.htwberlin.api.service.GameManagerInterface;
import de.htwberlin.impl.service.CardService;
import de.htwberlin.impl.service.GameService;
import de.htwberlin.impl.service.RuleService;
import de.htwberlin.api.model.*;
import de.htwberlin.api.enums.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    private CardService cardService;

    @BeforeEach
    void setUp() {
        cardService = new CardService();
        gameService = new GameService();
        ruleService = mock(RuleService.class);
        gameState = new GameState();

        player1 = new Player("Player 1", List.of(
                new Card(Suit.HEARTS, Rank.SEVEN),
                new Card(Suit.CLUBS, Rank.EIGHT),
                new Card(Suit.SPADES, Rank.NINE),
                new Card(Suit.DIAMONDS, Rank.TEN),
                new Card(Suit.CLUBS, Rank.JACK)
        ));

        player2 = new Player("Player 2", List.of(
                new Card(Suit.SPADES, Rank.QUEEN),
                new Card(Suit.DIAMONDS, Rank.KING),
                new Card(Suit.HEARTS, Rank.ACE),
                new Card(Suit.CLUBS, Rank.SEVEN),
                new Card(Suit.SPADES, Rank.EIGHT)
        ));

        player3 = new Player("Player 3", List.of(
                new Card(Suit.HEARTS, Rank.NINE),
                new Card(Suit.CLUBS, Rank.TEN),
                new Card(Suit.SPADES, Rank.JACK),
                new Card(Suit.DIAMONDS, Rank.QUEEN),
                new Card(Suit.HEARTS, Rank.KING)
        ));

        player4 = new Player("Player 4", List.of(
                new Card(Suit.DIAMONDS, Rank.ACE),
                new Card(Suit.SPADES, Rank.SEVEN),
                new Card(Suit.CLUBS, Rank.NINE),
                new Card(Suit.HEARTS, Rank.TEN),
                new Card(Suit.DIAMONDS, Rank.JACK)
        ));


        gameState.setPlayers(Arrays.asList(player1, player2, player3, player4));
        gameState.setCurrentPlayerIndex(0);
        gameState.setDeck(cardService.createDeck());
        gameState.setDiscardPile(new Stack<>());
    }

    /**
     * Test initializing the game with a specified number of players.
     */
    @Test
    void testInitGame() {
        int playersCount = 4;
        GameState gamestate = gameService.initializeGame(playersCount);
        gameState.getPlayers().forEach(player -> assertEquals(5, player.getHand().size())); // checks that every player has 5 cards on hand
        assertEquals(32 - (playersCount * 5) - 1, gamestate.getDeck().getCards().size()); // checks deck has 32 cards - 5 cards for every player - initial discardPile card
        assertEquals(playersCount, gamestate.getPlayers().size());
        assertNotEquals(gameState.getCurrentPlayerIndex(), gamestate.getNextPlayerIndex());
        assertEquals(0, gamestate.getCurrentPlayerIndex());
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
        player2.setHand(List.of(new Card(Suit.HEARTS, Rank.ACE)));
        assertFalse(gameService.checkWinner(player2));
    }
}
