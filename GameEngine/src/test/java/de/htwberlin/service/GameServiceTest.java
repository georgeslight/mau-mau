package de.htwberlin.service;

import de.htwberlin.api.model.GameState;
import de.htwberlin.api.model.Player;
import de.htwberlin.impl.service.CardService;
import de.htwberlin.impl.service.GameService;
import de.htwberlin.impl.service.PlayerService;
import de.htwberlin.impl.service.RuleService;
import de.htwberlin.api.model.*;
import de.htwberlin.api.enums.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GameServiceTest {

    private GameService gameService;
    private PlayerService playerService;
    private CardService cardService;
    private RuleService ruleService;

    @BeforeEach
    void setUp() {
        this.playerService = mock(PlayerService.class);
        this.cardService = mock(CardService.class);
        this.ruleService = mock(RuleService.class);
        this.gameService = new GameService(playerService, cardService, ruleService);
    }

    /**
     * Test initializing the game with a specified number of players.
     */
    @Test
    void testInitGame() {
        // Create a valid deck of cards
        Stack<Card> deck = Stream.of(Suit.values())
                .flatMap(suit -> Stream.of(Rank.values())
                        .map(rank -> new Card(suit, rank)))
                .collect(Collectors.toCollection(Stack::new));

        // Define the behavior of the cardService mocks
        when(cardService.createDeck()).thenReturn(deck);
        when(cardService.shuffle(any())).thenAnswer(invocation -> {
            Stack<Card> originalDeck = invocation.getArgument(0);
            Collections.shuffle(originalDeck);
            return originalDeck;
        });

        // Define the behavior of the playerService mock to return valid players
        when(playerService.createPlayer(anyString(), anyList())).thenAnswer(invocation -> {
            String name = invocation.getArgument(0);
            List<Card> hand = invocation.getArgument(1);
            return new Player(name, hand);
        });

        int playersCount = 4;
        GameState gameState = gameService.initializeGame(playersCount);
        gameState.getPlayers().forEach(player -> assertEquals(5, player.getHand().size())); // checks that every player has 5 cards on hand
        assertEquals(32 - (playersCount * 5) - 1, gameState.getDeck().size()); // checks deck has 32 cards - 5 cards for every player - initial discardPile card
        assertEquals(playersCount, gameState.getPlayers().size());
        assertNotEquals(gameState.getCurrentPlayerIndex(), gameState.getNextPlayerIndex());
        assertEquals(0, gameState.getCurrentPlayerIndex());
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
