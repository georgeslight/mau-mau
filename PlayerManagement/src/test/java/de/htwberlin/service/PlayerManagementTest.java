package de.htwberlin.service;

import de.htwberlin.enums.Rank;
import de.htwberlin.enums.Suit;
import de.htwberlin.model.Card;
import de.htwberlin.model.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerManagementTest {
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


    @Test
    void sortPlayersCards() {
        PlayerManagement playerManagement = new PlayerManagement();
        Player player = new Player();
        List<Card> unsortedCards = List.of(new Card(Suit.HEARTS, Rank.JACK), new Card(Suit.SPADES, Rank.TEN), new Card(Suit.DIAMONDS, Rank.ACE));
        player.setHand(unsortedCards);
        List<Card> sortedCards = playerManagement.sortPlayersCards(player);
        assertArrayEquals(new Card[] {
                new Card(Suit.SPADES, Rank.TEN),
                new Card(Suit.HEARTS, Rank.JACK),
                new Card(Suit.DIAMONDS, Rank.ACE)
        }, sortedCards.toArray());
    }

    /**
     * Testet die Funktion calculateTotalScore(), um sicherzustellen, dass die Gesamtpunktzahl eines Spielers korrekt berechnet wird.
     */
    @Test
    void calculateTotalScore_CorrectCalculation_Success() {
        PlayerManagement playerManagement = new PlayerManagement();
        Player player = new Player();
        int[] score = {50, 100, 150}; // Beispielwert für die Punktzahl
        player.setScore(score);
        int totalScore = playerManagement.calculateTotalScore(player);
        assertEquals(300, totalScore); // Überprüfe, ob die Gesamtpunktzahl korrekt berechnet wurde
    }

    private List<Card> additionalCards = List.of(
            new Card(Suit.CLUBS, Rank.SEVEN),
            new Card(Suit.DIAMONDS, Rank.QUEEN),
            new Card(Suit.HEARTS, Rank.KING)
    );

    /**
     * Testet die Funktion addCardToHand(), um sicherzustellen, dass eine Karte korrekt der Hand eines Spielers hinzugefügt wird.
     */
    @Test
    void addCardToHand_CardAddedToHand_Success() {
        PlayerManagement playerManagement = new PlayerManagement();
        Player player = new Player();
        Card newCard = new Card(Suit.SPADES, Rank.QUEEN);
        playerManagement.addCardToHand(player, newCard);
        assertTrue(player.getHand().contains(newCard));
    }

    /**
     * Testet die Funktion removeCardFromHand(), um sicherzustellen, dass eine Karte korrekt aus der Hand eines Spielers entfernt wird.
     */
    @Test
    void removeCardFromHand_CardRemovedFromHand_Success() {
        PlayerManagement playerManagement = new PlayerManagement();
        List<Card> cards = new ArrayList<>(additionalCards);
        Player player = new Player(cards);
        Card cardToRemove = additionalCards.get(1); // Wähle eine Karte aus der Liste aus
        playerManagement.removeCardFromHand(player, cardToRemove);
        assertFalse(player.getHand().contains(cardToRemove));
    }



}