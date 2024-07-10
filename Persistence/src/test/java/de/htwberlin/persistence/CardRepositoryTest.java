package de.htwberlin.persistence;

import de.htwberlin.cardsmanagement.api.enums.Rank;
import de.htwberlin.cardsmanagement.api.enums.Suit;
import de.htwberlin.cardsmanagement.api.model.Card;
import de.htwberlin.persistence.repo.CardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringJUnitConfig(classes = PersistenceJPAConfig.class)
public class CardRepositoryTest {

    @Autowired
    private CardRepository cardRepository;

    @Test
    public void testSaveCard() {
        // Create a new Card entity
        Card card= new Card(Suit.HEARTS, Rank.ACE);

        // Save the Card entity in db
        Card savedCard = cardRepository.save(card);

        // Verify
        assertNotNull(savedCard);
        assertNotNull(savedCard.getId());
        assertEquals(Suit.HEARTS, savedCard.getSuit());
        assertEquals(Rank.ACE, savedCard.getRank());
    }
}
