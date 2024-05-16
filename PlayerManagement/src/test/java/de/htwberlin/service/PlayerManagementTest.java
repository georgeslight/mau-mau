package de.htwberlin.service;

import de.htwberlin.enums.Rank;
import de.htwberlin.enums.Suit;
import de.htwberlin.model.Card;
import de.htwberlin.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerManagementTest {

    private PlayerManagement playerService;

    @BeforeEach
    void setUp() {
        playerService = new PlayerManagement();
    }

    /**
     * Tests the creation of a player.
     */
    @Test
    void createPlayer() {
        PlayerManagement playerManagement = new PlayerManagement();
        Player player = playerManagement.createPlayer();
        assertNotNull(player);
        player.setName("Player 1");
        assertEquals("Player 1", player.getName());
        assertEquals(5, player.getHand().size());
    }

    /**
     * Tests the surrender operation for a player.
     * It ensures that a player can be surrendered without errors.
     */
    @Test
    void testEndRound() {
        Player player = new Player(new ArrayList<>());
        player.setHand(List.of(new Card(Suit.HEARTS, Rank.ACE),
                new Card(Suit.SPADES, Rank.KING),
                new Card(Suit.DIAMONDS, Rank.SEVEN),
                new Card(Suit.SPADES, Rank.QUEEN),
                new Card(Suit.CLUBS, Rank.EIGHT)));
        int rankingPointsBeforeSurrender = player.getRankingPoints();
        playerService.endRound(player);
        int rankingPointsAfterSurrender = player.getRankingPoints();
        assertTrue(rankingPointsBeforeSurrender < rankingPointsAfterSurrender);
        assertEquals(player.getScore()[player.getScore().length], (int) player.getRankingPoints());
        assertEquals(33, player.getRankingPoints());
    }


    /**
     * Test if player says mau changes boolean state
     */
    @Test
    void mau() {
        //        test if player has only one card, if one card keep playing
        Player playerWithOneCard = new Player("Player 1");
        List<Card> oneCardHand = List.of(new Card(Suit.HEARTS, Rank.ACE));
        playerWithOneCard.setHand(oneCardHand);
        playerService.mau(playerWithOneCard);
        assertTrue(playerWithOneCard.isSaidMau());
        assertEquals(1, playerWithOneCard.getHand().size());

        Player playerWithMultipleCards = new Player("Player 1");
        List<Card> multipleCardsHand = List.of(
                new Card(Suit.HEARTS, Rank.ACE),
                new Card(Suit.SPADES, Rank.KING)
        );
        playerWithMultipleCards.setHand(multipleCardsHand);
        playerService.mau(playerWithMultipleCards);
        assertFalse(playerWithMultipleCards.isSaidMau(), "Player should not be able to say Mau with more than one card.");
        assertEquals(3, playerWithMultipleCards.getHand().size());
    }

    /**
     * Tests handling of a lost "mau" scenario for a player.
     * It ensures that the lost "mau" scenario can be handled without errors.
     */
    @Test
    void lostMau() {
        Player playerWithOneCard = new Player("Player 1");
        List<Card> oneCardHand = List.of(new Card(Suit.HEARTS, Rank.ACE));
        playerWithOneCard.setHand(oneCardHand);
        playerService.lostMau(playerWithOneCard);
        assertEquals(3, playerWithOneCard.getHand().size());

        // test player with only one card, doesnt click mau, and ahs to draw moire cards
    } // todo george -> lostMau if player one card and didnt says mau, draws cards (number irrelevant)
        // if player says mau with more than one card throw exception oder so was

    /**
     * Order is CLUBS, DIAMONDS, HEARTS, SPADES and Rank asc
     */
    @Test
    void sortPlayersCards() {
        List<Card> hand = new ArrayList<>();
        hand.add(new Card(Suit.SPADES, Rank.TEN));
        hand.add(new Card(Suit.HEARTS, Rank.SEVEN));
        hand.add(new Card(Suit.CLUBS, Rank.ACE));
        hand.add(new Card(Suit.SPADES, Rank.SEVEN));
        hand.add(new Card(Suit.HEARTS, Rank.KING));
        hand.add(new Card(Suit.DIAMONDS, Rank.KING));

        Player player = new Player(hand);
        playerService.sortPlayersCards(player);

        List<Card> sortedHand = player.getHand();

        assertEquals(new Card(Suit.CLUBS, Rank.ACE), sortedHand.get(0));
        assertEquals(new Card(Suit.DIAMONDS, Rank.KING), sortedHand.get(1));
        assertEquals(new Card(Suit.HEARTS, Rank.SEVEN), sortedHand.get(2));
        assertEquals(new Card(Suit.HEARTS, Rank.KING), sortedHand.get(3));
        assertEquals(new Card(Suit.SPADES, Rank.SEVEN), sortedHand.get(4));
        assertEquals(new Card(Suit.SPADES, Rank.TEN), sortedHand.get(5));
    }
}
