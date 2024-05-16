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

        player1 = new Player("Player 1");

        player2 = new Player("Player 2");

        player3 = new Player("Player 3");

        player4 = new Player("Player 4");


        gameState.setPlayers(Arrays.asList(player1, player2, player3, player4));
        gameState.setCurrentPlayerIndex(0);
        gameState.setDeck(new Deck());
        gameState.setDiscardPile(new Stack<>());
    }

    /**
     * Test initializing the game with a specified number of players.
     */
    @Test
    void testInitGame() {
        int numbplayers = 4;
        GameState gamestate = gameService.initializeGame(numbplayers);
        gameState.getPlayers().forEach(player -> assertEquals(5, player.getHand().size())); // checks that every player has 5 cards on hand
// todo Ghazi: the component shouldn't be testing a concrete number of cards in the deck. this will be mocked from RulesService
        assertEquals(32 - (numbplayers * 5) - 1, gamestate.getDeck().getCards().size()); // checks deck has 32 cards - 5 cards for every player - initial discardPile card

// todo Ghazi: same as abouve, will be updated
        assertEquals(numbplayers, gamestate.getPlayers().size());
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
//     To-do define numbers for variables -> never mind.
//     The values for the variables must be assigned when gameInitialize is called
    }

    @Test
    void testDrawCardGeorge() {
        GameState gameState = gameService.initializeGame(4);
        int playerIndex = gameState.getCurrentPlayerIndex();
        Player player = gameState.getPlayers().get(playerIndex);
        int initialHandSize = player.getHand().size();
        int initialDiscardPileSize = gameState.getDiscardPile().size();
        Card drawnCard = gameService.drawCard(playerIndex);
        assertNotNull(drawnCard);
        assertEquals(initialHandSize + 1, player.getHand().size());
        assertEquals(initialHandSize - 1, gameState.getDiscardPile().size());

        Stack<Card> allCardsWithoutDrawCard = new Stack<>();
        allCardsWithoutDrawCard.addAll(gameState.getDeck().getCards());
        allCardsWithoutDrawCard.addAll(gameState.getDiscardPile());
        gameState.getPlayers().forEach(p -> allCardsWithoutDrawCard.addAll(p.getHand()));
        assertFalse(allCardsWithoutDrawCard.contains(drawnCard));
//        todo drawCard is in Player not zwischenspeicher
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
