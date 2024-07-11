package de.htwberlin.persistence;

import de.htwberlin.cardsmanagement.api.enums.Rank;
import de.htwberlin.cardsmanagement.api.enums.Suit;
import de.htwberlin.cardsmanagement.api.model.Card;
import de.htwberlin.persistence.service.CardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@ActiveProfiles("test")
@SpringJUnitConfig(classes = PersistenceJPAConfig.class)
public class CardRepositoryTest {

    @Autowired
    private CardService cardService;

    @BeforeEach
    public void setUp() {
        cardService.deleteAllCards();
    }

    @Test
    public void testSaveCard() {
        // Create a new Card entity
        Card card = new Card(Suit.HEARTS, Rank.ACE);

        // Save the Card entity in db
        Card savedCard = cardService.saveCard(card);

        // Verify
        assertNotNull(savedCard);
        assertNotNull(savedCard.getId());
        assertEquals(Suit.HEARTS, savedCard.getSuit());
        assertEquals(Rank.ACE, savedCard.getRank());
    }

    @Test
    public void testFindById() {
        // Create and save a new Card entity
        Card card = new Card(Suit.CLUBS, Rank.KING);
        Card savedCard = cardService.saveCard(card);

        // Retrieve the Card entity by its ID
        Optional<Card> retrievedCard = cardService.findCardById(savedCard.getId());

        // Verify
        assertTrue(retrievedCard.isPresent());
        assertEquals(Suit.CLUBS, retrievedCard.get().getSuit());
        assertEquals(Rank.KING, retrievedCard.get().getRank());
    }

    @Test
    public void testFindAll() {
        // Save multiple Card entities
        Card card1 = new Card(Suit.CLUBS, Rank.KING);
        Card card2 = new Card(Suit.SPADES, Rank.QUEEN);
        cardService.saveCard(card1);
        cardService.saveCard(card2);

        // Retrieve all Card entities
        List<Card> cards = cardService.findAllCards();

        // Verify
        assertEquals(2, cards.size());
        assertTrue(cards.contains(card1));
        assertTrue(cards.contains(card2));
    }

    @Test
    public void testUpdateCard() {
        // Create and save a new Card entity
        Card card = new Card(Suit.HEARTS, Rank.ACE);
        Card savedCard = cardService.saveCard(card);

        // Update the Card entity
        savedCard.setSuit(Suit.DIAMONDS);
        Card updatedCard = cardService.saveCard(savedCard);

        // Verify
        assertEquals(Suit.DIAMONDS, updatedCard.getSuit());
        assertEquals(Rank.ACE, updatedCard.getRank());
    }

    @Test
    public void testDeleteById() {
        // Create and save a new Card entity
        Card card = new Card(Suit.HEARTS, Rank.ACE);
        Card savedCard = cardService.saveCard(card);

        // Delete the Card entity by its ID
        cardService.deleteCardById(savedCard.getId());

        // Verify
        Optional<Card> retrievedCard = cardService.findCardById(savedCard.getId());
        assertFalse(retrievedCard.isPresent());
    }

    @Test
    public void testDeleteAll() {
        // Save multiple Card entities
        Card card1 = new Card(Suit.CLUBS, Rank.KING);
        Card card2 = new Card(Suit.SPADES, Rank.QUEEN);
        cardService.saveCard(card1);
        cardService.saveCard(card2);

        // Delete all Card entities
        cardService.deleteAllCards();

        // Verify
        List<Card> cards = cardService.findAllCards();
        assertTrue(cards.isEmpty());
    }

    @Test
    public void testCount() {
        // Save multiple Card entities
        Card card1 = new Card(Suit.CLUBS, Rank.KING);
        Card card2 = new Card(Suit.SPADES, Rank.QUEEN);
        cardService.saveCard(card1);
        cardService.saveCard(card2);

        // Count the number of Card entities
        long count = cardService.countCards();

        // Verify
        assertEquals(2, count);
    }

    @Test
    public void testExistsById() {
        // Create and save a new Card entity
        Card card = new Card(Suit.HEARTS, Rank.ACE);
        Card savedCard = cardService.saveCard(card);

        // Check if the Card entity exists by its ID
        boolean exists = cardService.existsCardById(savedCard.getId());

        // Verify
        assertTrue(exists);

        // Delete the Card entity by its ID
        cardService.deleteCardById(savedCard.getId());

        // Verify
        exists = cardService.existsCardById(savedCard.getId());
        assertFalse(exists);
    }
}
