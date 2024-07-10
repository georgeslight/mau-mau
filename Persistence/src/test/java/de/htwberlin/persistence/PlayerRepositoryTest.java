package de.htwberlin.persistence;

import de.htwberlin.cardsmanagement.api.enums.Rank;
import de.htwberlin.cardsmanagement.api.enums.Suit;
import de.htwberlin.cardsmanagement.api.model.Card;
import de.htwberlin.playermanagement.api.model.Player;
import de.htwberlin.persistence.repo.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(classes = PersistenceJPAConfig.class)
public class PlayerRepositoryTest {

    @Autowired
    PlayerRepository playerRepository;

    @Test
    public void testSavePlayer() {
        // Create player
        List<Card> hand = List.of(new Card(Suit.HEARTS, Rank.ACE), new Card(Suit.CLUBS, Rank.KING));
        Player player = new Player("John Doe", hand);

        // Save player
        Player savedPlayer = playerRepository.save(player);

        // Assert player is saved correctly
        assertNotNull(savedPlayer);
        assertNotNull(savedPlayer.getId());
        assertEquals(savedPlayer.getName(), "John Doe");
        assertEquals(savedPlayer.getHand().size(), 2);
    }

    @Test
    public void testDeletePlayer() {
        // Create and save a player
        List<Card> hand = List.of(new Card(Suit.SPADES, Rank.TEN), new Card(Suit.CLUBS, Rank.NINE));
        Player player = new Player("Max Mustermann", hand);
        playerRepository.save(player);

        // Delete player
        playerRepository.delete(player);

        // Assert player is deleted
        Player foundPlayer = playerRepository.findById(player.getId()).orElse(null);
        assertNull(foundPlayer);
    }

    /**
     * Testing EntityManager in Hibernate
     */
    @Test
    public void givenCustomRepository_whenInvokeCustomFindMethod_thenEntityIsFound() {
        Player player = new Player();
        player.setHand(List.of(new Card(Suit.CLUBS, Rank.ACE), new Card(Suit.HEARTS, Rank.KING)));
        player.setName("Max Mustermann");

        Player persistedPlayer = playerRepository.save(player);

        assertEquals(persistedPlayer, playerRepository.customFindMethod(player.getId()));
    }

}
