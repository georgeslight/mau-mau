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
import java.util.stream.IntStream;
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
        // Mock the RuleService to calculate the next player index
        when(ruleService.calculateNextPlayerIndex(anyInt(), anyInt())).thenAnswer(invocation -> {
            int currentIndex = invocation.getArgument(0);
            int totalPlayers = invocation.getArgument(1);
            return (currentIndex + 1) % totalPlayers;
        });

        // Set up a GameState with 4 players
        List<Player> players = IntStream.range(0, 4)
                .mapToObj(i -> new Player("Player " + i, new ArrayList<>()))
                .collect(Collectors.toList());

        GameState gameState = new GameState();
        gameState.setPlayers(players);
        gameState.setCurrentPlayerIndex(0);

        // Call nextPlayer and verify the state change
        Player nextPlayer = gameService.nextPlayer(gameState);
        assertEquals(1, gameState.getCurrentPlayerIndex());
        assertEquals(players.get(1), nextPlayer);

        // Call nextPlayer again to verify circular behavior
        nextPlayer = gameService.nextPlayer(gameState);
        assertEquals(2, gameState.getCurrentPlayerIndex());
        assertEquals(players.get(2), nextPlayer);

        // Simulate wrapping around to the first player
        gameState.setCurrentPlayerIndex(3);
        nextPlayer = gameService.nextPlayer(gameState);
        assertEquals(0, gameState.getCurrentPlayerIndex());
        assertEquals(players.get(0), nextPlayer);
    }

    /**
     * Test drawing a card from the draw pile.
     */
    @Test
    void testDrawCard() {
        // Create a valid deck with one card
        Stack<Card> deck = new Stack<>();
        Card cardToDraw = new Card(Suit.HEARTS, Rank.ACE);
        deck.push(cardToDraw);

        // Create a player with one card in hand
        List<Card> initialHand = new ArrayList<>();
        initialHand.add(new Card(Suit.SPADES, Rank.JACK));
        Player player = new Player("Player 1", initialHand);

        // Set up the game state
        GameState gameState = new GameState();
        gameState.setDeck(deck);
        gameState.setPlayers(List.of(player));
        gameState.setCurrentPlayerIndex(0);

        // Draw the card
        Card drawnCard = gameService.drawCard(gameState, player);

        // Verify the card was drawn correctly
        assertEquals(cardToDraw, drawnCard);
        assertEquals(2, player.getHand().size());
        assertTrue(player.getHand().contains(cardToDraw));
        assertTrue(player.getHand().contains(new Card(Suit.SPADES, Rank.JACK)));
        assertTrue(gameState.getDeck().isEmpty());

        // Test drawing from an empty deck
        assertThrows(IllegalStateException.class, () -> gameService.drawCard(gameState, player));
    }

    /**
     * Test playing a card from the player's hand.
     */
    @Test
    void testPlayCard() {
        // Create a player with a hand containing the card to play
        Card cardToPlay = new Card(Suit.HEARTS, Rank.ACE);
        Card anotherCard = new Card(Suit.SPADES, Rank.EIGHT);
        List<Card> initialHand = new ArrayList<>();
        initialHand.add(cardToPlay);
        initialHand.add(anotherCard);
        Player player = new Player("Player 1", initialHand);

        // Set up the game state with a discard pile containing one card
        Stack<Card> discardPile = new Stack<>();
        discardPile.push(new Card(Suit.CLUBS, Rank.TEN)); // Initial card in the discard pile

        GameState gameState = new GameState();
        gameState.setPlayers(List.of(player));
        gameState.setDiscardPile(discardPile);

        // Play the card
        gameService.playCard(player, cardToPlay, gameState);

        // Verify the card was played correctly
        assertEquals(1, player.getHand().size()); // Player's hand should be empty
        assertTrue(player.getHand().contains(anotherCard)); // Remaining card should be the one not played
        assertEquals(cardToPlay, gameState.getDiscardPile().peek()); // Discard pile should contain the played card
        assertEquals(2, gameState.getDiscardPile().size()); // Discard pile should have two cards

        // Test playing a card that the player does not have
        Card cardNotInHand = new Card(Suit.SPADES, Rank.NINE);
        assertThrows(IllegalArgumentException.class, () -> gameService.playCard(player, cardNotInHand, gameState));
    }

    /**
     * Test checking if a player has won the game.
     */
    @Test
    void testCheckWinner() {
        // Create a player with an empty hand (winning condition)
        Player winningPlayer = new Player("Player 1", new ArrayList<>());

        // Create a player with cards in hand (non-winning condition)
        List<Card> hand = new ArrayList<>();
        hand.add(new Card(Suit.HEARTS, Rank.ACE));
        Player nonWinningPlayer = new Player("Player 2", hand);

        // Check if the winning player has won
        boolean hasWon = gameService.checkWinner(winningPlayer);
        assertTrue(hasWon);

        // Check if the non-winning player has not won
        hasWon = gameService.checkWinner(nonWinningPlayer);
        assertFalse(hasWon);
    }
}
