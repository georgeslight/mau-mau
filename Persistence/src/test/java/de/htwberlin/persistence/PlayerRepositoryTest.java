package de.htwberlin.persistence;

import de.htwberlin.cardsmanagement.api.enums.Rank;
import de.htwberlin.cardsmanagement.api.enums.Suit;
import de.htwberlin.cardsmanagement.api.model.Card;
import de.htwberlin.playermanagement.api.model.Player;
import de.htwberlin.persistence.repo.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;

@Transactional
@ActiveProfiles("test")
@SpringJUnitConfig(classes = PersistenceJPAConfig.class)
public class PlayerRepositoryTest {

    @Autowired
    PlayerRepository playerRepository;

    @BeforeEach
    public void setUp() {
        playerRepository.deleteAll();
    }

    @Test
    public void testFindByName() {
        // Create and save a new Player entity
        Player player = new Player("John Doe", new ArrayList<>(), false);
        playerRepository.save(player);

        // Retrieve the Player entity by its name
        List<Player> players = playerRepository.findByName("John Doe");

        // Verify
        assertEquals(1, players.size());
        assertEquals("John Doe", players.get(0).getName());
    }

    @Test
    public void testFindByRankingPointsGreaterThan() {
        // Create and save a new Players entity
        Player player1 = new Player("Player1", new ArrayList<>(), false);
        player1.setRankingPoints(10);
        Player player2 = new Player("Player2", new ArrayList<>(), false);
        player2.setRankingPoints(15);
        Player player3 = new Player("Player3", new ArrayList<>(), false);
        player3.setRankingPoints(20);
        Player player4 = new Player("Player4", new ArrayList<>(), false);
        player4.setRankingPoints(30);
        playerRepository.save(player1);
        playerRepository.save(player2);
        playerRepository.save(player3);
        playerRepository.save(player4);

        // Retrieve the Player entity by RankingPoints > 15
        List<Player> players = playerRepository.findByRankingPointsGreaterThan(15);

        // Verify
        assertEquals(2, players.size());
        // Verify that "Player2", "Player3", and "Player4" are in the result
        assertTrue(players.stream()
                .map(Player::getName)
                .collect(Collectors.toList())
                .containsAll(List.of("Player3", "Player4")));
    }

    @Test
    public void testFindByHandSuit() {
        // Create and Save player1
        List<Card> hand1 = new ArrayList<>();
        hand1.add(new Card(Suit.HEARTS, Rank.ACE));
        Player player1 = new Player("Player1", hand1, false);
        playerRepository.save(player1);

        // Create and Save player2
        List<Card> hand2 = new ArrayList<>();
        hand2.add(new Card(Suit.HEARTS, Rank.KING));
        Player player2 = new Player("Player2", hand2, false);
        playerRepository.save(player2);

        // Create and Save player3
        List<Card> hand3 = new ArrayList<>();
        hand3.add(new Card(Suit.CLUBS, Rank.EIGHT));
        Player player3 = new Player("Player3", hand3, false);
        playerRepository.save(player3);

        // Retrieve
        List<Player> players = playerRepository.findByHandSuit(Suit.HEARTS);

        // Verify
        assertEquals(2, players.size());
        assertTrue(players.stream()
                .map(Player::getName)
                .collect(Collectors.toList())
                .containsAll(List.of("Player1", "Player2")));
    }

    @Test
    public void testFindBySaidMau() {
        // Create and Save player1
        Player player1 = new Player("Player1", anyList(), false);
        player1.setSaidMau(true);
        playerRepository.save(player1);

        // Create and Save player2
        Player player2 = new Player("Player2", anyList(), false);
        player2.setSaidMau(false);
        playerRepository.save(player2);

        // Create and Save player3
        Player player3 = new Player("Player3", anyList(), false);
        player3.setSaidMau(true);
        playerRepository.save(player3);

        List<Player> players = playerRepository.findBySaidMau(true);
        assertEquals(2, players.size());
        assertTrue(players.stream()
                .map(Player::getName)
                .collect(Collectors.toList())
                .containsAll(List.of("Player1", "Player3")));
    }
}
