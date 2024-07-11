package de.htwberlin.playermanagement;

import de.htwberlin.cardsmanagement.api.enums.Rank;
import de.htwberlin.cardsmanagement.api.enums.Suit;
import de.htwberlin.cardsmanagement.api.model.Card;
import de.htwberlin.cardsmanagement.api.service.CardManagerInterface;
import de.htwberlin.cardsmanagement.impl.CardService;
import de.htwberlin.playermanagement.api.model.Player;
import de.htwberlin.playermanagement.impl.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerManagementTest {

    private CardManagerInterface cardService;
    private PlayerService playerService;


    @BeforeEach
    void setUp() {
        this.cardService = new CardService();
        this.playerService = new PlayerService();
    }

    /**
     * Tests the creation of a player.
     */


    @Test
    void createPlayer() {
        Player player = playerService.createPlayer("Player 1", List.of(cardService.createCard(Suit.CLUBS, Rank.EIGHT),
                cardService.createCard(Suit.SPADES, Rank.JACK),
                cardService.createCard(Suit.DIAMONDS, Rank.NINE),
                cardService.createCard(Suit.CLUBS, Rank.QUEEN),
                cardService.createCard(Suit.HEARTS, Rank.QUEEN)));
        assertNotNull(player);
        assertEquals("Player 1", player.getName());
        assertEquals(5, player.getHand().size());
    }



    /**
     * Test if player says mau changes boolean state
     */
    @Test
    void mau() {
        //        test if player has only one card, if one card keep playing
        Player playerWithTwoCard = new Player("Player 1", List.of(cardService.createCard(Suit.HEARTS, Rank.ACE), new Card(Suit.SPADES, Rank.KING)));
        playerService.mau(playerWithTwoCard);
        assertTrue(playerWithTwoCard.isSaidMau());
        assertEquals(2, playerWithTwoCard.getHand().size());

        Player playerWithMultipleCards = new Player("Player 1", List.of(cardService.createCard(Suit.HEARTS, Rank.ACE), cardService.createCard(Suit.SPADES, Rank.KING), cardService.createCard(Suit.SPADES, Rank.JACK)));
        playerService.mau(playerWithMultipleCards);
        assertFalse(playerWithMultipleCards.isSaidMau(), "Player should not be able to say Mau with more than two card.");
        assertEquals(3, playerWithMultipleCards.getHand().size());
    }

    /**
     * Order is CLUBS, DIAMONDS, HEARTS, SPADES and Rank asc
     */
    @Test
    void sortPlayersCards() {
        List<Card> hand = new ArrayList<>();
        hand.add(cardService.createCard(Suit.SPADES, Rank.TEN));
        hand.add(cardService.createCard(Suit.HEARTS, Rank.SEVEN));
        hand.add(cardService.createCard(Suit.CLUBS, Rank.ACE));
        hand.add(cardService.createCard(Suit.SPADES, Rank.SEVEN));
        hand.add(cardService.createCard(Suit.HEARTS, Rank.KING));
        hand.add(cardService.createCard(Suit.DIAMONDS, Rank.KING));

        Player player = new Player("test", hand);
        playerService.sortPlayersCards(player);

        List<Card> sortedHand = player.getHand();

        assertEquals(cardService.createCard(Suit.CLUBS, Rank.ACE), sortedHand.get(0));
        assertEquals(cardService.createCard(Suit.DIAMONDS, Rank.KING), sortedHand.get(1));
        assertEquals(cardService.createCard(Suit.HEARTS, Rank.SEVEN), sortedHand.get(2));
        assertEquals(cardService.createCard(Suit.HEARTS, Rank.KING), sortedHand.get(3));
        assertEquals(cardService.createCard(Suit.SPADES, Rank.SEVEN), sortedHand.get(4));
        assertEquals(cardService.createCard(Suit.SPADES, Rank.TEN), sortedHand.get(5));
    }
//    /**
//     * Tests the surrender operation for a player.
//     * It ensures that a player can be surrendered without errors.
//     */
//    @Test
//    void testEndRound() {
//        Player player = new Player("test", List.of(new Card(Suit.HEARTS, Rank.ACE),
//                new Card(Suit.SPADES, Rank.KING),
//                new Card(Suit.DIAMONDS, Rank.SEVEN),
//                new Card(Suit.SPADES, Rank.QUEEN),
//                new Card(Suit.CLUBS, Rank.EIGHT)));
//        int rankingPointsBeforeSurrender = player.getRankingPoints();
//        playerService.endRound(player);
//        int rankingPointsAfterSurrender = player.getRankingPoints();
//        assertTrue(rankingPointsBeforeSurrender < rankingPointsAfterSurrender);
//        assertEquals(player.getScore().get(player.getScore().size()), player.getRankingPoints());
//        assertEquals(33, player.getRankingPoints())
//        //todo: move lost mau tests into game engine (as recommended by prof)
//    }


//
//    /**
//     * Tests the scenario where a player has one card left and fails to say "Mau."
//     * The player should draw two cards as a penalty.
//     */
//    @Test
//    void testLostMauWithOneCardAndDidNotSayMau() {
//        Player playerWithOneCard = new Player("Player 1", List.of(new Card(Suit.HEARTS, Rank.ACE)));
//        playerWithOneCard.setSaidMau(false);
//        playerService.lostMau(playerWithOneCard);
//        assertEquals(3, playerWithOneCard.getHand().size()); // player has to draw to cards if mau fails.
//    }
//
//    /**
//     * Tests the scenario where a player has more than one card and incorrectly says "Mau."
//     * An IllegalStateException should be thrown.
//     */
//    @Test
//    void testPlayerMoreThanOneCardAndSaidMau() {
//        Player player = new Player("Player 2", List.of(new Card(Suit.HEARTS, Rank.ACE), new Card(Suit.CLUBS, Rank.QUEEN)));
//        player.setSaidMau(true); //todo: instead of setting the variable, use the mau method?
//
//        Exception exception = assertThrows(IllegalStateException.class, () -> playerService.lostMau(player));
//        //todo: create a new exception for this case?

//
//        String expectedMessage = "Player cannot say Mau with more than one card.";
//        String actualMessage = exception.getMessage();
//
//        assertTrue(actualMessage.contains(expectedMessage));
//    }
}
