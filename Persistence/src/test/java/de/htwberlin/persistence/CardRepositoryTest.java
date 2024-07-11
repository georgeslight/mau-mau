package de.htwberlin.persistence;

import de.htwberlin.cardsmanagement.api.enums.Rank;
import de.htwberlin.cardsmanagement.api.enums.Suit;
import de.htwberlin.cardsmanagement.api.model.Card;
import de.htwberlin.persistence.repo.CardRepository;
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
    private CardRepository cardRepository;

    @BeforeEach
    public void setUp() {
        cardRepository.deleteAll();
    }

    @Test
    public void testSaveCard() {
        // Create a new Card entity
        Card card = new Card(Suit.HEARTS, Rank.ACE);

        // Save the Card entity in db
        Card savedCard = cardRepository.save(card);

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
        Card savedCard = cardRepository.save(card);

        // Retrieve the Card entity by its ID
        Optional<Card> retrievedCard = cardRepository.findById(savedCard.getId());

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
        cardRepository.save(card1);
        cardRepository.save(card2);

        // Retrieve all Card entities
        List<Card> cards = cardRepository.findAll();

        // Verify
        assertEquals(2, cards.size());
        assertTrue(cards.contains(card1));
        assertTrue(cards.contains(card2));
    }

    @Test
    public void testUpdateCard() {
        // Create and save a new Card entity
        Card card = new Card(Suit.HEARTS, Rank.ACE);
        Card savedCard = cardRepository.save(card);

        // Update the Card entity
        savedCard.setSuit(Suit.DIAMONDS);
        Card updatedCard = cardRepository.save(savedCard);

        // Verify
        assertEquals(Suit.DIAMONDS, updatedCard.getSuit());
        assertEquals(Rank.ACE, updatedCard.getRank());
    }

    @Test
    public void testDeleteById() {
        // Create and save a new Card entity
        Card card = new Card(Suit.HEARTS, Rank.ACE);
        Card savedCard = cardRepository.save(card);

        // Delete the Card entity by its ID
        cardRepository.deleteById(savedCard.getId());

        // Verify
        Optional<Card> retrievedCard = cardRepository.findById(savedCard.getId());
        assertFalse(retrievedCard.isPresent());
    }

    @Test
    public void testDeleteAll() {
        // Save multiple Card entities
        Card card1 = new Card(Suit.CLUBS, Rank.KING);
        Card card2 = new Card(Suit.SPADES, Rank.QUEEN);
        cardRepository.save(card1);
        cardRepository.save(card2);

        // Delete all Card entities
        cardRepository.deleteAll();

        // Verify
        List<Card> cards = cardRepository.findAll();
        assertTrue(cards.isEmpty());
    }

    @Test
    public void testCount() {
        // Save multiple Card entities
        Card card1 = new Card(Suit.CLUBS, Rank.KING);
        Card card2 = new Card(Suit.SPADES, Rank.QUEEN);
        cardRepository.save(card1);
        cardRepository.save(card2);

        // Count the number of Card entities
        long count = cardRepository.count();

        // Verify
        assertEquals(2, count);
    }

    @Test
    public void testExistsById() {
        // Create and save a new Card entity
        Card card = new Card(Suit.HEARTS, Rank.ACE);
        Card savedCard = cardRepository.save(card);

        // Check if the Card entity exists by its ID
        boolean exists = cardRepository.existsById(savedCard.getId());

        // Verify
        assertTrue(exists);

        // Delete the Card entity by its ID
        cardRepository.deleteById(savedCard.getId());

        // Verify
        exists = cardRepository.existsById(savedCard.getId());
        assertFalse(exists);
    }
}
