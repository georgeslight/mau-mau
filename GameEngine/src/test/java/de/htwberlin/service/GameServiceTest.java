package de.htwberlin.service;

import de.htwberlin.api.model.GameState;
import de.htwberlin.api.model.Player;
import de.htwberlin.api.service.CardManagerInterface;
import de.htwberlin.api.service.GameManagerInterface;
import de.htwberlin.api.service.PlayerManagerInterface;
import de.htwberlin.api.service.RuleEngineInterface;
import de.htwberlin.impl.service.GameService;
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

    private GameManagerInterface gameManagerInterface;
    private PlayerManagerInterface playerManagerInterface;
    private CardManagerInterface cardManagerInterface;
    private RuleEngineInterface ruleEngineInterface;
    private GameState gameState;

    @BeforeEach
    void setUp() {
        this.playerManagerInterface = mock(PlayerManagerInterface.class);
        this.cardManagerInterface = mock(CardManagerInterface.class);
        this.ruleEngineInterface = mock(RuleEngineInterface.class);
        this.gameManagerInterface = new GameService(playerManagerInterface, cardManagerInterface, ruleEngineInterface);
        this.gameState = new GameState();

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
        when(cardManagerInterface.createDeck()).thenReturn(deck);
        when(cardManagerInterface.shuffle(any())).thenAnswer(invocation -> {
            Stack<Card> originalDeck = invocation.getArgument(0);
            Collections.shuffle(originalDeck);
            return originalDeck;
        });

        // Define the behavior of the playerService mock to return valid players
        when(playerManagerInterface.createPlayer(anyString(), anyList())).thenAnswer(invocation -> {
            String name = invocation.getArgument(0);
            List<Card> hand = invocation.getArgument(1);
            return new Player(name, hand);
        });

        int playersCount = 4;
        GameState gameState = gameManagerInterface.initializeGame(playersCount);
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
        when(ruleEngineInterface.calculateNextPlayerIndex(anyInt(), anyInt())).thenAnswer(invocation -> {
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
        Player nextPlayer = gameManagerInterface.nextPlayer(gameState);
        assertEquals(1, gameState.getCurrentPlayerIndex());
        assertEquals(players.get(1), nextPlayer);

        // Call nextPlayer again to verify circular behavior
        nextPlayer = gameManagerInterface.nextPlayer(gameState);
        assertEquals(2, gameState.getCurrentPlayerIndex());
        assertEquals(players.get(2), nextPlayer);

        // Simulate wrapping around to the first player
        gameState.setCurrentPlayerIndex(3);
        nextPlayer = gameManagerInterface.nextPlayer(gameState);
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
        Card drawnCard = gameManagerInterface.drawCard(gameState, player);

        // Verify the card was drawn correctly
        assertEquals(cardToDraw, drawnCard);
        assertEquals(2, player.getHand().size());
        assertTrue(player.getHand().contains(cardToDraw));
        assertTrue(player.getHand().contains(new Card(Suit.SPADES, Rank.JACK)));
        assertTrue(gameState.getDeck().isEmpty());

        // Test drawing from an empty deck
        assertThrows(IllegalStateException.class, () -> gameManagerInterface.drawCard(gameState, player));
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
        gameManagerInterface.playCard(player, cardToPlay, gameState);

        // Verify the card was played correctly
        assertEquals(1, player.getHand().size()); // Player's hand should be empty
        assertTrue(player.getHand().contains(anotherCard)); // Remaining card should be the one not played
        assertEquals(cardToPlay, gameState.getDiscardPile().peek()); // Discard pile should contain the played card
        assertEquals(2, gameState.getDiscardPile().size()); // Discard pile should have two cards

        // Test playing a card that the player does not have
        Card cardNotInHand = new Card(Suit.SPADES, Rank.NINE);
        assertThrows(IllegalArgumentException.class, () -> gameManagerInterface.playCard(player, cardNotInHand, gameState));
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
        boolean hasWon = gameManagerInterface.checkWinner(gameState ,winningPlayer);
        assertTrue(hasWon);

        // Check if the non-winning player has not won
        hasWon = gameManagerInterface.checkWinner(gameState,nonWinningPlayer);
        assertFalse(hasWon);
    }


    /**
     * Test ending the game and determining the winner.
     */
    @Test
    void testEndGame() {
        // Create players with different scores
        Player player1 = new Player("Player 1", new ArrayList<>());
        player1.setScore(Arrays.asList(10, 15, 20)); // total: 45

        Player player2 = new Player("Player 2", new ArrayList<>());
        player2.setScore(Arrays.asList(5, 10, 15)); // total: 30

        Player player3 = new Player("Player 3", new ArrayList<>());
        player3.setScore(Arrays.asList(20, 25, 30)); // total: 75

        // Set up the game state
        GameState gameState = new GameState();
        gameState.setPlayers(Arrays.asList(player1, player2, player3));

        // End the game and determine the winner
        Player winner = gameManagerInterface.endGame(gameState);

        // Verify the winner
        assertNotNull(winner);
        assertEquals(player3, winner); // Player 3 has the highest total score
        assertEquals(75, winner.getRankingPoints());
    }


    //todo Ghazi/ George: test that player who forgot to say mau, draws a penalty card.
}
