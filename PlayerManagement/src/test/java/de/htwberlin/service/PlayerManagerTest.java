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

class PlayerManagerTest {

    private PlayerManagement playerService;

    @BeforeEach
    void setUp() {
        playerService = new PlayerManagement();
    }

    /**
     * Tests the creation of a player.
     * It verifies that the createPlayer method of PlayerManagement produces a non-null player object.
     */
    @Test
    void createPlayer() {
        PlayerManagement playerManagement = new PlayerManagement();
        Player player = playerManagement.createPlayer();
        assertNotNull(player);
    }


    /**
     * Order is 1. CLUBS, 2. DIAMONDS, 3. HEARTS, 4. SPADES and Rank is asc
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

    /**
     * Tests the surrender operation for a player.
     * It ensures that a player can be surrendered without errors.
     */

    @Test
    void surrender() {
        PlayerManagement playerManagement = new PlayerManagement();
        Player player = new Player(new ArrayList<>());
        playerManagement.surrender(player);
    }


    /**
     * Tests the "mau" action for a player.
     * It verifies that a player can perform the "mau" action without errors.
     */
    @Test
    void mau() {
        PlayerManagement playerManagement = new PlayerManagement();
        Player player = new Player(new ArrayList<>());
        playerManagement.mau(player);

    }

    /**
     * Tests handling of a lost "mau" scenario for a player.
     * It ensures that the lost "mau" scenario can be handled without errors.
     */
    @Test
    void lostMau() {
        PlayerManagement playerManagement = new PlayerManagement();
        Player player = new Player(new ArrayList<>());
        playerManagement.lostMau(player);
    }
}
