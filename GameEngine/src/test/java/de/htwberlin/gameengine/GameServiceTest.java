package de.htwberlin.gameengine;

import de.htwberlin.cardsmanagement.api.enums.Rank;
import de.htwberlin.cardsmanagement.api.enums.Suit;
import de.htwberlin.cardsmanagement.api.model.Card;
import de.htwberlin.gameengine.api.model.GameState;
import de.htwberlin.playermanagement.api.model.Player;
import de.htwberlin.cardsmanagement.api.service.CardManagerInterface;
import de.htwberlin.gameengine.api.service.GameManagerInterface;
import de.htwberlin.playermanagement.api.service.PlayerManagerInterface;
import de.htwberlin.rulesmanagement.api.model.Rules;
import de.htwberlin.rulesmanagement.api.service.RuleEngineInterface;
import de.htwberlin.gameengine.impl.GameService;
import de.htwberlin.virtualplayer.api.service.VirtualPlayerInterface;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private PlayerManagerInterface playerManagerInterface;
    @Mock
    private CardManagerInterface cardManagerInterface;
    @Mock
    private RuleEngineInterface ruleEngineInterface;
    @Mock
    private VirtualPlayerInterface virtualPlayerInterface;


    @InjectMocks
    private GameManagerInterface gameService = new GameService();

    /**
     * Test initializing the game with a specified number of players.
     */
    @Test
    void testInitGame() {
        String playerName = "Max";
        // Create a valid deck of cards
        List<Card> deck = Stream.of(Suit.values())
                .flatMap(suit -> Stream.of(Rank.values())
                        .map(rank -> new Card(suit, rank)))
                .collect(Collectors.toList());

        // Define the behavior of the cardService mocks
        when(cardManagerInterface.createDeck()).thenReturn(deck);
        when(cardManagerInterface.shuffle(any())).thenAnswer(invocation -> {
            List<Card> originalDeck = invocation.getArgument(0);
            Collections.shuffle(originalDeck);
            return originalDeck;
        });

        // Define the behavior of the playerService mock to return valid players
        when(playerManagerInterface.createPlayer(anyString(), anyList(), anyBoolean())).thenAnswer(invocation -> {
            String name = invocation.getArgument(0);
            List<Card> hand = invocation.getArgument(1);
            return new Player(name, hand, false);
        });


        int playersCount = 4;
        GameState gameState = gameService.initializeGame(playerName, playersCount);

        // Verify interactions with the mocks
        verify(cardManagerInterface).createDeck();
        verify(cardManagerInterface).shuffle(deck);
        verify(playerManagerInterface, times(playersCount)).createPlayer(anyString(), anyList(), anyBoolean());

        gameState.getPlayers().forEach(player -> assertEquals(5, player.getHand().size())); // checks that every player has 5 cards on hand
        assertEquals(32 - (playersCount * 5) - 1, gameState.getDeck().size()); // checks deck has 32 cards - 5 cards for every player - initial discardPile card
        assertEquals(playersCount, gameState.getPlayers().size());
        assertEquals(0, gameState.getCurrentPlayerIndex());
    }

    /**
     * Test moving to the next player in the sequence.
     */
    @Test
    void testNextPlayer() {
        GameState gameState = new GameState();
        // Mock the RuleService to calculate the next player index
        when(ruleEngineInterface.calculateNextPlayerIndex(anyInt(), anyInt(), eq(gameState.getRules()))).thenAnswer(invocation -> {
            int currentIndex = invocation.getArgument(0);
            int totalPlayers = invocation.getArgument(1);
            return (currentIndex + 1) % totalPlayers;
        });

        // Set up a GameState with 4 players
        List<Player> players = IntStream.range(0, 4)
                .mapToObj(i -> new Player("Player " + i, new ArrayList<>(), false))
                .collect(Collectors.toList());

        gameState.setPlayers(players);
        gameState.setCurrentPlayerIndex(0);

        // Call nextPlayer and verify the state change
        Player nextPlayer = gameService.nextPlayer(gameState);

        // Verify interactions with the mocks
        verify(ruleEngineInterface).calculateNextPlayerIndex(eq(0), eq(4), eq(gameState.getRules()));

        assertEquals(1, gameState.getCurrentPlayerIndex());
        assertEquals(players.get(1), nextPlayer);

        // Call nextPlayer again to verify circular behavior
        nextPlayer = gameService.nextPlayer(gameState);

        // Verify interactions with the mocks
        verify(ruleEngineInterface).calculateNextPlayerIndex(eq(1), eq(4), eq(gameState.getRules()));

        assertEquals(2, gameState.getCurrentPlayerIndex());
        assertEquals(players.get(2), nextPlayer);

        // Simulate wrapping around to the first player
        gameState.setCurrentPlayerIndex(3);
        nextPlayer = gameService.nextPlayer(gameState);

        // Verify interactions with the mocks
        verify(ruleEngineInterface).calculateNextPlayerIndex(eq(3), eq(4), eq(gameState.getRules()));

        assertEquals(0, gameState.getCurrentPlayerIndex());
        assertEquals(players.get(0), nextPlayer);
    }

    /**
     * Test drawing a card from the draw pile.
     */
    @Test
    void testDrawCard() {
        // Create a valid deck with one card
        List<Card> deck = new ArrayList<>();
        Card cardToDraw = new Card(Suit.HEARTS, Rank.ACE);
        deck.add(cardToDraw);

        // Create a player with one card in hand
        List<Card> initialHand = new ArrayList<>();
        initialHand.add(new Card(Suit.SPADES, Rank.JACK));
        Player player = new Player("Player 1", initialHand, false);

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

        // Add an additional check for reshuffling
        // Empty the deck and add cards to the discard pile for reshuffling
        gameState.setDeck(new ArrayList<>()); // Empty the deck
        List<Card> discardPile = new ArrayList<>();
        discardPile.add(new Card(Suit.CLUBS, Rank.SEVEN));
        discardPile.add(new Card(Suit.DIAMONDS, Rank.EIGHT));
        gameState.setDiscardPile(discardPile);

        // Mock the behavior of reshuffling
        List<Card> shuffledDeck = new ArrayList<>();
        shuffledDeck.add(new Card(Suit.DIAMONDS, Rank.EIGHT));
        shuffledDeck.add(new Card(Suit.CLUBS, Rank.SEVEN));
        when(cardManagerInterface.shuffle(anyList())).thenReturn(shuffledDeck);

        // Verify no interaction with the card manager
        verify(cardManagerInterface, times(0)).shuffle(anyList());

        // Draw a card which should trigger reshuffling
        drawnCard = gameService.drawCard(gameState, player);

        // Verify interaction with the card manager for reshuffling
        verify(cardManagerInterface).shuffle(anyList());

        // Verify the reshuffled card
        assertEquals(new Card(Suit.CLUBS, Rank.SEVEN), drawnCard); // The last card in the discard pile becomes the top card in the deck after reshuffling
        assertEquals(3, player.getHand().size());
        assertTrue(player.getHand().contains(new Card(Suit.CLUBS, Rank.SEVEN)));
        assertEquals(1, gameState.getDeck().size());
        assertEquals(new Card(Suit.DIAMONDS, Rank.EIGHT), gameState.getDeck().get(gameState.getDeck().size() - 1));
        assertEquals(1, gameState.getDiscardPile().size());
        assertEquals(new Card(Suit.DIAMONDS, Rank.EIGHT), gameState.getDiscardPile().remove(gameState.getDeck().size() - 1));

        // Draw the last card in the reshuffled deck
        drawnCard = gameService.drawCard(gameState, player);
        assertEquals(new Card(Suit.DIAMONDS, Rank.EIGHT), drawnCard);
        assertEquals(4, player.getHand().size());
        assertTrue(player.getHand().contains(new Card(Suit.CLUBS, Rank.SEVEN)));
        assertTrue(gameState.getDeck().isEmpty());
    }

    /**
     * Test playing a card from the player's hand.
     */
    @Test
    void testPlayCard() {
        // Create a player with a hand containing the card to play
        Card cardToPlay = new Card(Suit.HEARTS, Rank.TEN);
        Card anotherCard = new Card(Suit.SPADES, Rank.EIGHT);
        List<Card> initialHand = new ArrayList<>();
        initialHand.add(cardToPlay);
        initialHand.add(anotherCard);
        Player player = new Player("Player 1", initialHand, false);

        // Set up the game state with a discard pile containing one card
        List<Card> discardPile = new ArrayList<>();
        discardPile.add(new Card(Suit.CLUBS, Rank.TEN)); // Initial card in the discard pile

        GameState gameState = new GameState();
        gameState.setPlayers(List.of(player));
        gameState.setDiscardPile(discardPile);
        gameState.setRules(new Rules());

        // Play the card
        gameService.playCard(player, cardToPlay, gameState);

        // Verify the card was played correctly
        assertEquals(1, player.getHand().size()); // Player's hand should be empty
        assertTrue(player.getHand().contains(anotherCard)); // Remaining card should be the one not played
        assertEquals(cardToPlay, gameState.getDiscardPile().get(gameState.getDiscardPile().size() - 1)); // Discard pile should contain the played card
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
        GameState gameState = new GameState();
        // Create a player with an empty hand (winning condition)
        Player winningPlayer = new Player("Player 1", new ArrayList<>(), false);

        // Create a player with cards in hand (non-winning condition)
        List<Card> hand = new ArrayList<>();
        hand.add(new Card(Suit.HEARTS, Rank.ACE));
        Player nonWinningPlayer = new Player("Player 2", hand, false);

        gameState.setPlayers(List.of(winningPlayer, nonWinningPlayer));
        // Check if the winning player has won
        gameService.calcRankingPoints(gameState);
        assertFalse(gameService.checkEmptyHand(winningPlayer) && winningPlayer.isSaidMau());
        winningPlayer.setSaidMau(true);
        assertTrue(gameService.checkEmptyHand(winningPlayer) && winningPlayer.isSaidMau());

        // Check if the non-winning player has not wongameManagerInterface.checkWinner(nonWinningPlayer)
        assertFalse(gameService.checkEmptyHand(nonWinningPlayer));
    }

    /**
     * Test ending the game and determining the winner.
     */
    @Test
    void testEndGame() {
        // Create players with different scores
        Player player1 = new Player("Player 1", new ArrayList<>(),false);
        player1.setScore(Arrays.asList(-10, -15, -20)); // total: -45

        Player player2 = new Player("Player 2", new ArrayList<>(), false);
        player2.setScore(Arrays.asList(-5, -10, -15)); // total: -30

        Player player3 = new Player("Player 3", new ArrayList<>(), false);
        player3.setScore(Arrays.asList(-20, -25, -30)); // total: -75

        // Set up the game state
        GameState gameState = new GameState();
        gameState.setPlayers(Arrays.asList(player1, player2, player3));

        // End the game and determine the winner
        gameService.calcRankingPoints(gameState);
        Player winner = gameService.getWinner(gameState);

        // Verify the winner
        assertNotNull(winner);
        assertEquals(player2, winner); // Player 2 has the highest total score
        assertEquals(-30, winner.getRankingPoints());
    }

    /**
     * Test ending the round and reinitializing game state.
     */
    @Test
    void testEndRound() {
        GameState gameState = new GameState();
        //test updated rankingPoints & test players have 5 cards each & test Deck Pile is full - 1 & test discard pile is 1
        // Create players with different scores
        Player player1 = new Player("Player 1", new ArrayList<>(), false);
        player1.setScore(Arrays.asList(10, 15, 20)); // total: 45

        Player player2 = new Player("Player 2", new ArrayList<>(), false);
        player2.setScore(Arrays.asList(5, 10, 15)); // total: 30

        Player player3 = new Player("Player 3", new ArrayList<>(), false);
        player3.setScore(Arrays.asList(20, 25, 30)); // total: 75
        gameState.setPlayers(Arrays.asList(player1, player2, player3));

        // Set up the game state
        List<Card> deck = Stream.of(Suit.values())
                .flatMap(suit -> Stream.of(Rank.values())
                        .map(rank -> new Card(suit, rank)))
                .collect(Collectors.toList());

        when(cardManagerInterface.createDeck()).thenReturn(deck);
        when(cardManagerInterface.shuffle(any())).thenAnswer(invocation -> {
            List<Card> originalDeck = invocation.getArgument(0);
            Collections.shuffle(originalDeck);
            return originalDeck;
        });

        when(ruleEngineInterface.calculateScore(anyList())).thenReturn(anyInt());
        int playersCount = gameState.getPlayers().size();
        gameState.setDiscardPile(new ArrayList<>());
        gameService.endRound(gameState);

        // Verify the interactions
        verify(cardManagerInterface).createDeck();
        verify(cardManagerInterface).shuffle(deck);
        verify(ruleEngineInterface, times(playersCount)).calculateScore(anyList());

        // Verify the game state after ending the round
        gameState.getPlayers().forEach(player -> assertEquals(5, player.getHand().size())); // checks that every player has 5 cards on hand
        assertEquals(32 - (playersCount * 5) - 1, gameState.getDeck().size()); // checks deck has 32 cards - 5 cards for every player - initial discardPile card
        assertEquals(playersCount, gameState.getPlayers().size());
        assertEquals(0, gameState.getCurrentPlayerIndex());
    }
}
