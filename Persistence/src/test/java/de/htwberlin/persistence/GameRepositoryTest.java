package de.htwberlin.persistence;

import de.htwberlin.cardsmanagement.api.model.Card;
import de.htwberlin.cardsmanagement.impl.CardService;
import de.htwberlin.gameengine.api.model.GameState;
import de.htwberlin.gameengine.impl.GameService;
import de.htwberlin.persistence.repo.GameRepository;
import de.htwberlin.persistence.repo.PlayerRepository;
import de.htwberlin.playermanagement.api.model.Player;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

//@Transactional
@SpringJUnitConfig(classes = PersistenceJPAConfig.class)
public class GameRepositoryTest {

    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private GameService gameService;
    @Autowired
    private CardService cardService;


    @Test
    @Transactional
    public void testFindAllPlayers() {
        List<Card> hand1 = List.of(mock(Card.class));
        List<Card> hand2 = List.of(mock(Card.class));

        Player player1 = new Player("Max Mustermannn", hand1);
        Player player2 = new Player("John Doee", hand2);

        player1 = playerRepository.save(player1);
        player2 = playerRepository.save(player2);

        GameState gameState = new GameState();
        gameState.setPlayers(List.of(player1, player2));
        GameState savedGame = gameRepository.save(gameState);

        Collection<Player> foundPlayers = gameRepository.findAllPlayerByGameId(savedGame.getId());
        assertNotNull(foundPlayers);
        assertEquals(savedGame.getPlayers().size(), foundPlayers.size());
    }

    @Test
    public void initGame() {
        GameState gameState = gameService.initializeGame("John Doe", 2);
        gameRepository.persistInitGame(gameState);
    }
}
