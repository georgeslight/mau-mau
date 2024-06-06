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
        Player player = new Player("Player", List.of(
                new Card(Suit.HEARTS, Rank.SEVEN),
                new Card(Suit.CLUBS, Rank.EIGHT),
                new Card(Suit.SPADES, Rank.NINE),
                new Card(Suit.DIAMONDS, Rank.TEN),
                new Card(Suit.CLUBS, Rank.JACK)));

        Card playedCard = new Card(Suit.HEARTS, Rank.ACE);
        Card antoherCard = new Card(Suit.DIAMONDS, Rank.ACE);
        List<Card> cards = new ArrayList<>();
        cards.add(playedCard);
        cards.add(antoherCard);
        player.setHand(cards);
//        gameService.playCard(player, playedCard, gameState);
//        assertTrue(player.getHand().contains(antoherCard));// Player still has the other card
//        assertFalse(player.getHand().contains(playedCard));// Player no longer has the played card
//        assertEquals(playedCard, gameState.getDiscardPile().peek());// Played card is now on discard pile
    }

    /**
     * Test checking if a player has won the game.
     */
    @Test
    void testCheckWinner() {
        Player player = new Player("Player", List.of(
                new Card(Suit.HEARTS, Rank.SEVEN),
                new Card(Suit.CLUBS, Rank.EIGHT),
                new Card(Suit.SPADES, Rank.NINE),
                new Card(Suit.DIAMONDS, Rank.TEN),
                new Card(Suit.CLUBS, Rank.JACK)));

        Player player2 = new Player("Player 2", List.of(
                new Card(Suit.SPADES, Rank.QUEEN),
                new Card(Suit.DIAMONDS, Rank.KING),
                new Card(Suit.HEARTS, Rank.ACE),
                new Card(Suit.CLUBS, Rank.SEVEN),
                new Card(Suit.SPADES, Rank.EIGHT)));

        player.setHand(Collections.emptyList()); // No cards left
        assertTrue(gameService.checkWinner(player));
        player2.setHand(List.of(new Card(Suit.HEARTS, Rank.ACE)));
        assertFalse(gameService.checkWinner(player2));
    }
}
