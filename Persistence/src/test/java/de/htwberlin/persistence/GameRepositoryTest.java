package de.htwberlin.persistence;

import de.htwberlin.cardsmanagement.api.model.Card;
import de.htwberlin.gameengine.api.model.GameState;
import de.htwberlin.gameengine.api.service.GameManagerInterface;
import de.htwberlin.persistence.repo.CardRepository;
import de.htwberlin.persistence.repo.GameRepository;
import de.htwberlin.persistence.repo.PlayerRepository;
import de.htwberlin.persistence.repo.RulesRepository;
import de.htwberlin.playermanagement.api.model.Player;
import de.htwberlin.rulesmanagement.api.model.Rules;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@ActiveProfiles("test")
@SpringJUnitConfig(classes = PersistenceJPAConfig.class)
public class GameRepositoryTest {

    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private RulesRepository rulesRepository;
    @Autowired
    private GameManagerInterface gameService;

    @BeforeEach
    public void setUp() {
        gameRepository.deleteAll();
        playerRepository.deleteAll();
        cardRepository.deleteAll();
        rulesRepository.deleteAll();
    }

    @Test
    public void testSaveGame() {
        // Initialize the game
        GameState game = gameService.initializeGame("MainPlayer", 4);

        // Save the game state
        gameRepository.saveGame(game);

        // Verify that the game state is saved
        assertNotNull(game.getId());

        // Fetch the saved game state
        GameState fetchedGame = gameRepository.findById(game.getId()).orElse(null);

        // Verify game with fetchedGame
        assertNotNull(fetchedGame);
        assertEquals(game.getPlayers().size(), fetchedGame.getPlayers().size());
        assertEquals(game.getDeck().size(), fetchedGame.getDeck().size());
        assertEquals(game.getDiscardPile().size(), fetchedGame.getDiscardPile().size());
        assertEquals(game.isGameRunning(), fetchedGame.isGameRunning());
        assertEquals(game.getCurrentPlayerIndex(), fetchedGame.getCurrentPlayerIndex());

        // Verify Cards
        List<Card> cards = cardRepository.findAll();
        assertEquals(32, cards.size());

        long playerCardsCount = cardRepository.findPlayerCardsByGameId(game.getId()).size();
        assertEquals(20, playerCardsCount);

        long pileCardsCount = cardRepository.findPileCardsByGameId(game.getId()).size();
        assertEquals(1, pileCardsCount);

        long deckCardsCount = cardRepository.findDeckCardsByGameId(game.getId()).size();
        assertEquals(11, deckCardsCount);

        // Verify Players
        List<Player> players = playerRepository.findAll();
        assertEquals(game.getPlayers().size(), players.size());

        List<Player> playersByGameId = playerRepository.findPlayerByGameId(game.getId());
        // Verify that both lists are identical
        assertEquals(players.stream()
                .sorted((p1, p2) -> p1.getId().compareTo(p2.getId()))
                .map(Player::getId)
                .collect(Collectors.toList()), playersByGameId.stream()
                .sorted((p1, p2) -> p1.getId().compareTo(p2.getId()))
                .map(Player::getId)
                .collect(Collectors.toList()));

        // Verify Rules
        Rules rules = rulesRepository.findById(game.getRules().getId()).orElse(null);
        assertNotNull(rules);
        assertEquals(game.getRules().getId(), rules.getId());
    }
}
