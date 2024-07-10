package de.htwberlin.persistence;

import de.htwberlin.cardsmanagement.api.enums.Suit;
import de.htwberlin.gameengine.api.model.GameState;
import de.htwberlin.persistence.repo.GameRepository;
import de.htwberlin.persistence.repo.RulesRepository;
import de.htwberlin.rulesmanagement.api.model.Rules;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@ActiveProfiles("test")
@SpringJUnitConfig(classes = PersistenceJPAConfig.class)
public class RulesRepositoryTest {

    @Autowired
    private RulesRepository rulesRepository;

    @Autowired
    private GameRepository gameRepository;

    @BeforeEach
    public void setUp() {
        gameRepository.deleteAll();
        rulesRepository.deleteAll();
    }

    @Test
    public void testFindRulesWithMoreCardsToBeDrawnThan() {
        // Create and save Rules entities
        Rules rules1 = new Rules();
        rules1.setCardsTObeDrawn(5);
        rulesRepository.save(rules1);

        Rules rules2 = new Rules();
        rules2.setCardsTObeDrawn(10);
        rulesRepository.save(rules2);

        Rules rules3 = new Rules();
        rules3.setCardsTObeDrawn(7);
        rulesRepository.save(rules3);

        // Retrieve Rules entities with more cards to be drawn than specified value
        List<Rules> retrievedRules = rulesRepository.findRulesWithMoreCardsToBeDrawnThan(7);

        // Verify
        assertEquals(1, retrievedRules.size());
        assertEquals(rules2.getId(), retrievedRules.get(0).getId());
    }

    @Test
    public void testFindRulesByWishCard() {
        // Create and save Rules entities
        Rules rules1 = new Rules();
        rules1.setWishCard(Suit.HEARTS);
        rulesRepository.save(rules1);

        Rules rules2 = new Rules();
        rules2.setWishCard(Suit.SPADES);
        rulesRepository.save(rules2);

        // Retrieve Rules entities by wish card
        List<Rules> retrievedRules = rulesRepository.findRulesByWishCard(Suit.HEARTS);

        // Verify
        assertEquals(1, retrievedRules.size());
        assertEquals(rules1.getId(), retrievedRules.get(0).getId());
    }

    @Test
    public void testFindRulesByGameId() {
        // Create a new Rules entity
        Rules rules = new Rules();
        rules.setWishCard(Suit.HEARTS);
        rulesRepository.save(rules);

        // Create a new GameState entity and associate it with the Rules entity
        GameState gameState = new GameState();
        gameState.setCurrentPlayerIndex(1);
        gameState.setGameRunning(true);
        gameState.setRules(rules);
        GameState savedGame = gameRepository.save(gameState);

        // Retrieve the Rules entity by GameState ID
        Rules retrievedRules = rulesRepository.findRulesByGameId(gameState.getId());

        // Verify
        assertNotNull(retrievedRules);
        assertEquals(savedGame.getRules().getId(), retrievedRules.getId());
    }

    @Test
    public void testFindRulesWhereSkipNextPlayerTurnIsTrue() {
        // Create and save Rules entities
        Rules rules1 = new Rules();
        rules1.setSkipNextPlayerTurn(true);
        rulesRepository.save(rules1);

        Rules rules2 = new Rules();
        rules2.setSkipNextPlayerTurn(false);
        rulesRepository.save(rules2);

        // Retrieve Rules entities where skipNextPlayerTurn is true
        List<Rules> retrievedRules = rulesRepository.findRulesWhereSkipNextPlayerTurnIsTrue();

        // Verify
        assertEquals(1, retrievedRules.size());
        assertEquals(rules1.getId(), retrievedRules.get(0).getId());
    }

    @Test
    public void testFindRulesWhereCanPlayAgain() {
        // Create and save Rules entities
        Rules rules1 = new Rules();
        rules1.setCanPlayAgain(true);
        rulesRepository.save(rules1);

        Rules rules2 = new Rules();
        rules2.setCanPlayAgain(false);
        rulesRepository.save(rules2);

        // Retrieve Rules entities where canPlayAgain is true
        List<Rules> retrievedRules = rulesRepository.findRulesWhereCanPlayAgain(true);

        // Verify
        assertEquals(1, retrievedRules.size());
        assertEquals(rules1.getId(), retrievedRules.get(0).getId());
    }
}
